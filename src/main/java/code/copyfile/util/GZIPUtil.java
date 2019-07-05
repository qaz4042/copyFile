package code.copyfile.util;

import cn.hutool.core.io.FileUtil;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

/**
 */
public class GZIPUtil {
    //in order to gzip file to tar.gz,we need to get *.tar file

    /**
     * 打tar包
     */
    private static void pack(File file, File target, String zipRoot) throws IOException {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(target);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        TarArchiveOutputStream aos = new TarArchiveOutputStream(out);
        Map<String, byte[]> pathByteMap = toFileMap(zipRoot, file.listFiles());
        for (String path : pathByteMap.keySet()) {
            byte[] bytes = pathByteMap.get(path);
            TarArchiveEntry entry = new TarArchiveEntry(path);
            entry.setSize(bytes.length);
            aos.putArchiveEntry(entry);
            aos.write(bytes);
            aos.closeArchiveEntry();
        }
        aos.flush();
        aos.close();
    }

    private static Map<String, byte[]> toFileMap(String parentPath, File[] files) {
        Map<String, byte[]> map = new HashMap<>();
        for (File file : files) {
            String path = parentPath + File.separator + file.getName();
            if (file.isDirectory()) {
                if (null != file.listFiles()) {
                    map.putAll(toFileMap(path, file.listFiles()));
                }
            } else {
                map.put(path, FileUtil.readBytes(file));
//                return Collections.singletonMap(parentPath, FileUtil.readBytes(file));
            }
        }
        return map;
    }
    /*    private static Map<String, byte[]> toFileMap(String parentPath, File fileOrDirectory) {
        String pathNew = parentPath + File.separator + fileOrDirectory.getName();
        if (fileOrDirectory.isDirectory()) {
            Map<String, byte[]> map = new HashMap<>();
            if (V.noEmpty(fileOrDirectory.listFiles())) {
                for (File file : fileOrDirectory.listFiles()) {
                    map.putAll(toFileMap(pathNew, file));
                }
            }
            return map;
        } else {
            return Collections.singletonMap(pathNew, FileUtil.readBytes(fileOrDirectory));
        }
    }*/

    /**
     * 压gz缩
     */
    private static void compress(File source, File target) {
        FileInputStream in = null;
        GZIPOutputStream out = null;
        try {
            in = new FileInputStream(source);
            out = new GZIPOutputStream(new FileOutputStream(target));
            byte[] array = new byte[1024];
            int number;
            while ((number = in.read(array, 0, array.length)) != -1) {
                out.write(array, 0, number);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(in);
            close(out);
        }
    }

    private static void close(Closeable out) {
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 打包压缩 tar.gz
     */
    public static void toTarGz(File fromFolder, File to, String zipRoot) {
        try {
            File tempTar = new File(to.getPath() + ".temp");

            pack(fromFolder, tempTar, zipRoot);

            compress(tempTar, to);
            //noinspection ResultOfMethodCallIgnored
            tempTar.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
