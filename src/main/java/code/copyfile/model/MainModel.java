package code.copyfile.model;

import java.io.File;
import java.util.List;

public class MainModel {
    //目标目录 为空则默认是jar包的上级目录
    public File toPath;
    public List<CopyFile> files;
}
