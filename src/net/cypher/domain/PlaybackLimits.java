
package net.cypher.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class PlaybackLimits {

    @SerializedName("streaming_limit")
    @Expose
    private long streamingLimit;
    @SerializedName("streaming_limits_enabled")
    @Expose
    private boolean streamingLimitsEnabled;

    public long getStreamingLimit() {
        return streamingLimit;
    }

    public void setStreamingLimit(long streamingLimit) {
        this.streamingLimit = streamingLimit;
    }

    public boolean isStreamingLimitsEnabled() {
        return streamingLimitsEnabled;
    }

    public void setStreamingLimitsEnabled(boolean streamingLimitsEnabled) {
        this.streamingLimitsEnabled = streamingLimitsEnabled;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
