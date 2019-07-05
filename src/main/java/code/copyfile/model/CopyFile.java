package code.copyfile.model;


import code.copyfile.enums.EnumFileType;

import java.io.File;

public class CopyFile {
    public File from;
    public File to;//为空时,取jar包上一级目录
    public EnumFileType type;//为空时,为直接拷贝
    public String toName;
    public String zipRoot;

    public File getFrom() {
        return from;
    }

    public void setFrom(File from) {
        this.from = from;
    }

    public File getTo() {
        return to;
    }

    public void setTo(File to) {
        this.to = to;
    }

    public EnumFileType getType() {
        return type;
    }

    public void setType(EnumFileType type) {
        this.type = type;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getZipRoot() {
        return zipRoot;
    }

    public void setZipRoot(String zipRoot) {
        this.zipRoot = zipRoot;
    }
}
