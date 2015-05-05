package cz.muni.fgdovin.bachelorthesis.web;

import cz.muni.fgdovin.bachelorthesis.support.EventProperty;

import java.util.List;

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
    private String exchange;  //TODO redundant?
    private List<EventProperty> mapProperties;

    public EventTypeModel() {
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

    public List<EventProperty> getMapProperties() {
        return mapProperties;
    }

    public void setMapProperties(List<EventProperty> mapProperties) {
        this.mapProperties = mapProperties;
    }
}
