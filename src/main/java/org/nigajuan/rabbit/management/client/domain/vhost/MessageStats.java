
package org.nigajuan.rabbit.management.client.domain.vhost;

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
    "ack",
    "ack_details",
    "confirm",
    "confirm_details",
    "deliver",
    "deliver_details",
    "deliver_get",
    "deliver_get_details",
    "publish",
    "publish_details",
    "redeliver",
    "redeliver_details"
})
public class MessageStats {

    @JsonProperty("ack")
    private Long ack;
    @JsonProperty("ack_details")
    private AckDetails ackDetails;
    @JsonProperty("confirm")
    private Long confirm;
    @JsonProperty("confirm_details")
    private ConfirmDetails confirmDetails;
    @JsonProperty("deliver")
    private Long deliver;
    @JsonProperty("deliver_details")
    private DeliverDetails deliverDetails;
    @JsonProperty("deliver_get")
    private Long deliverGet;
    @JsonProperty("deliver_get_details")
    private DeliverGetDetails deliverGetDetails;
    @JsonProperty("publish")
    private Long publish;
    @JsonProperty("publish_details")
    private PublishDetails publishDetails;
    @JsonProperty("redeliver")
    private Long redeliver;
    @JsonProperty("redeliver_details")
    private RedeliverDetails redeliverDetails;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The ack
     */
    @JsonProperty("ack")
    public Long getAck() {
        return ack;
    }

    /**
     * 
     * @param ack
     *     The ack
     */
    @JsonProperty("ack")
    public void setAck(Long ack) {
        this.ack = ack;
    }

    public MessageStats withAck(Long ack) {
        this.ack = ack;
        return this;
    }

    /**
     * 
     * @return
     *     The ackDetails
     */
    @JsonProperty("ack_details")
    public AckDetails getAckDetails() {
        return ackDetails;
    }

    /**
     * 
     * @param ackDetails
     *     The ack_details
     */
    @JsonProperty("ack_details")
    public void setAckDetails(AckDetails ackDetails) {
        this.ackDetails = ackDetails;
    }

    public MessageStats withAckDetails(AckDetails ackDetails) {
        this.ackDetails = ackDetails;
        return this;
    }

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
     *     The deliver
     */
    @JsonProperty("deliver")
    public Long getDeliver() {
        return deliver;
    }

    /**
     * 
     * @param deliver
     *     The deliver
     */
    @JsonProperty("deliver")
    public void setDeliver(Long deliver) {
        this.deliver = deliver;
    }

    public MessageStats withDeliver(Long deliver) {
        this.deliver = deliver;
        return this;
    }

    /**
     * 
     * @return
     *     The deliverDetails
     */
    @JsonProperty("deliver_details")
    public DeliverDetails getDeliverDetails() {
        return deliverDetails;
    }

    /**
     * 
     * @param deliverDetails
     *     The deliver_details
     */
    @JsonProperty("deliver_details")
    public void setDeliverDetails(DeliverDetails deliverDetails) {
        this.deliverDetails = deliverDetails;
    }

    public MessageStats withDeliverDetails(DeliverDetails deliverDetails) {
        this.deliverDetails = deliverDetails;
        return this;
    }

    /**
     * 
     * @return
     *     The deliverGet
     */
    @JsonProperty("deliver_get")
    public Long getDeliverGet() {
        return deliverGet;
    }

    /**
     * 
     * @param deliverGet
     *     The deliver_get
     */
    @JsonProperty("deliver_get")
    public void setDeliverGet(Long deliverGet) {
        this.deliverGet = deliverGet;
    }

    public MessageStats withDeliverGet(Long deliverGet) {
        this.deliverGet = deliverGet;
        return this;
    }

    /**
     * 
     * @return
     *     The deliverGetDetails
     */
    @JsonProperty("deliver_get_details")
    public DeliverGetDetails getDeliverGetDetails() {
        return deliverGetDetails;
    }

    /**
     * 
     * @param deliverGetDetails
     *     The deliver_get_details
     */
    @JsonProperty("deliver_get_details")
    public void setDeliverGetDetails(DeliverGetDetails deliverGetDetails) {
        this.deliverGetDetails = deliverGetDetails;
    }

    public MessageStats withDeliverGetDetails(DeliverGetDetails deliverGetDetails) {
        this.deliverGetDetails = deliverGetDetails;
        return this;
    }

    /**
     * 
     * @return
     *     The publish
     */
    @JsonProperty("publish")
    public Long getPublish() {
        return publish;
    }

    /**
     * 
     * @param publish
     *     The publish
     */
    @JsonProperty("publish")
    public void setPublish(Long publish) {
        this.publish = publish;
    }

    public MessageStats withPublish(Long publish) {
        this.publish = publish;
        return this;
    }

    /**
     * 
     * @return
     *     The publishDetails
     */
    @JsonProperty("publish_details")
    public PublishDetails getPublishDetails() {
        return publishDetails;
    }

    /**
     * 
     * @param publishDetails
     *     The publish_details
     */
    @JsonProperty("publish_details")
    public void setPublishDetails(PublishDetails publishDetails) {
        this.publishDetails = publishDetails;
    }

    public MessageStats withPublishDetails(PublishDetails publishDetails) {
        this.publishDetails = publishDetails;
        return this;
    }

    /**
     * 
     * @return
     *     The redeliver
     */
    @JsonProperty("redeliver")
    public Long getRedeliver() {
        return redeliver;
    }

    /**
     * 
     * @param redeliver
     *     The redeliver
     */
    @JsonProperty("redeliver")
    public void setRedeliver(Long redeliver) {
        this.redeliver = redeliver;
    }

    public MessageStats withRedeliver(Long redeliver) {
        this.redeliver = redeliver;
        return this;
    }

    /**
     * 
     * @return
     *     The redeliverDetails
     */
    @JsonProperty("redeliver_details")
    public RedeliverDetails getRedeliverDetails() {
        return redeliverDetails;
    }

    /**
     * 
     * @param redeliverDetails
     *     The redeliver_details
     */
    @JsonProperty("redeliver_details")
    public void setRedeliverDetails(RedeliverDetails redeliverDetails) {
        this.redeliverDetails = redeliverDetails;
    }

    public MessageStats withRedeliverDetails(RedeliverDetails redeliverDetails) {
        this.redeliverDetails = redeliverDetails;
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
        return new HashCodeBuilder().append(ack).append(ackDetails).append(confirm).append(confirmDetails).append(deliver).append(deliverDetails).append(deliverGet).append(deliverGetDetails).append(publish).append(publishDetails).append(redeliver).append(redeliverDetails).append(additionalProperties).toHashCode();
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
        return new EqualsBuilder().append(ack, rhs.ack).append(ackDetails, rhs.ackDetails).append(confirm, rhs.confirm).append(confirmDetails, rhs.confirmDetails).append(deliver, rhs.deliver).append(deliverDetails, rhs.deliverDetails).append(deliverGet, rhs.deliverGet).append(deliverGetDetails, rhs.deliverGetDetails).append(publish, rhs.publish).append(publishDetails, rhs.publishDetails).append(redeliver, rhs.redeliver).append(redeliverDetails, rhs.redeliverDetails).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
