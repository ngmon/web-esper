package cz.muni.fgdovin.bachelorthesis.web;

import org.nigajuan.rabbit.management.client.domain.exchange.Exchange;

import java.util.List;
import java.util.Map;

/**
 * Model class to represent event type,
 * which is needed by Spring for binding.
 * List of Property is used so Spring
 * can fill it from user-filled form.
 * @author Filip Gdovin
 * @version 26. 3. 2015
 */
public class EventTypeModel {

    private String eventType;
    private String properties;
    private String exchange;
    private Map<String, Object> mapProperties;

    public EventTypeModel() {
    }

    public EventTypeModel(String eventType, String properties) {
        this.eventType = eventType;
        this.properties = properties;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public Map<String, Object> getMapProperties() {
        return mapProperties;
    }

    public void setMapProperties(Map<String, Object> mapProperties) {
        this.mapProperties = mapProperties;
    }
}
