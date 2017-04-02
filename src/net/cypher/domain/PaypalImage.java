
package net.cypher.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class PaypalImage {

    @SerializedName("format")
    @Expose
    private long format;
    @SerializedName("https_url")
    @Expose
    private String httpsUrl;
    @SerializedName("width")
    @Expose
    private long width;
    @SerializedName("image_id")
    @Expose
    private long imageId;
    @SerializedName("height")
    @Expose
    private long height;
    @SerializedName("url")
    @Expose
    private String url;

    public long getFormat() {
        return format;
    }

    public void setFormat(long format) {
        this.format = format;
    }

    public String getHttpsUrl() {
        return httpsUrl;
    }

    public void setHttpsUrl(String httpsUrl) {
        this.httpsUrl = httpsUrl;
    }

    public long getWidth() {
        return width;
    }

    public void setWidth(long width) {
        this.width = width;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
