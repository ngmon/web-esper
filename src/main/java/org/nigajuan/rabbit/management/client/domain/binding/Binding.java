
package org.nigajuan.rabbit.management.client.domain.binding;

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
    "source",
    "vhost",
    "destination",
    "destination_type",
    "routing_key",
    "arguments",
    "properties_key"
})
public class Binding {

    @JsonProperty("source")
    private String source;
    @JsonProperty("vhost")
    private String vhost;
    @JsonProperty("destination")
    private String destination;
    @JsonProperty("destination_type")
    private String destinationType;
    @JsonProperty("routing_key")
    private String routingKey;
    @JsonProperty("arguments")
    private Arguments arguments;
    @JsonProperty("properties_key")
    private String propertiesKey;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The source
     */
    @JsonProperty("source")
    public String getSource() {
        return source;
    }

    /**
     * 
     * @param source
     *     The source
     */
    @JsonProperty("source")
    public void setSource(String source) {
        this.source = source;
    }

    public Binding withSource(String source) {
        this.source = source;
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

    public Binding withVhost(String vhost) {
        this.vhost = vhost;
        return this;
    }

    /**
     * 
     * @return
     *     The destination
     */
    @JsonProperty("destination")
    public String getDestination() {
        return destination;
    }

    /**
     * 
     * @param destination
     *     The destination
     */
    @JsonProperty("destination")
    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Binding withDestination(String destination) {
        this.destination = destination;
        return this;
    }

    /**
     * 
     * @return
     *     The destinationType
     */
    @JsonProperty("destination_type")
    public String getDestinationType() {
        return destinationType;
    }

    /**
     * 
     * @param destinationType
     *     The destination_type
     */
    @JsonProperty("destination_type")
    public void setDestinationType(String destinationType) {
        this.destinationType = destinationType;
    }

    public Binding withDestinationType(String destinationType) {
        this.destinationType = destinationType;
        return this;
    }

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

    public Binding withRoutingKey(String routingKey) {
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

    public Binding withArguments(Arguments arguments) {
        this.arguments = arguments;
        return this;
    }

    /**
     * 
     * @return
     *     The propertiesKey
     */
    @JsonProperty("properties_key")
    public String getPropertiesKey() {
        return propertiesKey;
    }

    /**
     * 
     * @param propertiesKey
     *     The properties_key
     */
    @JsonProperty("properties_key")
    public void setPropertiesKey(String propertiesKey) {
        this.propertiesKey = propertiesKey;
    }

    public Binding withPropertiesKey(String propertiesKey) {
        this.propertiesKey = propertiesKey;
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

    public Binding withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(source).append(vhost).append(destination).append(destinationType).append(routingKey).append(arguments).append(propertiesKey).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Binding) == false) {
            return false;
        }
        Binding rhs = ((Binding) other);
        return new EqualsBuilder().append(source, rhs.source).append(vhost, rhs.vhost).append(destination, rhs.destination).append(destinationType, rhs.destinationType).append(routingKey, rhs.routingKey).append(arguments, rhs.arguments).append(propertiesKey, rhs.propertiesKey).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
