
package net.cypher.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Album {

    @SerializedName("selling_band_id")
    @Expose
    private Long sellingBandId;
    @SerializedName("require_email")
    @Expose
    private Object requireEmail;
    @SerializedName("publish_date")
    @Expose
    private String publishDate;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("band_id")
    @Expose
    private Long bandId;
    @SerializedName("new_desc_format")
    @Expose
    private Long newDescFormat;
    @SerializedName("require_email_0")
    @Expose
    private Object requireEmail0;
    @SerializedName("upc")
    @Expose
    private Object upc;
    @SerializedName("about")
    @Expose
    private String about;
    @SerializedName("mod_date")
    @Expose
    private String modDate;
    @SerializedName("purchase_url")
    @Expose
    private Object purchaseUrl;
    @SerializedName("minimum_price")
    @Expose
    private Double minimumPrice;
    @SerializedName("private")
    @Expose
    private Object _private;
    @SerializedName("credits")
    @Expose
    private Object credits;
    @SerializedName("set_price")
    @Expose
    private Double setPrice;
    @SerializedName("killed")
    @Expose
    private Object killed;
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("purchase_title")
    @Expose
    private Object purchaseTitle;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;
    @SerializedName("auto_repriced")
    @Expose
    private Object autoRepriced;
    @SerializedName("is_set_price")
    @Expose
    private Object isSetPrice;
    @SerializedName("new_date")
    @Expose
    private String newDate;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("featured_track_id")
    @Expose
    private Long featuredTrackId;
    @SerializedName("download_desc_id")
    @Expose
    private Object downloadDescId;
    @SerializedName("art_id")
    @Expose
    private Long artId;
    @SerializedName("artist")
    @Expose
    private Object artist;
    @SerializedName("minimum_price_nonzero")
    @Expose
    private Double minimumPriceNonzero;
    @SerializedName("download_pref")
    @Expose
    private Long downloadPref;
    @SerializedName("audit")
    @Expose
    private Long audit;
    
    private List<Track> trackList = new ArrayList<Track>();
    
    private boolean isTrack = false;

    public Long getSellingBandId() {
        return sellingBandId;
    }

    public void setSellingBandId(Long sellingBandId) {
        this.sellingBandId = sellingBandId;
    }

    public Object getRequireEmail() {
        return requireEmail;
    }

    public void setRequireEmail(Object requireEmail) {
        this.requireEmail = requireEmail;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getBandId() {
        return bandId;
    }

    public void setBandId(Long bandId) {
        this.bandId = bandId;
    }

    public Long getNewDescFormat() {
        return newDescFormat;
    }

    public void setNewDescFormat(Long newDescFormat) {
        this.newDescFormat = newDescFormat;
    }

    public Object getRequireEmail0() {
        return requireEmail0;
    }

    public void setRequireEmail0(Object requireEmail0) {
        this.requireEmail0 = requireEmail0;
    }

    public Object getUpc() {
        return upc;
    }

    public void setUpc(Object upc) {
        this.upc = upc;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getModDate() {
        return modDate;
    }

    public void setModDate(String modDate) {
        this.modDate = modDate;
    }

    public Object getPurchaseUrl() {
        return purchaseUrl;
    }

    public void setPurchaseUrl(Object purchaseUrl) {
        this.purchaseUrl = purchaseUrl;
    }

    public Double getMinimumPrice() {
        return minimumPrice;
    }

    public void setMinimumPrice(Double minimumPrice) {
        this.minimumPrice = minimumPrice;
    }

    public Object getPrivate() {
        return _private;
    }

    public void setPrivate(Object _private) {
        this._private = _private;
    }

    public Object getCredits() {
        return credits;
    }

    public void setCredits(Object credits) {
        this.credits = credits;
    }

    public Double getSetPrice() {
        return setPrice;
    }

    public void setSetPrice(Double setPrice) {
        this.setPrice = setPrice;
    }

    public Object getKilled() {
        return killed;
    }

    public void setKilled(Object killed) {
        this.killed = killed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Object getPurchaseTitle() {
        return purchaseTitle;
    }

    public void setPurchaseTitle(Object purchaseTitle) {
        this.purchaseTitle = purchaseTitle;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Object getAutoRepriced() {
        return autoRepriced;
    }

    public void setAutoRepriced(Object autoRepriced) {
        this.autoRepriced = autoRepriced;
    }

    public Object getIsSetPrice() {
        return isSetPrice;
    }

    public void setIsSetPrice(Object isSetPrice) {
        this.isSetPrice = isSetPrice;
    }

    public String getNewDate() {
        return newDate;
    }

    public void setNewDate(String newDate) {
        this.newDate = newDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getFeaturedTrackId() {
        return featuredTrackId;
    }

    public void setFeaturedTrackId(Long featuredTrackId) {
        this.featuredTrackId = featuredTrackId;
    }

    public Object getDownloadDescId() {
        return downloadDescId;
    }

    public void setDownloadDescId(Object downloadDescId) {
        this.downloadDescId = downloadDescId;
    }

    public Long getArtId() {
        return artId;
    }

    public void setArtId(Long artId) {
        this.artId = artId;
    }

    public Object getArtist() {
        return artist;
    }

    public void setArtist(Object artist) {
        this.artist = artist;
    }

    public Double getMinimumPriceNonzero() {
        return minimumPriceNonzero;
    }

    public void setMinimumPriceNonzero(Double minimumPriceNonzero) {
        this.minimumPriceNonzero = minimumPriceNonzero;
    }

    public Long getDownloadPref() {
        return downloadPref;
    }

    public void setDownloadPref(Long downloadPref) {
        this.downloadPref = downloadPref;
    }

    public Long getAudit() {
        return audit;
    }

    public void setAudit(Long audit) {
        this.audit = audit;
    }
    
	public List<Track> getTrackList() {
		return trackList;
	}

	public void setTrackList(List<Track> trackList) {
		this.trackList = trackList;
	}
	
	public void addTrack(Track track)
	{
		this.trackList.add(track);
	}
	
	public boolean isTrack()
	{
		return this.isTrack;
	}

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

	public void setIsTrack(boolean isTrack) {
		this.isTrack = isTrack;
	}

}
