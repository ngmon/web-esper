
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
    "message_stats",
    "messages",
    "messages_details",
    "messages_ready",
    "messages_ready_details",
    "messages_unacknowledged",
    "messages_unacknowledged_details",
    "recv_oct",
    "recv_oct_details",
    "send_oct",
    "send_oct_details",
    "name",
    "tracing"
})
public class Vhost {

    @JsonProperty("message_stats")
    private MessageStats messageStats;
    @JsonProperty("messages")
    private Long messages;
    @JsonProperty("messages_details")
    private MessagesDetails messagesDetails;
    @JsonProperty("messages_ready")
    private Long messagesReady;
    @JsonProperty("messages_ready_details")
    private MessagesReadyDetails messagesReadyDetails;
    @JsonProperty("messages_unacknowledged")
    private Long messagesUnacknowledged;
    @JsonProperty("messages_unacknowledged_details")
    private MessagesUnacknowledgedDetails messagesUnacknowledgedDetails;
    @JsonProperty("recv_oct")
    private Long recvOct;
    @JsonProperty("recv_oct_details")
    private RecvOctDetails recvOctDetails;
    @JsonProperty("send_oct")
    private Long sendOct;
    @JsonProperty("send_oct_details")
    private SendOctDetails sendOctDetails;
    @JsonProperty("name")
    private String name;
    @JsonProperty("tracing")
    private Boolean tracing;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The messageStats
     */
    @JsonProperty("message_stats")
    public MessageStats getMessageStats() {
        return messageStats;
    }

    /**
     * 
     * @param messageStats
     *     The message_stats
     */
    @JsonProperty("message_stats")
    public void setMessageStats(MessageStats messageStats) {
        this.messageStats = messageStats;
    }

    public Vhost withMessageStats(MessageStats messageStats) {
        this.messageStats = messageStats;
        return this;
    }

    /**
     * 
     * @return
     *     The messages
     */
    @JsonProperty("messages")
    public Long getMessages() {
        return messages;
    }

    /**
     * 
     * @param messages
     *     The messages
     */
    @JsonProperty("messages")
    public void setMessages(Long messages) {
        this.messages = messages;
    }

    public Vhost withMessages(Long messages) {
        this.messages = messages;
        return this;
    }

    /**
     * 
     * @return
     *     The messagesDetails
     */
    @JsonProperty("messages_details")
    public MessagesDetails getMessagesDetails() {
        return messagesDetails;
    }

    /**
     * 
     * @param messagesDetails
     *     The messages_details
     */
    @JsonProperty("messages_details")
    public void setMessagesDetails(MessagesDetails messagesDetails) {
        this.messagesDetails = messagesDetails;
    }

    public Vhost withMessagesDetails(MessagesDetails messagesDetails) {
        this.messagesDetails = messagesDetails;
        return this;
    }

    /**
     * 
     * @return
     *     The messagesReady
     */
    @JsonProperty("messages_ready")
    public Long getMessagesReady() {
        return messagesReady;
    }

    /**
     * 
     * @param messagesReady
     *     The messages_ready
     */
    @JsonProperty("messages_ready")
    public void setMessagesReady(Long messagesReady) {
        this.messagesReady = messagesReady;
    }

    public Vhost withMessagesReady(Long messagesReady) {
        this.messagesReady = messagesReady;
        return this;
    }

    /**
     * 
     * @return
     *     The messagesReadyDetails
     */
    @JsonProperty("messages_ready_details")
    public MessagesReadyDetails getMessagesReadyDetails() {
        return messagesReadyDetails;
    }

    /**
     * 
     * @param messagesReadyDetails
     *     The messages_ready_details
     */
    @JsonProperty("messages_ready_details")
    public void setMessagesReadyDetails(MessagesReadyDetails messagesReadyDetails) {
        this.messagesReadyDetails = messagesReadyDetails;
    }

    public Vhost withMessagesReadyDetails(MessagesReadyDetails messagesReadyDetails) {
        this.messagesReadyDetails = messagesReadyDetails;
        return this;
    }

    /**
     * 
     * @return
     *     The messagesUnacknowledged
     */
    @JsonProperty("messages_unacknowledged")
    public Long getMessagesUnacknowledged() {
        return messagesUnacknowledged;
    }

    /**
     * 
     * @param messagesUnacknowledged
     *     The messages_unacknowledged
     */
    @JsonProperty("messages_unacknowledged")
    public void setMessagesUnacknowledged(Long messagesUnacknowledged) {
        this.messagesUnacknowledged = messagesUnacknowledged;
    }

    public Vhost withMessagesUnacknowledged(Long messagesUnacknowledged) {
        this.messagesUnacknowledged = messagesUnacknowledged;
        return this;
    }

