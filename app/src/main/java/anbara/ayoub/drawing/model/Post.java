package anbara.ayoub.drawing.model;

public class Post {
    int imageId;
    int numberImage;
    private int viewType = 2;

    public int getViewType() {
        return viewType;
    }

    public int getNumberImage() {
        return numberImage;
    }

    public void setNumberImage(int numberImage) {
        this.numberImage = numberImage;
    }

    public Post(int imageId, int numberImage) {
        this.imageId = imageId;
        this.numberImage=numberImage;
    }

    public Post() {
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
