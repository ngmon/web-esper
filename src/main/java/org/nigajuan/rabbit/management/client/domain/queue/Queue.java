
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
    "memory",
    "messages",
    "messages_details",
    "messages_ready",
    "messages_ready_details",
    "messages_unacknowledged",
    "messages_unacknowledged_details",
    "idle_since",
    "consumer_utilisation",
    "policy",
    "exclusive_consumer_tag",
    "consumers",
    "down_slave_nodes",
    "state",
    "messages_ram",
    "messages_ready_ram",
    "messages_unacknowledged_ram",
    "messages_persistent",
    "message_bytes",
    "message_bytes_ready",
    "message_bytes_unacknowledged",
    "message_bytes_ram",
    "message_bytes_persistent",
    "name",
    "vhost",
    "durable",
    "auto_delete",
    "arguments",
    "node"
})
public class Queue {

    @JsonProperty("memory")
    private Long memory;
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
    @JsonProperty("idle_since")
    private String idleSince;
    @JsonProperty("consumer_utilisation")
    private String consumerUtilisation;
    @JsonProperty("policy")
    private String policy;
    @JsonProperty("exclusive_consumer_tag")
    private String exclusiveConsumerTag;
    @JsonProperty("consumers")
    private Long consumers;
    @JsonProperty("down_slave_nodes")
    private String downSlaveNodes;
    @JsonProperty("state")
    private String state;
    @JsonProperty("messages_ram")
    private Long messagesRam;
    @JsonProperty("messages_ready_ram")
    private Long messagesReadyRam;
    @JsonProperty("messages_unacknowledged_ram")
    private Long messagesUnacknowledgedRam;
    @JsonProperty("messages_persistent")
    private Long messagesPersistent;
    @JsonProperty("message_bytes")
    private Long messageBytes;
    @JsonProperty("message_bytes_ready")
    private Long messageBytesReady;
    @JsonProperty("message_bytes_unacknowledged")
    private Long messageBytesUnacknowledged;
    @JsonProperty("message_bytes_ram")
    private Long messageBytesRam;
    @JsonProperty("message_bytes_persistent")
    private Long messageBytesPersistent;
    @JsonProperty("name")
    private String name;
    @JsonProperty("vhost")
    private String vhost;
    @JsonProperty("durable")
    private Boolean durable;
    @JsonProperty("auto_delete")
    private Boolean autoDelete;
    @JsonProperty("arguments")
    private Arguments arguments;
    @JsonProperty("node")
    private String node;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The memory
     */
    @JsonProperty("memory")
    public Long getMemory() {
        return memory;
    }

    /**
     * 
     * @param memory
     *     The memory
     */
    @JsonProperty("memory")
    public void setMemory(Long memory) {
        this.memory = memory;
    }

