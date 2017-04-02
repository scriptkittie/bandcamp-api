
package io.laniakia.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class UrlHints {

    @SerializedName("custom_domain")
    @Expose
    private Object customDomain;
    @SerializedName("subdomain")
    @Expose
    private String subdomain;
    @SerializedName("custom_domain_verified")
    @Expose
    private Object customDomainVerified;

    public Object getCustomDomain() {
        return customDomain;
    }

    public void setCustomDomain(Object customDomain) {
        this.customDomain = customDomain;
    }

    public String getSubdomain() {
        return subdomain;
    }

    public void setSubdomain(String subdomain) {
        this.subdomain = subdomain;
    }

    public Object getCustomDomainVerified() {
        return customDomainVerified;
    }

    public void setCustomDomainVerified(Object customDomainVerified) {
        this.customDomainVerified = customDomainVerified;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
