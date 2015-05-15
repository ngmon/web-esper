package cz.muni.fgdovin.bachelorthesis.support;

import org.springframework.core.env.Environment;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Support class for easy-to-use conversion of list of
 * Property instances to Map required by Esper.
 * Also, timestamp attribute specified in config file
 * is renamed to "timestamp".
 *
 * @author Filip Gdovin
 * @version 7. 3. 2015
 */
public class EventTypeHelper {
    /**
     * Method used to convert List of Property instances to Map.
     * Value in each Property must be String, Boolean, Integer or Long.
     * When something else is detected, it will be dealt with as String.
     *
     * @param properties List of Property, where each contains pair "attributeName attributeType"
     * @return Map representation of such List, with special characters removed.
     */

    @Resource
    private Environment environment;

    public Map<String, Object> toMap(List<EventProperty> properties) {
        if((properties == null) || (properties.isEmpty()) ) {
            return null;
        }

        Map<String, Object> schemaMap = new HashMap<>();

        for (EventProperty oneProp : properties) {
            String pair = oneProp.toString();
            pair = pair.trim();
            int spacePosition = pair.indexOf(" ");
            String key = pair.substring(0, spacePosition);
            //TODO remove from all attributes if necessary?

            String timestampKey = environment.getProperty("timestampAttribute");
            if(key.equals(timestampKey)) {
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