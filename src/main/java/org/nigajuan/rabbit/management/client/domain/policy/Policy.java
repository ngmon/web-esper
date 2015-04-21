
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
    "vhost",
    "name",
    "pattern",
    "apply-to",
    "definition",
    "priority"
})
public class Policy {

    @JsonProperty("vhost")
    private String vhost;
    @JsonProperty("name")
    private String name;
    @JsonProperty("pattern")
    private String pattern;
    @JsonProperty("apply-to")
    private String applyTo;
    @JsonProperty("definition")
    private Definition definition;
    @JsonProperty("priority")
    private Long priority;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

    public Policy withVhost(String vhost) {
        this.vhost = vhost;
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

    public Policy withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * 
     * @return
     *     The pattern
     */
    @JsonProperty("pattern")
    public String getPattern() {
        return pattern;
    }

    /**
     * 
     * @param pattern
     *     The pattern
     */
    @JsonProperty("pattern")
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Policy withPattern(String pattern) {
        this.pattern = pattern;
        return this;
    }

    /**
     * 
     * @return
     *     The applyTo
     */
    @JsonProperty("apply-to")
    public String getApplyTo() {
        return applyTo;
    }

    /**
     * 
     * @param applyTo
     *     The apply-to
     */
    @JsonProperty("apply-to")
    public void setApplyTo(String applyTo) {
        this.applyTo = applyTo;
    }

    public Policy withApplyTo(String applyTo) {
        this.applyTo = applyTo;
        return this;
    }

    /**
     * 
     * @return
     *     The definition
     */
    @JsonProperty("definition")
    public Definition getDefinition() {
        return definition;
    }

    /**
     * 
     * @param definition
     *     The definition
     */
    @JsonProperty("definition")
    public void setDefinition(Definition definition) {
        this.definition = definition;
    }

    public Policy withDefinition(Definition definition) {
        this.definition = definition;
        return this;
    }

    /**
     * 
     * @return
     *     The priority
     */
    @JsonProperty("priority")
    public Long getPriority() {
        return priority;
    }

    /**
     * 
     * @param priority
     *     The priority
     */
    @JsonProperty("priority")
    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public Policy withPriority(Long priority) {
        this.priority = priority;
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

    public Policy withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(vhost).append(name).append(pattern).append(applyTo).append(definition).append(priority).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Policy) == false) {
            return false;
        }
        Policy rhs = ((Policy) other);
        return new EqualsBuilder().append(vhost, rhs.vhost).append(name, rhs.name).append(pattern, rhs.pattern).append(applyTo, rhs.applyTo).append(definition, rhs.definition).append(priority, rhs.priority).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
