package cz.muni.fgdovin.bachelorthesis.web;

import java.util.HashMap;

/**
 * @author Filip Gdovin
 * @version 26. 3. 2015
 */
public class EventTypeModel {

    private String eventType;
    private String properties;
    private HashMap<String, Object> mapProp;

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

    public HashMap<String, Object> getMapProp() {
        return mapProp;
    }

    public void setMapProp(HashMap<String, Object> mapProp) {
        this.mapProp = mapProp;
    }
}
