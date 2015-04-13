package cz.muni.fgdovin.bachelorthesis.support;

import com.espertech.esperio.amqp.AMQPToObjectCollector;
import com.espertech.esperio.amqp.AMQPToObjectCollectorContext;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.*;

/**
 * Created by Filip Gdovin on 6. 2. 2015.
 */
public class AMQPToEvent implements AMQPToObjectCollector {

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger("AMQPToEvent");
    private HashMap<String, Object> resultMap = new HashMap<String, Object>();

    @Override
    public void collect(final AMQPToObjectCollectorContext context) {
        String input = new String(context.getBytes());
        flatMap(input, "");
        context.getEmitter().submit(resultMap);
    }

    private Map toMap(String input) {
        TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {};
        Map<String, String> nestedMap = null;
        try {
            nestedMap = new ObjectMapper().readValue(input, typeRef);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nestedMap;
    }

    private void flatMap(String input, String keyPrefix) {
        Map<String, Object> nestedMap = toMap(input);
        for (String key : nestedMap.keySet()) {
            String actualKey = keyPrefix + key;
            Object value = nestedMap.get(key);
            if((actualKey.contains("timestamp")) && (!(value instanceof Long))) {
                value = (Long) parseDate(value.toString());
            }
            putInMap(actualKey, value);
        }
    }

    private void putInMap(String actualKey, Object value) {
        if(value instanceof Number) {
            resultMap.put(actualKey, value);
        }
        else if(value instanceof Boolean) {
            resultMap.put(actualKey, (Boolean) value);
        }
        else if(value.getClass().isArray()) {
            StringBuilder mapAsString = new StringBuilder("[");
            Object[] myArray = (Object[])value;
            for(int i = 0; i < myArray.length; i++) {
                Object arrayValue = myArray[i];
                if ((arrayValue instanceof Number) || (arrayValue instanceof Boolean)
                        || (arrayValue instanceof Map) || (arrayValue.getClass().isArray())){
                    mapAsString.append("\"" + actualKey + "[" + i + "]\": " + arrayValue);
                } else {
                    mapAsString.append("\"" + actualKey + "[" + i + "]\": \"" + arrayValue + "\"");
            }


            }
            mapAsString.append("]");
            flatMap(mapAsString.toString(), actualKey + ".");
        }
        else if(value instanceof Map) {
            StringBuilder mapAsString = new StringBuilder("{");
            int keySetSize = ((Map) value).keySet().size();
            int current = 0;
            for(Object mapKey : ((Map) value).keySet()) {
                String stringKey = mapKey.toString();
                Object mapValue = ((Map) value).get(stringKey);
                if ((mapValue instanceof Number) || (mapValue instanceof Boolean)
                        || (mapValue instanceof Map) || (mapValue.getClass().isArray())) {
                    mapAsString.append("\"" + stringKey + "\": " + mapValue);
                } else {
                    mapAsString.append("\"" + stringKey + "\": \"" + mapValue + "\"");
                }
                if(current < keySetSize-1) {
                    mapAsString.append(", ");
                }
                current++;
            }
            mapAsString.append("}");
            flatMap(mapAsString.toString(), actualKey + ".");
        }
        else {
            resultMap.put(actualKey, value);
        }
    }

    private Long parseDate(String input) {
        final DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME; //"yyyy-MM-dd'T'HH:mm:ss.SSSz" format
        final ZonedDateTime zonedDateTime = ZonedDateTime.parse(input, formatter);
        return zonedDateTime.toInstant().toEpochMilli();
    }
}
