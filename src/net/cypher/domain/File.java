
package net.cypher.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class File {

    @SerializedName("mp3-128")
    @Expose
    private String mp3128;

    public String getMp3128() {
        return mp3128;
    }

    public void setMp3128(String mp3128) {
        this.mp3128 = mp3128;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