    /**
     * 
     * @return
     *     The messagesUnacknowledgedDetails
     */
    @JsonProperty("messages_unacknowledged_details")
    public MessagesUnacknowledgedDetails getMessagesUnacknowledgedDetails() {
        return messagesUnacknowledgedDetails;
    }

    /**
     * 
     * @param messagesUnacknowledgedDetails
     *     The messages_unacknowledged_details
     */
    @JsonProperty("messages_unacknowledged_details")
    public void setMessagesUnacknowledgedDetails(MessagesUnacknowledgedDetails messagesUnacknowledgedDetails) {
        this.messagesUnacknowledgedDetails = messagesUnacknowledgedDetails;
    }

    public Vhost withMessagesUnacknowledgedDetails(MessagesUnacknowledgedDetails messagesUnacknowledgedDetails) {
        this.messagesUnacknowledgedDetails = messagesUnacknowledgedDetails;
        return this;
    }

    /**
     * 
     * @return
     *     The recvOct
     */
    @JsonProperty("recv_oct")
    public Long getRecvOct() {
        return recvOct;
    }

    /**
     * 
     * @param recvOct
     *     The recv_oct
     */
    @JsonProperty("recv_oct")
    public void setRecvOct(Long recvOct) {
        this.recvOct = recvOct;
    }

    public Vhost withRecvOct(Long recvOct) {
        this.recvOct = recvOct;
        return this;
    }

    /**
     * 
     * @return
     *     The recvOctDetails
     */
    @JsonProperty("recv_oct_details")
    public RecvOctDetails getRecvOctDetails() {
        return recvOctDetails;
    }

    /**
     * 
     * @param recvOctDetails
     *     The recv_oct_details
     */
    @JsonProperty("recv_oct_details")
    public void setRecvOctDetails(RecvOctDetails recvOctDetails) {
        this.recvOctDetails = recvOctDetails;
    }

    public Vhost withRecvOctDetails(RecvOctDetails recvOctDetails) {
        this.recvOctDetails = recvOctDetails;
        return this;
    }

    /**
     * 
     * @return
     *     The sendOct
     */
    @JsonProperty("send_oct")
    public Long getSendOct() {
        return sendOct;
    }

    /**
     * 
     * @param sendOct
     *     The send_oct
     */
    @JsonProperty("send_oct")
    public void setSendOct(Long sendOct) {
        this.sendOct = sendOct;
    }

    public Vhost withSendOct(Long sendOct) {
        this.sendOct = sendOct;
        return this;
    }

    /**
     * 
     * @return
     *     The sendOctDetails
     */
    @JsonProperty("send_oct_details")
    public SendOctDetails getSendOctDetails() {
        return sendOctDetails;
    }

    /**
     * 
     * @param sendOctDetails
     *     The send_oct_details
     */
    @JsonProperty("send_oct_details")
    public void setSendOctDetails(SendOctDetails sendOctDetails) {
        this.sendOctDetails = sendOctDetails;
    }

    public Vhost withSendOctDetails(SendOctDetails sendOctDetails) {
        this.sendOctDetails = sendOctDetails;
        return this;
    }

    /**
     * 
     * @return
     *     The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public Vhost withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * 
     * @return
     *     The tracing
     */
    @JsonProperty("tracing")
    public Boolean getTracing() {
        return tracing;
    }

    /**
     * 
     * @param tracing
     *     The tracing
     */
    @JsonProperty("tracing")
    public void setTracing(Boolean tracing) {
        this.tracing = tracing;
    }

    public Vhost withTracing(Boolean tracing) {
        this.tracing = tracing;
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

    public Vhost withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(messageStats).append(messages).append(messagesDetails).append(messagesReady).append(messagesReadyDetails).append(messagesUnacknowledged).append(messagesUnacknowledgedDetails).append(recvOct).append(recvOctDetails).append(sendOct).append(sendOctDetails).append(name).append(tracing).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Vhost) == false) {
            return false;
        }
        Vhost rhs = ((Vhost) other);
        return new EqualsBuilder().append(messageStats, rhs.messageStats).append(messages, rhs.messages).append(messagesDetails, rhs.messagesDetails).append(messagesReady, rhs.messagesReady).append(messagesReadyDetails, rhs.messagesReadyDetails).append(messagesUnacknowledged, rhs.messagesUnacknowledged).append(messagesUnacknowledgedDetails, rhs.messagesUnacknowledgedDetails).append(recvOct, rhs.recvOct).append(recvOctDetails, rhs.recvOctDetails).append(sendOct, rhs.sendOct).append(sendOctDetails, rhs.sendOctDetails).append(name, rhs.name).append(tracing, rhs.tracing).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
