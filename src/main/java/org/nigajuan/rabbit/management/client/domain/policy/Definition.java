
package org.nigajuan.rabbit.management.client.domain.policy;

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
    "ha-mode",
    "dead-letter-routing-key",
    "alternate-exchange",
    "ha-params",
    "ha-sync-mode",
    "message-ttl",
    "expires",
    "max-length",
    "max-length-bytes",
    "dead-letter-exchange"
})
public class Definition {

    @JsonProperty("ha-mode")
    private String haMode;
    @JsonProperty("dead-letter-routing-key")
    private String deadLetterRoutingKey;
    @JsonProperty("alternate-exchange")
    private String alternateExchange;
    @JsonProperty("ha-params")
    private Long haParams;
    @JsonProperty("ha-sync-mode")
    private String haSyncMode;
    @JsonProperty("message-ttl")
    private Long messageTtl;
    @JsonProperty("expires")
    private Long expires;
    @JsonProperty("max-length")
    private Long maxLength;
    @JsonProperty("max-length-bytes")
    private Long maxLengthBytes;
    @JsonProperty("dead-letter-exchange")
    private String deadLetterExchange;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The haMode
     */
    @JsonProperty("ha-mode")
    public String getHaMode() {
        return haMode;
    }

    /**
     * 
     * @param haMode
     *     The ha-mode
     */
    @JsonProperty("ha-mode")
    public void setHaMode(String haMode) {
        this.haMode = haMode;
    }

    public Definition withHaMode(String haMode) {
        this.haMode = haMode;
        return this;
    }

    /**
     * 
     * @return
     *     The deadLetterRoutingKey
     */
    @JsonProperty("dead-letter-routing-key")
    public String getDeadLetterRoutingKey() {
        return deadLetterRoutingKey;
    }

    /**
     * 
     * @param deadLetterRoutingKey
     *     The dead-letter-routing-key
     */
    @JsonProperty("dead-letter-routing-key")
    public void setDeadLetterRoutingKey(String deadLetterRoutingKey) {
        this.deadLetterRoutingKey = deadLetterRoutingKey;
    }

    public Definition withDeadLetterRoutingKey(String deadLetterRoutingKey) {
        this.deadLetterRoutingKey = deadLetterRoutingKey;
        return this;
    }

    /**
     * 
     * @return
     *     The alternateExchange
     */
    @JsonProperty("alternate-exchange")
    public String getAlternateExchange() {
        return alternateExchange;
    }

    /**
     * 
     * @param alternateExchange
     *     The alternate-exchange
     */
    @JsonProperty("alternate-exchange")
    public void setAlternateExchange(String alternateExchange) {
        this.alternateExchange = alternateExchange;
    }

    public Definition withAlternateExchange(String alternateExchange) {
        this.alternateExchange = alternateExchange;
        return this;
    }

    /**
     * 
     * @return
     *     The haParams
     */
    @JsonProperty("ha-params")
    public Long getHaParams() {
        return haParams;
    }

    /**
     * 
     * @param haParams
     *     The ha-params
     */
    @JsonProperty("ha-params")
    public void setHaParams(Long haParams) {
        this.haParams = haParams;
    }

    public Definition withHaParams(Long haParams) {
        this.haParams = haParams;
        return this;
    }

    /**
     * 
     * @return
     *     The haSyncMode
     */
    @JsonProperty("ha-sync-mode")
    public String getHaSyncMode() {
        return haSyncMode;
    }

    /**
     * 
     * @param haSyncMode
     *     The ha-sync-mode
     */
    @JsonProperty("ha-sync-mode")
    public void setHaSyncMode(String haSyncMode) {
        this.haSyncMode = haSyncMode;
    }

    public Definition withHaSyncMode(String haSyncMode) {
        this.haSyncMode = haSyncMode;
        return this;
    }

    /**
     * 
     * @return
     *     The messageTtl
     */
    @JsonProperty("message-ttl")
    public Long getMessageTtl() {
        return messageTtl;
    }

    /**
     * 
     * @param messageTtl
     *     The message-ttl
     */
    @JsonProperty("message-ttl")
    public void setMessageTtl(Long messageTtl) {
        this.messageTtl = messageTtl;
    }

    public Definition withMessageTtl(Long messageTtl) {
        this.messageTtl = messageTtl;
        return this;
    }

    /**
     * 
     * @return
     *     The expires
     */
    @JsonProperty("expires")
    public Long getExpires() {
        return expires;
    }

    /**
     * 
     * @param expires
     *     The expires
     */
    @JsonProperty("expires")
    public void setExpires(Long expires) {
        this.expires = expires;
    }

    public Definition withExpires(Long expires) {
        this.expires = expires;
        return this;
    }

    /**
     * 
     * @return
     *     The maxLength
     */
    @JsonProperty("max-length")
    public Long getMaxLength() {
        return maxLength;
    }

    /**
     * 
     * @param maxLength
     *     The max-length
     */
    @JsonProperty("max-length")
    public void setMaxLength(Long maxLength) {
        this.maxLength = maxLength;
    }

    public Definition withMaxLength(Long maxLength) {
        this.maxLength = maxLength;
        return this;
    }

    /**
     * 
     * @return
     *     The maxLengthBytes
     */
    @JsonProperty("max-length-bytes")
    public Long getMaxLengthBytes() {
        return maxLengthBytes;
    }

    /**
     * 
     * @param maxLengthBytes
     *     The max-length-bytes
     */
    @JsonProperty("max-length-bytes")
    public void setMaxLengthBytes(Long maxLengthBytes) {
        this.maxLengthBytes = maxLengthBytes;
    }

    public Definition withMaxLengthBytes(Long maxLengthBytes) {
        this.maxLengthBytes = maxLengthBytes;
        return this;
    }

    /**
     * 
     * @return
     *     The deadLetterExchange
     */
    @JsonProperty("dead-letter-exchange")
    public String getDeadLetterExchange() {
        return deadLetterExchange;
    }

    /**
     * 
     * @param deadLetterExchange
     *     The dead-letter-exchange
     */
    @JsonProperty("dead-letter-exchange")
    public void setDeadLetterExchange(String deadLetterExchange) {
        this.deadLetterExchange = deadLetterExchange;
    }

    public Definition withDeadLetterExchange(String deadLetterExchange) {
        this.deadLetterExchange = deadLetterExchange;
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

    public Definition withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(haMode).append(deadLetterRoutingKey).append(alternateExchange).append(haParams).append(haSyncMode).append(messageTtl).append(expires).append(maxLength).append(maxLengthBytes).append(deadLetterExchange).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Definition) == false) {
            return false;
        }
        Definition rhs = ((Definition) other);
        return new EqualsBuilder().append(haMode, rhs.haMode).append(deadLetterRoutingKey, rhs.deadLetterRoutingKey).append(alternateExchange, rhs.alternateExchange).append(haParams, rhs.haParams).append(haSyncMode, rhs.haSyncMode).append(messageTtl, rhs.messageTtl).append(expires, rhs.expires).append(maxLength, rhs.maxLength).append(maxLengthBytes, rhs.maxLengthBytes).append(deadLetterExchange, rhs.deadLetterExchange).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
