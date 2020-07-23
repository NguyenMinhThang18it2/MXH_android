package thang.com.uptimum.model;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class FileMxh {
    @SerializedName("image")
    private String[] image;
    @SerializedName("imageComment")
    private String imageComment;
    @SerializedName("video")
    private String video;
    @SerializedName("background")
    private String background;

    public FileMxh(String[] image, String imageComment, String video, String background) {
        this.image = image;
        this.imageComment = imageComment;
        this.video = video;
        this.background = background;
    }

    @Override
    public String toString() {
        return "FileMxh{" +
                "image=" + Arrays.toString(image) +
                ", imageComment='" + imageComment + '\'' +
                ", video='" + video + '\'' +
                ", background='" + background + '\'' +
                '}';
    }

    public String[] getImage() {
        return image;
    }

    public void setImage(String[] image) {
        this.image = image;
    }

    public String getImageComment() {
        return imageComment;
    }

    public void setImageComment(String imageComment) {
        this.imageComment = imageComment;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }
}
