
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
    "message_stats",
    "name",
    "vhost",
    "type",
    "durable",
    "auto_delete",
    "internal",
    "arguments"
})
public class Exchange {

    @JsonProperty("message_stats")
    private MessageStats messageStats;
    @JsonProperty("name")
    private String name;
    @JsonProperty("vhost")
    private String vhost;
    @JsonProperty("type")
    private String type;
    @JsonProperty("durable")
    private Boolean durable;
    @JsonProperty("auto_delete")
    private Boolean autoDelete;
    @JsonProperty("internal")
    private Boolean internal;
    @JsonProperty("arguments")
    private Arguments arguments;
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

    public Exchange withMessageStats(MessageStats messageStats) {
        this.messageStats = messageStats;
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

    public Exchange withName(String name) {
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

    public Exchange withVhost(String vhost) {
        this.vhost = vhost;
        return this;
    }

    /**
     * 
     * @return
     *     The type
     */
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    public Exchange withType(String type) {
        this.type = type;
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

    public Exchange withDurable(Boolean durable) {
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

    public Exchange withAutoDelete(Boolean autoDelete) {
        this.autoDelete = autoDelete;
        return this;
    }

    /**
     * 
     * @return
     *     The internal
     */
    @JsonProperty("internal")
    public Boolean getInternal() {
        return internal;
    }

    /**
     * 
     * @param internal
     *     The internal
     */
    @JsonProperty("internal")
    public void setInternal(Boolean internal) {
        this.internal = internal;
    }

    public Exchange withInternal(Boolean internal) {
        this.internal = internal;
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

    public Exchange withArguments(Arguments arguments) {
        this.arguments = arguments;
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

    public Exchange withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(messageStats).append(name).append(vhost).append(type).append(durable).append(autoDelete).append(internal).append(arguments).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Exchange) == false) {
            return false;
        }
        Exchange rhs = ((Exchange) other);
        return new EqualsBuilder().append(messageStats, rhs.messageStats).append(name, rhs.name).append(vhost, rhs.vhost).append(type, rhs.type).append(durable, rhs.durable).append(autoDelete, rhs.autoDelete).append(internal, rhs.internal).append(arguments, rhs.arguments).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
