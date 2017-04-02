
package net.cypher.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Design {

    @SerializedName("link_color")
    @Expose
    private String linkColor;
    @SerializedName("defaultbg")
    @Expose
    private boolean defaultbg;
    @SerializedName("bg_image_id")
    @Expose
    private long bgImageId;
    @SerializedName("bg_file_id")
    @Expose
    private Object bgFileId;
    @SerializedName("hd_ft_color")
    @Expose
    private String hdFtColor;
    @SerializedName("bg_file_name")
    @Expose
    private String bgFileName;
    @SerializedName("bg_color")
    @Expose
    private String bgColor;
    @SerializedName("navbar_bg_color")
    @Expose
    private String navbarBgColor;
    @SerializedName("body_color")
    @Expose
    private String bodyColor;
    @SerializedName("bg_fixed")
    @Expose
    private long bgFixed;
    @SerializedName("invert_iconography")
    @Expose
    private Object invertIconography;
    @SerializedName("bg_behavior")
    @Expose
    private String bgBehavior;
    @SerializedName("text_color")
    @Expose
    private String textColor;
    @SerializedName("tile_bg")
    @Expose
    private long tileBg;
    @SerializedName("secondary_text_color")
    @Expose
    private String secondaryTextColor;
    @SerializedName("bg_src")
    @Expose
    private Object bgSrc;
    @SerializedName("bg_halign")
    @Expose
    private String bgHalign;

    public String getLinkColor() {
        return linkColor;
    }

    public void setLinkColor(String linkColor) {
        this.linkColor = linkColor;
    }

    public boolean isDefaultbg() {
        return defaultbg;
    }

    public void setDefaultbg(boolean defaultbg) {
        this.defaultbg = defaultbg;
    }

    public long getBgImageId() {
        return bgImageId;
    }

    public void setBgImageId(long bgImageId) {
        this.bgImageId = bgImageId;
    }

    public Object getBgFileId() {
        return bgFileId;
    }

    public void setBgFileId(Object bgFileId) {
        this.bgFileId = bgFileId;
    }

    public String getHdFtColor() {
        return hdFtColor;
    }

    public void setHdFtColor(String hdFtColor) {
        this.hdFtColor = hdFtColor;
    }

    public String getBgFileName() {
        return bgFileName;
    }

    public void setBgFileName(String bgFileName) {
        this.bgFileName = bgFileName;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public String getNavbarBgColor() {
        return navbarBgColor;
    }

    public void setNavbarBgColor(String navbarBgColor) {
        this.navbarBgColor = navbarBgColor;
    }

    public String getBodyColor() {
        return bodyColor;
    }

    public void setBodyColor(String bodyColor) {
        this.bodyColor = bodyColor;
    }

    public long getBgFixed() {
        return bgFixed;
    }

    public void setBgFixed(long bgFixed) {
        this.bgFixed = bgFixed;
    }

    public Object getInvertIconography() {
        return invertIconography;
    }

    public void setInvertIconography(Object invertIconography) {
        this.invertIconography = invertIconography;
    }

    public String getBgBehavior() {
        return bgBehavior;
    }

    public void setBgBehavior(String bgBehavior) {
        this.bgBehavior = bgBehavior;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public long getTileBg() {
        return tileBg;
    }

    public void setTileBg(long tileBg) {
        this.tileBg = tileBg;
    }

    public String getSecondaryTextColor() {
        return secondaryTextColor;
    }

    public void setSecondaryTextColor(String secondaryTextColor) {
        this.secondaryTextColor = secondaryTextColor;
    }

    public Object getBgSrc() {
        return bgSrc;
    }

    public void setBgSrc(Object bgSrc) {
        this.bgSrc = bgSrc;
    }

    public String getBgHalign() {
        return bgHalign;
    }

    public void setBgHalign(String bgHalign) {
        this.bgHalign = bgHalign;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
