package cz.muni.fgdovin.bachelorthesis.support;

import cz.muni.fgdovin.bachelorthesis.web.Property;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Support class for easy-to-use conversion of either
 * String-formatted event properties,
 * or list of Property instances
 * to Map of those properties required by Esper.
 * Also '@' is removed from "@timestamp" attribute
 * because Esper treats it as special character.
 *
 * @author Filip Gdovin
 * @version 7. 3. 2015
 */
public class EventTypeHelper {

    /**
     * Method used to convert String of format (val1 Integer, val2 String)
     * to Map. Second entity must be String, Boolean, Integer or Long.
     * When something else is detected, it will be dealt with as String.
     *
     * @param properties String containing pairs "attributeName attributeType"
     * @return Map representation of such String, with special characters removed.
     */
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
            //TODO remove from all attributes if necessary?
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

    /**
     * Method used to convert List of Property instances to Map.
     * Value in each Property must be String, Boolean, Integer or Long.
     * When something else is detected, it will be dealt with as String.
     *
     * @param properties List of Property, where each contains pair "attributeName attributeType"
     * @return Map representation of such List, with special characters removed.
     */
    public Map<String, Object> toMap(List<Property> properties) {
        if((properties == null) || (properties.isEmpty()) ) {
            return null;
        }

        Map<String, Object> schemaMap = new HashMap<>();

        for (Property oneProp : properties) {
            String pair = oneProp.toString();
            pair = pair.trim();
            int spacePosition = pair.indexOf(" ");
            String key = pair.substring(0, spacePosition);
            //TODO remove from all attributes if necessary?
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