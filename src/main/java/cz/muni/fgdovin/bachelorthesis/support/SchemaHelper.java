package cz.muni.fgdovin.bachelorthesis.support;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Filip Gdovin on 7. 3. 2015.
 */
public class SchemaHelper {

    private String eventType;
    private String properties;

    public SchemaHelper() {
    }

    public SchemaHelper(String eventType, String properties) {
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

    public static Map<String, Object> toMap(String properties) {
        String[] pairs = properties.split(",");

        Map<String, Object> schemaMap= new HashMap<String, Object>();

        for (String pair : pairs) {
            pair = pair.trim();
            int spacePosition = pair.indexOf(" ");
            String key = pair.substring(0, spacePosition);
            String value = pair.substring(spacePosition+1);

            if (value.equalsIgnoreCase("Integer")) {
                schemaMap.put(key, Integer.class);
            } else if (value.equalsIgnoreCase("Boolean")) {
                schemaMap.put(key, Boolean.class);
            } else if (value.equalsIgnoreCase("Long")) {
                schemaMap.put(key, Long.class);
            }else {
                schemaMap.put(key, String.class);
            }
        }

        return schemaMap;
    }
}
