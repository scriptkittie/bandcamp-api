
package net.cypher.domain;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class HeaderDesktop {

    @SerializedName("area_tags")
    @Expose
    private List<AreaTag> areaTags = new ArrayList<AreaTag>();
    @SerializedName("width")
    @Expose
    private long width;
    @SerializedName("image")
    @Expose
    private Image_ image;
    @SerializedName("image_id")
    @Expose
    private long imageId;
    @SerializedName("paypal_image")
    @Expose
    private PaypalImage paypalImage;
    @SerializedName("height")
    @Expose
    private long height;
    @SerializedName("using_map")
    @Expose
    private long usingMap;
    @SerializedName("url")
    @Expose
    private Object url;

    public List<AreaTag> getAreaTags() {
        return areaTags;
    }

    public void setAreaTags(List<AreaTag> areaTags) {
        this.areaTags = areaTags;
    }

    public long getWidth() {
        return width;
    }

    public void setWidth(long width) {
        this.width = width;
    }

    public Image_ getImage() {
        return image;
    }

    public void setImage(Image_ image) {
        this.image = image;
    }

    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }

    public PaypalImage getPaypalImage() {
        return paypalImage;
    }

    public void setPaypalImage(PaypalImage paypalImage) {
        this.paypalImage = paypalImage;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    public long getUsingMap() {
        return usingMap;
    }

    public void setUsingMap(long usingMap) {
        this.usingMap = usingMap;
    }

    public Object getUrl() {
        return url;
    }

    public void setUrl(Object url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