    public Queue withMemory(Long memory) {
        this.memory = memory;
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

    public Queue withMessages(Long messages) {
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

    public Queue withMessagesDetails(MessagesDetails messagesDetails) {
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

    public Queue withMessagesReady(Long messagesReady) {
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

    public Queue withMessagesReadyDetails(MessagesReadyDetails messagesReadyDetails) {
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

    public Queue withMessagesUnacknowledged(Long messagesUnacknowledged) {
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

    public Queue withMessagesUnacknowledgedDetails(MessagesUnacknowledgedDetails messagesUnacknowledgedDetails) {
        this.messagesUnacknowledgedDetails = messagesUnacknowledgedDetails;
        return this;
    }

    /**
     * 
     * @return
     *     The idleSince
     */
    @JsonProperty("idle_since")
    public String getIdleSince() {
        return idleSince;
    }

    /**
     * 
     * @param idleSince
     *     The idle_since
     */
    @JsonProperty("idle_since")
    public void setIdleSince(String idleSince) {
        this.idleSince = idleSince;
    }

    public Queue withIdleSince(String idleSince) {
        this.idleSince = idleSince;
        return this;
    }

    /**
     * 
     * @return
     *     The consumerUtilisation
     */
    @JsonProperty("consumer_utilisation")
    public String getConsumerUtilisation() {
        return consumerUtilisation;
    }

    /**
     * 
     * @param consumerUtilisation
     *     The consumer_utilisation
     */
    @JsonProperty("consumer_utilisation")
    public void setConsumerUtilisation(String consumerUtilisation) {
        this.consumerUtilisation = consumerUtilisation;
    }

    public Queue withConsumerUtilisation(String consumerUtilisation) {
        this.consumerUtilisation = consumerUtilisation;
        return this;
    }

    /**
     * 
     * @return
     *     The policy
     */
    @JsonProperty("policy")
    public String getPolicy() {
        return policy;
    }

    /**
     * 
     * @param policy
     *     The policy
     */
    @JsonProperty("policy")
    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public Queue withPolicy(String policy) {
        this.policy = policy;
        return this;
    }

    /**
     * 
     * @return
     *     The exclusiveConsumerTag
     */
    @JsonProperty("exclusive_consumer_tag")
    public String getExclusiveConsumerTag() {
        return exclusiveConsumerTag;
    }

    /**
     * 
     * @param exclusiveConsumerTag
     *     The exclusive_consumer_tag
     */
    @JsonProperty("exclusive_consumer_tag")
    public void setExclusiveConsumerTag(String exclusiveConsumerTag) {
        this.exclusiveConsumerTag = exclusiveConsumerTag;
    }

    public Queue withExclusiveConsumerTag(String exclusiveConsumerTag) {
        this.exclusiveConsumerTag = exclusiveConsumerTag;
        return this;
    }

    /**
     * 
     * @return
     *     The consumers
     */
    @JsonProperty("consumers")
    public Long getConsumers() {
        return consumers;
    }

    /**
     * 
     * @param consumers
     *     The consumers
     */
    @JsonProperty("consumers")
    public void setConsumers(Long consumers) {
        this.consumers = consumers;
    }

    public Queue withConsumers(Long consumers) {
        this.consumers = consumers;
        return this;
    }

    /**
     * 
     * @return
     *     The downSlaveNodes
     */
    @JsonProperty("down_slave_nodes")
    public String getDownSlaveNodes() {
        return downSlaveNodes;
    }

    /**
     * 
     * @param downSlaveNodes
     *     The down_slave_nodes
     */
    @JsonProperty("down_slave_nodes")
    public void setDownSlaveNodes(String downSlaveNodes) {
        this.downSlaveNodes = downSlaveNodes;
    }

    public Queue withDownSlaveNodes(String downSlaveNodes) {
        this.downSlaveNodes = downSlaveNodes;
        return this;
    }

    /**
     * 
     * @return
     *     The state
     */
    @JsonProperty("state")
    public String getState() {
        return state;
    }

    /**
     * 
     * @param state
     *     The state
     */
    @JsonProperty("state")
    public void setState(String state) {
        this.state = state;
    }

    public Queue withState(String state) {
        this.state = state;
        return this;
    }

    /**
     * 
     * @return
     *     The messagesRam
     */
    @JsonProperty("messages_ram")
    public Long getMessagesRam() {
        return messagesRam;
    }

    /**
     * 
     * @param messagesRam
     *     The messages_ram
     */
    @JsonProperty("messages_ram")
    public void setMessagesRam(Long messagesRam) {
        this.messagesRam = messagesRam;
    }

    public Queue withMessagesRam(Long messagesRam) {
        this.messagesRam = messagesRam;
        return this;
    }

    /**
     * 
     * @return
     *     The messagesReadyRam
     */
    @JsonProperty("messages_ready_ram")
    public Long getMessagesReadyRam() {
        return messagesReadyRam;
    }

    /**
     * 
     * @param messagesReadyRam
     *     The messages_ready_ram
     */
    @JsonProperty("messages_ready_ram")
    public void setMessagesReadyRam(Long messagesReadyRam) {
        this.messagesReadyRam = messagesReadyRam;
    }

    public Queue withMessagesReadyRam(Long messagesReadyRam) {
        this.messagesReadyRam = messagesReadyRam;
        return this;
    }

    /**
     * 
     * @return
     *     The messagesUnacknowledgedRam
     */
    @JsonProperty("messages_unacknowledged_ram")
    public Long getMessagesUnacknowledgedRam() {
        return messagesUnacknowledgedRam;
    }

    /**
     * 
     * @param messagesUnacknowledgedRam
     *     The messages_unacknowledged_ram
     */
    @JsonProperty("messages_unacknowledged_ram")
    public void setMessagesUnacknowledgedRam(Long messagesUnacknowledgedRam) {
        this.messagesUnacknowledgedRam = messagesUnacknowledgedRam;
    }

    public Queue withMessagesUnacknowledgedRam(Long messagesUnacknowledgedRam) {
        this.messagesUnacknowledgedRam = messagesUnacknowledgedRam;
        return this;
    }

    /**
     * 
     * @return
     *     The messagesPersistent
     */
    @JsonProperty("messages_persistent")
    public Long getMessagesPersistent() {
        return messagesPersistent;
    }

    /**
     * 
     * @param messagesPersistent
     *     The messages_persistent
     */
    @JsonProperty("messages_persistent")
    public void setMessagesPersistent(Long messagesPersistent) {
        this.messagesPersistent = messagesPersistent;
    }

    public Queue withMessagesPersistent(Long messagesPersistent) {
        this.messagesPersistent = messagesPersistent;
        return this;
    }

    /**
     * 
     * @return
     *     The messageBytes
     */
    @JsonProperty("message_bytes")
    public Long getMessageBytes() {
        return messageBytes;
    }

    /**
     * 
     * @param messageBytes
     *     The message_bytes
     */
    @JsonProperty("message_bytes")
    public void setMessageBytes(Long messageBytes) {
        this.messageBytes = messageBytes;
    }

    public Queue withMessageBytes(Long messageBytes) {
        this.messageBytes = messageBytes;
        return this;
    }

    /**
     * 
     * @return
     *     The messageBytesReady
     */
    @JsonProperty("message_bytes_ready")
    public Long getMessageBytesReady() {
        return messageBytesReady;
    }

    /**
     * 
     * @param messageBytesReady
     *     The message_bytes_ready
     */
    @JsonProperty("message_bytes_ready")
    public void setMessageBytesReady(Long messageBytesReady) {
        this.messageBytesReady = messageBytesReady;
    }

    public Queue withMessageBytesReady(Long messageBytesReady) {
        this.messageBytesReady = messageBytesReady;
        return this;
    }

    /**
     * 
     * @return
     *     The messageBytesUnacknowledged
     */
    @JsonProperty("message_bytes_unacknowledged")
    public Long getMessageBytesUnacknowledged() {
        return messageBytesUnacknowledged;
    }

    /**
     * 
     * @param messageBytesUnacknowledged
     *     The message_bytes_unacknowledged
     */
    @JsonProperty("message_bytes_unacknowledged")
    public void setMessageBytesUnacknowledged(Long messageBytesUnacknowledged) {
        this.messageBytesUnacknowledged = messageBytesUnacknowledged;
    }

    public Queue withMessageBytesUnacknowledged(Long messageBytesUnacknowledged) {
        this.messageBytesUnacknowledged = messageBytesUnacknowledged;
        return this;
    }

    /**
     * 
     * @return
     *     The messageBytesRam
     */
    @JsonProperty("message_bytes_ram")
    public Long getMessageBytesRam() {
        return messageBytesRam;
    }

    /**
     * 
     * @param messageBytesRam
     *     The message_bytes_ram
     */
    @JsonProperty("message_bytes_ram")
    public void setMessageBytesRam(Long messageBytesRam) {
        this.messageBytesRam = messageBytesRam;
    }

    public Queue withMessageBytesRam(Long messageBytesRam) {
        this.messageBytesRam = messageBytesRam;
        return this;
    }

    /**
     * 
     * @return
     *     The messageBytesPersistent
     */
    @JsonProperty("message_bytes_persistent")
    public Long getMessageBytesPersistent() {
        return messageBytesPersistent;
    }

    /**
     * 
     * @param messageBytesPersistent
     *     The message_bytes_persistent
     */
    @JsonProperty("message_bytes_persistent")
    public void setMessageBytesPersistent(Long messageBytesPersistent) {
        this.messageBytesPersistent = messageBytesPersistent;
    }

    public Queue withMessageBytesPersistent(Long messageBytesPersistent) {
        this.messageBytesPersistent = messageBytesPersistent;
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

    public Queue withName(String name) {
        this.name = name;
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

    public Queue withVhost(String vhost) {
        this.vhost = vhost;
        return this;
    }

    /**
     * 
     * @return
     *     The durable
     */
    @JsonProperty("durable")
    public Boolean getDurable() {
        return durable;
    }

    /**
     * 
     * @param durable
     *     The durable
     */
    @JsonProperty("durable")
    public void setDurable(Boolean durable) {
        this.durable = durable;
    }

    public Queue withDurable(Boolean durable) {
        this.durable = durable;
        return this;
    }

    /**
     * 
     * @return
     *     The autoDelete
     */
    @JsonProperty("auto_delete")
    public Boolean getAutoDelete() {
        return autoDelete;
    }

    /**
     * 
     * @param autoDelete
     *     The auto_delete
     */
    @JsonProperty("auto_delete")
    public void setAutoDelete(Boolean autoDelete) {
        this.autoDelete = autoDelete;
    }

    public Queue withAutoDelete(Boolean autoDelete) {
        this.autoDelete = autoDelete;
        return this;
    }

    /**
     * 
     * @return
     *     The arguments
     */
    @JsonProperty("arguments")
    public Arguments getArguments() {
        return arguments;
    }

    /**
     * 
     * @param arguments
     *     The arguments
     */
    @JsonProperty("arguments")
    public void setArguments(Arguments arguments) {
        this.arguments = arguments;
    }

    public Queue withArguments(Arguments arguments) {
        this.arguments = arguments;
        return this;
    }

    /**
     * 
     * @return
     *     The node
     */
    @JsonProperty("node")
    public String getNode() {
        return node;
    }

    /**
     * 
     * @param node
     *     The node
     */
    @JsonProperty("node")
    public void setNode(String node) {
        this.node = node;
    }

    public Queue withNode(String node) {
        this.node = node;
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

    public Queue withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(memory).append(messages).append(messagesDetails).append(messagesReady).append(messagesReadyDetails).append(messagesUnacknowledged).append(messagesUnacknowledgedDetails).append(idleSince).append(consumerUtilisation).append(policy).append(exclusiveConsumerTag).append(consumers).append(downSlaveNodes).append(state).append(messagesRam).append(messagesReadyRam).append(messagesUnacknowledgedRam).append(messagesPersistent).append(messageBytes).append(messageBytesReady).append(messageBytesUnacknowledged).append(messageBytesRam).append(messageBytesPersistent).append(name).append(vhost).append(durable).append(autoDelete).append(arguments).append(node).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Queue) == false) {
            return false;
        }
        Queue rhs = ((Queue) other);
        return new EqualsBuilder().append(memory, rhs.memory).append(messages, rhs.messages).append(messagesDetails, rhs.messagesDetails).append(messagesReady, rhs.messagesReady).append(messagesReadyDetails, rhs.messagesReadyDetails).append(messagesUnacknowledged, rhs.messagesUnacknowledged).append(messagesUnacknowledgedDetails, rhs.messagesUnacknowledgedDetails).append(idleSince, rhs.idleSince).append(consumerUtilisation, rhs.consumerUtilisation).append(policy, rhs.policy).append(exclusiveConsumerTag, rhs.exclusiveConsumerTag).append(consumers, rhs.consumers).append(downSlaveNodes, rhs.downSlaveNodes).append(state, rhs.state).append(messagesRam, rhs.messagesRam).append(messagesReadyRam, rhs.messagesReadyRam).append(messagesUnacknowledgedRam, rhs.messagesUnacknowledgedRam).append(messagesPersistent, rhs.messagesPersistent).append(messageBytes, rhs.messageBytes).append(messageBytesReady, rhs.messageBytesReady).append(messageBytesUnacknowledged, rhs.messageBytesUnacknowledged).append(messageBytesRam, rhs.messageBytesRam).append(messageBytesPersistent, rhs.messageBytesPersistent).append(name, rhs.name).append(vhost, rhs.vhost).append(durable, rhs.durable).append(autoDelete, rhs.autoDelete).append(arguments, rhs.arguments).append(node, rhs.node).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
