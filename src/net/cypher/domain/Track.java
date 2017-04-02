
package net.cypher.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Track {

    @SerializedName("alt_link")
    @Expose
    private Object altLink;
    @SerializedName("has_info")
    @Expose
    private Boolean hasInfo;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("track_id")
    @Expose
    private Long trackId;
    @SerializedName("video_source_id")
    @Expose
    private Object videoSourceId;
    @SerializedName("track_num")
    @Expose
    private Long trackNum;
    @SerializedName("encoding_error")
    @Expose
    private Object encodingError;
    @SerializedName("is_downloadable")
    @Expose
    private Boolean isDownloadable;
    @SerializedName("video_mobile_url")
    @Expose
    private Object videoMobileUrl;
    @SerializedName("lyrics")
    @Expose
    private Object lyrics;
    @SerializedName("album_preorder")
    @Expose
    private Boolean albumPreorder;
    @SerializedName("encoding_pending")
    @Expose
    private Object encodingPending;
    @SerializedName("video_id")
    @Expose
    private Object videoId;
    @SerializedName("has_free_download")
    @Expose
    private Object hasFreeDownload;
    @SerializedName("streaming")
    @Expose
    private Long streaming;
    @SerializedName("video_poster_url")
    @Expose
    private Object videoPosterUrl;
    @SerializedName("duration")
    @Expose
    private Double duration;
    @SerializedName("unreleased_track")
    @Expose
    private Boolean unreleasedTrack;
    @SerializedName("private")
    @Expose
    private Object _private;
    @SerializedName("play_count")
    @Expose
    private Long playCount;
    @SerializedName("is_draft")
    @Expose
    private Boolean isDraft;
    @SerializedName("free_album_download")
    @Expose
    private Boolean freeAlbumDownload;
    @SerializedName("video_caption")
    @Expose
    private Object videoCaption;
    @SerializedName("title_link")
    @Expose
    private String titleLink;
    @SerializedName("license_type")
    @Expose
    private Long licenseType;
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("is_capped")
    @Expose
    private Boolean isCapped;
    @SerializedName("sizeof_lyrics")
    @Expose
    private Long sizeofLyrics;
    @SerializedName("encodings_id")
    @Expose
    private Long encodingsId;
    @SerializedName("video_featured")
    @Expose
    private Object videoFeatured;
    @SerializedName("has_lyrics")
    @Expose
    private Boolean hasLyrics;
    @SerializedName("file")
    @Expose
    private File file;

    public Object getAltLink() {
        return altLink;
    }

    public void setAltLink(Object altLink) {
        this.altLink = altLink;
    }

    public Boolean getHasInfo() {
        return hasInfo;
    }

    public void setHasInfo(Boolean hasInfo) {
        this.hasInfo = hasInfo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getTrackId() {
        return trackId;
    }

    public void setTrackId(Long trackId) {
        this.trackId = trackId;
    }

    public Object getVideoSourceId() {
        return videoSourceId;
    }

    public void setVideoSourceId(Object videoSourceId) {
        this.videoSourceId = videoSourceId;
    }

    public Long getTrackNum() {
        return trackNum;
    }

    public void setTrackNum(Long trackNum) {
        this.trackNum = trackNum;
    }

    public Object getEncodingError() {
        return encodingError;
    }

    public void setEncodingError(Object encodingError) {
        this.encodingError = encodingError;
    }

    public Boolean getIsDownloadable() {
        return isDownloadable;
    }

    public void setIsDownloadable(Boolean isDownloadable) {
        this.isDownloadable = isDownloadable;
    }

    public Object getVideoMobileUrl() {
        return videoMobileUrl;
    }

    public void setVideoMobileUrl(Object videoMobileUrl) {
        this.videoMobileUrl = videoMobileUrl;
    }

    public Object getLyrics() {
        return lyrics;
    }

    public void setLyrics(Object lyrics) {
        this.lyrics = lyrics;
    }

    public Boolean getAlbumPreorder() {
        return albumPreorder;
    }

    public void setAlbumPreorder(Boolean albumPreorder) {
        this.albumPreorder = albumPreorder;
    }

    public Object getEncodingPending() {
        return encodingPending;
    }

    public void setEncodingPending(Object encodingPending) {
        this.encodingPending = encodingPending;
    }

    public Object getVideoId() {
        return videoId;
    }

    public void setVideoId(Object videoId) {
        this.videoId = videoId;
    }

    public Object getHasFreeDownload() {
        return hasFreeDownload;
    }

    public void setHasFreeDownload(Object hasFreeDownload) {
        this.hasFreeDownload = hasFreeDownload;
    }

    public Long getStreaming() {
        return streaming;
    }

    public void setStreaming(Long streaming) {
        this.streaming = streaming;
    }

    public Object getVideoPosterUrl() {
        return videoPosterUrl;
    }

    public void setVideoPosterUrl(Object videoPosterUrl) {
        this.videoPosterUrl = videoPosterUrl;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public Boolean getUnreleasedTrack() {
        return unreleasedTrack;
    }

    public void setUnreleasedTrack(Boolean unreleasedTrack) {
        this.unreleasedTrack = unreleasedTrack;
    }

    public Object getPrivate() {
        return _private;
    }

    public void setPrivate(Object _private) {
        this._private = _private;
    }

    public Long getPlayCount() {
        return playCount;
    }

    public void setPlayCount(Long playCount) {
        this.playCount = playCount;
    }

    public Boolean getIsDraft() {
        return isDraft;
    }

    public void setIsDraft(Boolean isDraft) {
        this.isDraft = isDraft;
    }

    public Boolean getFreeAlbumDownload() {
        return freeAlbumDownload;
    }

    public void setFreeAlbumDownload(Boolean freeAlbumDownload) {
        this.freeAlbumDownload = freeAlbumDownload;
    }

    public Object getVideoCaption() {
        return videoCaption;
    }

    public void setVideoCaption(Object videoCaption) {
        this.videoCaption = videoCaption;
    }

    public String getTitleLink() {
        return titleLink;
    }

    public void setTitleLink(String titleLink) {
        this.titleLink = titleLink;
    }

    public Long getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(Long licenseType) {
        this.licenseType = licenseType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIsCapped() {
        return isCapped;
    }

    public void setIsCapped(Boolean isCapped) {
        this.isCapped = isCapped;
    }

    public Long getSizeofLyrics() {
        return sizeofLyrics;
    }

    public void setSizeofLyrics(Long sizeofLyrics) {
        this.sizeofLyrics = sizeofLyrics;
    }

    public Long getEncodingsId() {
        return encodingsId;
    }

    public void setEncodingsId(Long encodingsId) {
        this.encodingsId = encodingsId;
    }

    public Object getVideoFeatured() {
        return videoFeatured;
    }

    public void setVideoFeatured(Object videoFeatured) {
        this.videoFeatured = videoFeatured;
    }

    public Boolean getHasLyrics() {
        return hasLyrics;
    }

    public void setHasLyrics(Boolean hasLyrics) {
        this.hasLyrics = hasLyrics;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
