package thang.com.uptimum.model;

import com.google.gson.annotations.SerializedName;

public class TextStory {
    @SerializedName("document")
    private String document;
    @SerializedName("color")
    private String color;

    public TextStory(String document, String color) {
        this.document = document;
        this.color = color;
    }

    @Override
    public String toString() {
        return "TextStory{" +
                "document='" + document + '\'' +
                ", color='" + color + '\'' +
                '}';
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
