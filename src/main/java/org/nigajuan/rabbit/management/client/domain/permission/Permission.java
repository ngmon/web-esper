
package org.nigajuan.rabbit.management.client.domain.permission;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "user",
    "vhost",
    "configure",
    "write",
    "read"
})
public class Permission {

    @JsonProperty("user")
    private String user;
    @JsonProperty("vhost")
    private String vhost;
    @JsonProperty("configure")
    private String configure;
    @JsonProperty("write")
    private String write;
    @JsonProperty("read")
    private String read;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The user
     */
    @JsonProperty("user")
    public String getUser() {
        return user;
    }

    /**
     * 
     * @param user
     *     The user
     */
    @JsonProperty("user")
    public void setUser(String user) {
        this.user = user;
    }

    public Permission withUser(String user) {
        this.user = user;
        return this;
    }

    /**
     * 
     * @return
     *     The vhost
     */
    @JsonProperty("vhost")
    public String getVhost() {
        return vhost;
    }

    /**
     * 
     * @param vhost
     *     The vhost
     */
    @JsonProperty("vhost")
    public void setVhost(String vhost) {
        this.vhost = vhost;
    }

    public Permission withVhost(String vhost) {
        this.vhost = vhost;
        return this;
    }

    /**
     * 
     * @return
     *     The configure
     */
    @JsonProperty("configure")
    public String getConfigure() {
        return configure;
    }

    /**
     * 
     * @param configure
     *     The configure
     */
    @JsonProperty("configure")
    public void setConfigure(String configure) {
        this.configure = configure;
    }

    public Permission withConfigure(String configure) {
        this.configure = configure;
        return this;
    }

    /**
     * 
     * @return
     *     The write
     */
    @JsonProperty("write")
    public String getWrite() {
        return write;
    }

    /**
     * 
     * @param write
     *     The write
     */
    @JsonProperty("write")
    public void setWrite(String write) {
        this.write = write;
    }

    public Permission withWrite(String write) {
        this.write = write;
        return this;
    }

    /**
     * 
     * @return
     *     The read
     */
    @JsonProperty("read")
    public String getRead() {
        return read;
    }

    /**
     * 
     * @param read
     *     The read
     */
    @JsonProperty("read")
    public void setRead(String read) {
        this.read = read;
    }

    public Permission withRead(String read) {
        this.read = read;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Permission withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(user).append(vhost).append(configure).append(write).append(read).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Permission) == false) {
            return false;
        }
        Permission rhs = ((Permission) other);
        return new EqualsBuilder().append(user, rhs.user).append(vhost, rhs.vhost).append(configure, rhs.configure).append(write, rhs.write).append(read, rhs.read).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
