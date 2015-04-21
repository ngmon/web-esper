
package org.nigajuan.rabbit.management.client.domain.exchange;

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
    "confirm",
    "confirm_details",
    "publish_in",
    "publish_in_details",
    "publish_out",
    "publish_out_details"
})
public class MessageStats {

    @JsonProperty("confirm")
    private Long confirm;
    @JsonProperty("confirm_details")
    private ConfirmDetails confirmDetails;
    @JsonProperty("publish_in")
    private Long publishIn;
    @JsonProperty("publish_in_details")
    private PublishInDetails publishInDetails;
    @JsonProperty("publish_out")
    private Long publishOut;
    @JsonProperty("publish_out_details")
    private PublishOutDetails publishOutDetails;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The confirm
     */
    @JsonProperty("confirm")
    public Long getConfirm() {
        return confirm;
    }

    /**
     * 
     * @param confirm
     *     The confirm
     */
    @JsonProperty("confirm")
    public void setConfirm(Long confirm) {
        this.confirm = confirm;
    }

    public MessageStats withConfirm(Long confirm) {
        this.confirm = confirm;
        return this;
    }

    /**
     * 
     * @return
     *     The confirmDetails
     */
    @JsonProperty("confirm_details")
    public ConfirmDetails getConfirmDetails() {
        return confirmDetails;
    }

    /**
     * 
     * @param confirmDetails
     *     The confirm_details
     */
    @JsonProperty("confirm_details")
    public void setConfirmDetails(ConfirmDetails confirmDetails) {
        this.confirmDetails = confirmDetails;
    }

    public MessageStats withConfirmDetails(ConfirmDetails confirmDetails) {
        this.confirmDetails = confirmDetails;
        return this;
    }

    /**
     * 
     * @return
     *     The publishIn
     */
    @JsonProperty("publish_in")
    public Long getPublishIn() {
        return publishIn;
    }

    /**
     * 
     * @param publishIn
     *     The publish_in
     */
    @JsonProperty("publish_in")
    public void setPublishIn(Long publishIn) {
        this.publishIn = publishIn;
    }

    public MessageStats withPublishIn(Long publishIn) {
        this.publishIn = publishIn;
        return this;
    }

    /**
     * 
     * @return
     *     The publishInDetails
     */
    @JsonProperty("publish_in_details")
    public PublishInDetails getPublishInDetails() {
        return publishInDetails;
    }

    /**
     * 
     * @param publishInDetails
     *     The publish_in_details
     */
    @JsonProperty("publish_in_details")
    public void setPublishInDetails(PublishInDetails publishInDetails) {
        this.publishInDetails = publishInDetails;
    }

    public MessageStats withPublishInDetails(PublishInDetails publishInDetails) {
        this.publishInDetails = publishInDetails;
        return this;
    }

    /**
     * 
     * @return
     *     The publishOut
     */
    @JsonProperty("publish_out")
    public Long getPublishOut() {
        return publishOut;
    }

    /**
     * 
     * @param publishOut
     *     The publish_out
     */
    @JsonProperty("publish_out")
    public void setPublishOut(Long publishOut) {
        this.publishOut = publishOut;
    }

    public MessageStats withPublishOut(Long publishOut) {
        this.publishOut = publishOut;
        return this;
    }

    /**
     * 
     * @return
     *     The publishOutDetails
     */
    @JsonProperty("publish_out_details")
    public PublishOutDetails getPublishOutDetails() {
        return publishOutDetails;
    }

    /**
     * 
     * @param publishOutDetails
     *     The publish_out_details
     */
    @JsonProperty("publish_out_details")
    public void setPublishOutDetails(PublishOutDetails publishOutDetails) {
        this.publishOutDetails = publishOutDetails;
    }

    public MessageStats withPublishOutDetails(PublishOutDetails publishOutDetails) {
        this.publishOutDetails = publishOutDetails;
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

    public MessageStats withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(confirm).append(confirmDetails).append(publishIn).append(publishInDetails).append(publishOut).append(publishOutDetails).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof MessageStats) == false) {
            return false;
        }
        MessageStats rhs = ((MessageStats) other);
        return new EqualsBuilder().append(confirm, rhs.confirm).append(confirmDetails, rhs.confirmDetails).append(publishIn, rhs.publishIn).append(publishInDetails, rhs.publishInDetails).append(publishOut, rhs.publishOut).append(publishOutDetails, rhs.publishOutDetails).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
