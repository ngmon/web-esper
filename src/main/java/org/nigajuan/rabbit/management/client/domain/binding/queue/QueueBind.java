
package org.nigajuan.rabbit.management.client.domain.binding.queue;

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
    "routing_key",
    "arguments"
})
public class QueueBind {

    @JsonProperty("routing_key")
    private String routingKey;
    @JsonProperty("arguments")
    private Arguments arguments;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The routingKey
     */
    @JsonProperty("routing_key")
    public String getRoutingKey() {
        return routingKey;
    }

    /**
     * 
     * @param routingKey
     *     The routing_key
     */
    @JsonProperty("routing_key")
    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public QueueBind withRoutingKey(String routingKey) {
        this.routingKey = routingKey;
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

    public QueueBind withArguments(Arguments arguments) {
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

    public QueueBind withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(routingKey).append(arguments).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof QueueBind) == false) {
            return false;
        }
        QueueBind rhs = ((QueueBind) other);
        return new EqualsBuilder().append(routingKey, rhs.routingKey).append(arguments, rhs.arguments).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
