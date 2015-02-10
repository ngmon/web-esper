package cz.muni.fgdovin.bachelorthesis.support;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Filip Gdovin on 18. 1. 2015.
 */
public class CreateEventSchema {

// --Commented out by Inspection START (5. 2. 2015 12:46):
//    public static Map getSchemaFromMap(Map oneEvent) {
//        Map retMap = new HashMap();
//        Set keys = oneEvent.keySet();
//        for (int i = 0; i < keys.size(); i++) {
//            String key = (String) keys.toArray()[i];
//            Object value = oneEvent.get(key);
//            if (isInteger(value.toString())) {
//                retMap.put(key, Integer.class);
//            } else if (isBool(value.toString())) {
//                retMap.put(key, Boolean.class);
//            } else
//                retMap.put(key, String.class);
//        }
//        return retMap;
//    }
// --Commented out by Inspection STOP (5. 2. 2015 12:46)

    public static String schemaMapToString(Map schema) {
        StringBuilder result = new StringBuilder("");
        Set keys = schema.keySet();

        for (int i = 0; i < keys.size(); i++) {
            String key = (String) keys.toArray()[i];
            Object value = schema.get(key);
            result.append(key).append(" " + schema.get(key));
            if (i < keys.size() - 1) {
                result.append(", ");
            }
        }
        return result.toString();
    }

    public static Map stringToSchemaMap(String schema) {
        String[] pairs = schema.split(", ");

        Map<String, Object>  schemaMap= new HashMap<>();

        for (String pair : pairs) {
            int spacePosition = pair.indexOf(" ");
            String key = pair.substring(0, spacePosition);
            String value = pair.substring(spacePosition);

            if (value.equalsIgnoreCase("Integer")) {
                schemaMap.put(key, Integer.class);
            } else if (value.equalsIgnoreCase("Boolean")) {
                schemaMap.put(key, Boolean.class);
            } else {
                schemaMap.put(key, String.class);
            }
        }
        return schemaMap;
    }

    private static boolean isInteger(String str) {
        try {
            //noinspection ResultOfMethodCallIgnored
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    private static boolean isBool(String str) {
        //noinspection RedundantIfStatement
        if ((str.equalsIgnoreCase("true")) || (str.equalsIgnoreCase("false"))) {
            return true;
        } else {
            return false;
        }
    }
}
