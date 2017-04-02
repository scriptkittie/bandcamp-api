
package net.cypher.domain;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Band implements BandType 
{
	
	@SerializedName("public_video_ct")
	@Expose
	private Object publicVideoCt;
	@SerializedName("header_phone")
	@Expose
	private HeaderPhone headerPhone;
	@SerializedName("has_discounts")
	@Expose
	private boolean hasDiscounts;
	@SerializedName("thanks_enabled")
	@Expose
	private long thanksEnabled;
	@SerializedName("design")
	@Expose
	private Design design;
	@SerializedName("header_desktop")
	@Expose
	private HeaderDesktop headerDesktop;
	@SerializedName("local_url")
	@Expose
	private String localUrl;
	@SerializedName("private_video_ct")
	@Expose
	private Object privateVideoCt;
	@SerializedName("merch_enabled")
	@Expose
	private boolean merchEnabled;
	@SerializedName("paypal_matching_label_id")
	@Expose
	private Object paypalMatchingLabelId;
	@SerializedName("has_download_codes")
	@Expose
	private boolean hasDownloadCodes;
	@SerializedName("reviews_enabled")
	@Expose
	private long reviewsEnabled;
	@SerializedName("https_url")
	@Expose
	private String httpsUrl;
	@SerializedName("create_date")
	@Expose
	private String createDate;
	@SerializedName("playback_limits")
	@Expose
	private PlaybackLimits playbackLimits;
	@SerializedName("sites")
	@Expose
	private List<Site> sites = new ArrayList<Site>();
	@SerializedName("has_tralbums")
	@Expose
	private boolean hasTralbums;
	@SerializedName("has_label")
	@Expose
	private boolean hasLabel;
	@SerializedName("genre_id")
	@Expose
	private long genreId;
	@SerializedName("has_policies")
	@Expose
	private long hasPolicies;
	@SerializedName("currency")
	@Expose
	private String currency;
	@SerializedName("can_enable_buy_full_discography")
	@Expose
	private boolean canEnableBuyFullDiscography;
	@SerializedName("has_public_tralbums")
	@Expose
	private boolean hasPublicTralbums;
	@SerializedName("is_label")
	@Expose
	private boolean isLabel;
	@SerializedName("fan_email")
	@Expose
	private String fanEmail;
	@SerializedName("name")
	@Expose
	private String name;
	@SerializedName("navbar_items")
	@Expose
	private List<NavbarItem> navbarItems = new ArrayList<NavbarItem>();
	@SerializedName("url_hints")
	@Expose
	private UrlHints urlHints;
	@SerializedName("subdomain")
	@Expose
	private String subdomain;
	@SerializedName("id")
	@Expose
	private long id;
	@SerializedName("buy_full_discography_banner_dismissed")
	@Expose
	private long buyFullDiscographyBannerDismissed;
	@SerializedName("has_items_for_sale")
	@Expose
	private boolean hasItemsForSale;
	@SerializedName("has_public_merch")
	@Expose
	private boolean hasPublicMerch;
	@SerializedName("has_recommendations")
	@Expose
	private boolean hasRecommendations;
	@SerializedName("navbar_hide")
	@Expose
	private boolean navbarHide;
	@SerializedName("google_analytics_id")
	@Expose
	private Object googleAnalyticsId;
	@SerializedName("https_local_url")
	@Expose
	private String httpsLocalUrl;
	@SerializedName("has_any_downloads")
	@Expose
	private boolean hasAnyDownloads;
	@SerializedName("url")
	@Expose
	private String url;
	
	private List<Album> albumList = new ArrayList<Album>();
	private List<Album> trackList = new ArrayList<Album>();

	public Object getPublicVideoCt() {
		return publicVideoCt;
	}

	public void setPublicVideoCt(Object publicVideoCt) {
		this.publicVideoCt = publicVideoCt;
	}

	public HeaderPhone getHeaderPhone() {
		return headerPhone;
	}

	public void setHeaderPhone(HeaderPhone headerPhone) {
		this.headerPhone = headerPhone;
	}

	public boolean isHasDiscounts() {
		return hasDiscounts;
	}

	public void setHasDiscounts(boolean hasDiscounts) {
		this.hasDiscounts = hasDiscounts;
	}

	public long getThanksEnabled() {
		return thanksEnabled;
	}

	public void setThanksEnabled(long thanksEnabled) {
		this.thanksEnabled = thanksEnabled;
	}

	public Design getDesign() {
		return design;
	}

	public void setDesign(Design design) {
		this.design = design;
	}

	public HeaderDesktop getHeaderDesktop() {
		return headerDesktop;
	}

	public void setHeaderDesktop(HeaderDesktop headerDesktop) {
		this.headerDesktop = headerDesktop;
	}

	public String getLocalUrl() {
		return localUrl;
	}

	public void setLocalUrl(String localUrl) {
		this.localUrl = localUrl;
	}

	public Object getPrivateVideoCt() {
		return privateVideoCt;
	}

	public void setPrivateVideoCt(Object privateVideoCt) {
		this.privateVideoCt = privateVideoCt;
	}

	public boolean isMerchEnabled() {
		return merchEnabled;
	}

	public void setMerchEnabled(boolean merchEnabled) {
		this.merchEnabled = merchEnabled;
	}

	public Object getPaypalMatchingLabelId() {
		return paypalMatchingLabelId;
	}

	public void setPaypalMatchingLabelId(Object paypalMatchingLabelId) {
		this.paypalMatchingLabelId = paypalMatchingLabelId;
	}

	public boolean isHasDownloadCodes() {
		return hasDownloadCodes;
	}

	public void setHasDownloadCodes(boolean hasDownloadCodes) {
		this.hasDownloadCodes = hasDownloadCodes;
	}

	public long getReviewsEnabled() {
		return reviewsEnabled;
	}

	public void setReviewsEnabled(long reviewsEnabled) {
		this.reviewsEnabled = reviewsEnabled;
	}

	public String getHttpsUrl() {
		return httpsUrl;
	}

	public void setHttpsUrl(String httpsUrl) {
		this.httpsUrl = httpsUrl;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public PlaybackLimits getPlaybackLimits() {
		return playbackLimits;
	}

	public void setPlaybackLimits(PlaybackLimits playbackLimits) {
		this.playbackLimits = playbackLimits;
	}

	public List<Site> getSites() {
		return sites;
	}

	public void setSites(List<Site> sites) {
		this.sites = sites;
	}

	public boolean isHasTralbums() {
		return hasTralbums;
	}

	public void setHasTralbums(boolean hasTralbums) {
		this.hasTralbums = hasTralbums;
	}

	public boolean isHasLabel() {
		return hasLabel;
	}

	public void setHasLabel(boolean hasLabel) {
		this.hasLabel = hasLabel;
	}

	public long getGenreId() {
		return genreId;
	}

	public void setGenreId(long genreId) {
		this.genreId = genreId;
	}

	public long getHasPolicies() {
		return hasPolicies;
	}

	public void setHasPolicies(long hasPolicies) {
		this.hasPolicies = hasPolicies;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public boolean isCanEnableBuyFullDiscography() {
		return canEnableBuyFullDiscography;
	}

	public void setCanEnableBuyFullDiscography(boolean canEnableBuyFullDiscography) {
		this.canEnableBuyFullDiscography = canEnableBuyFullDiscography;
	}

	public boolean isHasPublicTralbums() {
		return hasPublicTralbums;
	}

	public void setHasPublicTralbums(boolean hasPublicTralbums) {
		this.hasPublicTralbums = hasPublicTralbums;
	}

	public boolean isIsLabel() {
		return isLabel;
	}

	public void setIsLabel(boolean isLabel) {
		this.isLabel = isLabel;
	}

	public String getFanEmail() {
		return fanEmail;
	}

	public void setFanEmail(String fanEmail) {
		this.fanEmail = fanEmail;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<NavbarItem> getNavbarItems() {
		return navbarItems;
	}

	public void setNavbarItems(List<NavbarItem> navbarItems) {
		this.navbarItems = navbarItems;
	}

	public UrlHints getUrlHints() {
		return urlHints;
	}

	public void setUrlHints(UrlHints urlHints) {
		this.urlHints = urlHints;
	}

	public String getSubdomain() {
		return subdomain;
	}

	public void setSubdomain(String subdomain) {
		this.subdomain = subdomain;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getBuyFullDiscographyBannerDismissed() {
		return buyFullDiscographyBannerDismissed;
	}

	public void setBuyFullDiscographyBannerDismissed(long buyFullDiscographyBannerDismissed) {
		this.buyFullDiscographyBannerDismissed = buyFullDiscographyBannerDismissed;
	}

	public boolean isHasItemsForSale() {
		return hasItemsForSale;
	}

	public void setHasItemsForSale(boolean hasItemsForSale) {
		this.hasItemsForSale = hasItemsForSale;
	}

	public boolean isHasPublicMerch() {
		return hasPublicMerch;
	}

	public void setHasPublicMerch(boolean hasPublicMerch) {
		this.hasPublicMerch = hasPublicMerch;
	}

	public boolean isHasRecommendations() {
		return hasRecommendations;
	}

	public void setHasRecommendations(boolean hasRecommendations) {
		this.hasRecommendations = hasRecommendations;
	}

	public boolean isNavbarHide() {
		return navbarHide;
	}

	public void setNavbarHide(boolean navbarHide) {
		this.navbarHide = navbarHide;
	}

	public Object getGoogleAnalyticsId() {
		return googleAnalyticsId;
	}

	public void setGoogleAnalyticsId(Object googleAnalyticsId) {
		this.googleAnalyticsId = googleAnalyticsId;
	}

	public String getHttpsLocalUrl() {
		return httpsLocalUrl;
	}

	public void setHttpsLocalUrl(String httpsLocalUrl) {
		this.httpsLocalUrl = httpsLocalUrl;
	}

	public boolean isHasAnyDownloads() {
		return hasAnyDownloads;
	}

	public void setHasAnyDownloads(boolean hasAnyDownloads) {
		this.hasAnyDownloads = hasAnyDownloads;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	

	public List<Album> getAlbums() {
		return albumList;
	}

	public void setAlbum(List<Album> album) {
		this.albumList = album;
	}
	
	public void addAlbum(Album album)
	{
		this.albumList.add(album);
	}
	
	public List<Album> getTrackList() {
		return trackList;
	}

	public void setTrackList(List<Album> trackList) {
		this.trackList = trackList;
	}
	
	public void addTrack(Album track)
	{
		this.trackList.add(track);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
