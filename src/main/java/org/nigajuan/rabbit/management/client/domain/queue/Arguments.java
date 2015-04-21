
package org.nigajuan.rabbit.management.client.domain.queue;

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
    "x-message-ttl",
    "x-expires",
    "x-max-length",
    "x-max-length-bytes",
    "x-dead-letter-exchange",
    "x-dead-letter-routing-key"
})
public class Arguments {

    @JsonProperty("x-message-ttl")
    private Long xMessageTtl;
    @JsonProperty("x-expires")
    private Long xExpires;
    @JsonProperty("x-max-length")
    private Long xMaxLength;
    @JsonProperty("x-max-length-bytes")
    private Long xMaxLengthBytes;
    @JsonProperty("x-dead-letter-exchange")
    private String xDeadLetterExchange;
    @JsonProperty("x-dead-letter-routing-key")
    private String xDeadLetterRoutingKey;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The xMessageTtl
     */
    @JsonProperty("x-message-ttl")
    public Long getXMessageTtl() {
        return xMessageTtl;
    }

    /**
     * 
     * @param xMessageTtl
     *     The x-message-ttl
     */
    @JsonProperty("x-message-ttl")
    public void setXMessageTtl(Long xMessageTtl) {
        this.xMessageTtl = xMessageTtl;
    }

    public Arguments withXMessageTtl(Long xMessageTtl) {
        this.xMessageTtl = xMessageTtl;
        return this;
    }

    /**
     * 
     * @return
     *     The xExpires
     */
    @JsonProperty("x-expires")
    public Long getXExpires() {
        return xExpires;
    }

    /**
     * 
     * @param xExpires
     *     The x-expires
     */
    @JsonProperty("x-expires")
    public void setXExpires(Long xExpires) {
        this.xExpires = xExpires;
    }

    public Arguments withXExpires(Long xExpires) {
        this.xExpires = xExpires;
        return this;
    }

    /**
     * 
     * @return
     *     The xMaxLength
     */
    @JsonProperty("x-max-length")
    public Long getXMaxLength() {
        return xMaxLength;
    }

    /**
     * 
     * @param xMaxLength
     *     The x-max-length
     */
    @JsonProperty("x-max-length")
    public void setXMaxLength(Long xMaxLength) {
        this.xMaxLength = xMaxLength;
    }

    public Arguments withXMaxLength(Long xMaxLength) {
        this.xMaxLength = xMaxLength;
        return this;
    }

    /**
     * 
     * @return
     *     The xMaxLengthBytes
     */
    @JsonProperty("x-max-length-bytes")
    public Long getXMaxLengthBytes() {
        return xMaxLengthBytes;
    }

    /**
     * 
     * @param xMaxLengthBytes
     *     The x-max-length-bytes
     */
    @JsonProperty("x-max-length-bytes")
    public void setXMaxLengthBytes(Long xMaxLengthBytes) {
        this.xMaxLengthBytes = xMaxLengthBytes;
    }

    public Arguments withXMaxLengthBytes(Long xMaxLengthBytes) {
        this.xMaxLengthBytes = xMaxLengthBytes;
        return this;
    }

    /**
     * 
     * @return
     *     The xDeadLetterExchange
     */
    @JsonProperty("x-dead-letter-exchange")
    public String getXDeadLetterExchange() {
        return xDeadLetterExchange;
    }

    /**
     * 
     * @param xDeadLetterExchange
     *     The x-dead-letter-exchange
     */
    @JsonProperty("x-dead-letter-exchange")
    public void setXDeadLetterExchange(String xDeadLetterExchange) {
        this.xDeadLetterExchange = xDeadLetterExchange;
    }

    public Arguments withXDeadLetterExchange(String xDeadLetterExchange) {
        this.xDeadLetterExchange = xDeadLetterExchange;
        return this;
    }

    /**
     * 
     * @return
     *     The xDeadLetterRoutingKey
     */
    @JsonProperty("x-dead-letter-routing-key")
    public String getXDeadLetterRoutingKey() {
        return xDeadLetterRoutingKey;
    }

    /**
     * 
     * @param xDeadLetterRoutingKey
     *     The x-dead-letter-routing-key
     */
    @JsonProperty("x-dead-letter-routing-key")
    public void setXDeadLetterRoutingKey(String xDeadLetterRoutingKey) {
        this.xDeadLetterRoutingKey = xDeadLetterRoutingKey;
    }

    public Arguments withXDeadLetterRoutingKey(String xDeadLetterRoutingKey) {
        this.xDeadLetterRoutingKey = xDeadLetterRoutingKey;
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

    public Arguments withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(xMessageTtl).append(xExpires).append(xMaxLength).append(xMaxLengthBytes).append(xDeadLetterExchange).append(xDeadLetterRoutingKey).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Arguments) == false) {
            return false;
        }
        Arguments rhs = ((Arguments) other);
        return new EqualsBuilder().append(xMessageTtl, rhs.xMessageTtl).append(xExpires, rhs.xExpires).append(xMaxLength, rhs.xMaxLength).append(xMaxLengthBytes, rhs.xMaxLengthBytes).append(xDeadLetterExchange, rhs.xDeadLetterExchange).append(xDeadLetterRoutingKey, rhs.xDeadLetterRoutingKey).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
