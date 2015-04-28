package cz.muni.fgdovin.bachelorthesis.support;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Filip Gdovin
 * @version 7. 3. 2015
 */
public class EventTypeHelper {

    public Map<String, Object> toMap(String properties) {
        if((properties == null) || (properties.isEmpty()) ) {
            return null;
        }
        String[] pairs = properties.split(",");

        Map<String, Object> schemaMap = new HashMap<>();

        for (String pair : pairs) {
            pair = pair.trim();
            int spacePosition = pair.indexOf(" ");
            String key = pair.substring(0, spacePosition);
            if(key.equals("@timestamp")) {
                key = "timestamp";
            }
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