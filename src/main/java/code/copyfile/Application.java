package code.copyfile;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import code.copyfile.enums.EnumFileType;
import code.copyfile.model.MainModel;
import code.copyfile.util.GZIPUtil;
import code.copyfile.util.V;
import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

@SuppressWarnings({"ResultOfMethodCallIgnored", "ConstantConditions"})
public class Application {

    private static final String ROOT = System.getProperty("user.dir");
    private static final String ROOT_bin = ROOT + File.separator + "bin" + File.separator;

    /**
     * 执行
     */
    public static void main(String args[]) throws IOException {
        File config = new File(ROOT_bin + "copyFile.json");
        if (ok(config.exists(), "请在jar包旁添加配置文件{}", config)) {
            main(new FileInputStream(config));
        }
    }

    private static void backup(File toPath) {
        File[] files = toPath.listFiles();
        if (V.noEmpty(files)) {
            File toTemp = new File(toPath.getPath() + File.separator + "temp");
            toTemp.mkdir();
            if (Arrays.stream(files).anyMatch((f -> f.getName().equals("pom.xml")))) {
                return;//本地开发环境. 会把pom也清掉
            }
            Console.log("\n  --------备份到temp-------\n");
            Arrays.stream(files).filter(f -> !f.isDirectory() && !f.getName().contains(".bat")).forEach(f -> f.renameTo(new File(toTemp.getPath() + File.separator + f.getName())));
        }
    }

    static void main(InputStream configStream) throws IOException {

        MainModel mainModel = JSON.parseObject(configStream, MainModel.class);

        mainModel.toPath = V.or(mainModel.toPath, new File(ROOT));//toPath默认是jar的上级目录

        backup(mainModel.toPath);

        zipCopy(mainModel);
    }

    private static void zipCopy(MainModel mainModel) {
        Console.error("  -----------开始拷贝----------\n");
        mainModel.files.forEach(file -> {
            File from = file.from;
            File to = file.to;
            EnumFileType type = file.type;
            type = V.or(type, EnumFileType.直接拷贝);
            if (ok(null != from && from.exists(), "{}|源文件{}不存在", type, from)) {
                String toName = V.or(file.toName, from.getName());
                switch (type) {
                    case 直接拷贝:
                        if (null == to) {
                            to = new File(mainModel.toPath.getPath() + File.separator + from.getName());
                        }
                        FileUtil.copy(from, to, true);
                        break;
                    case 压缩拷贝:
                        if (!toName.contains(".tar.gz")) {
                            toName = toName + ".tar.gz";
                        }
                        if (null == to) {
                            to = new File(mainModel.toPath.getPath() + File.separator + toName);
                        }
                        File folder = to.getParentFile();
                        if (!folder.exists() && folder.mkdirs()) {
                            Console.log("创建目录{}", folder);
                        }
                        Console.error("  压缩 {} 中...", from);
                        GZIPUtil.toTarGz(from, to, V.or(file.zipRoot, "/"));
                        break;
                    default:
                        Console.error("异常的文件拷贝类型{}", type);
                }
                Console.error("  {}\n", to);

            }
        });
        Console.log("\n  -----------拷贝完毕----------");
    }


    /**
     * 如果不是true,打印后面的日志
     */
    private static boolean ok(boolean ok, String msgTemplate, Object... msgs) {
        if (!ok) {
            Console.error(StrUtil.format(msgTemplate, msgs));
        }
        return ok;
    }
}
