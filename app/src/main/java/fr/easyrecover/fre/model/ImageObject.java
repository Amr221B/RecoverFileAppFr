package fr.easyrecover.fre.model;

public class ImageObject {

    boolean check;
    String path;

    public ImageObject(String path, boolean check) {
        this.path = path;
        this.check = check;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isCheck() {
        return this.check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
