
package io.laniakia.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class HeaderPhone {

    @SerializedName("width")
    @Expose
    private long width;
    @SerializedName("image")
    @Expose
    private Image image;
    @SerializedName("image_id")
    @Expose
    private long imageId;
    @SerializedName("height")
    @Expose
    private long height;

    public long getWidth() {
        return width;
    }

    public void setWidth(long width) {
        this.width = width;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
