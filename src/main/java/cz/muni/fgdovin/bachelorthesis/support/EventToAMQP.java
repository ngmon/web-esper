package cz.muni.fgdovin.bachelorthesis.support;

import com.espertech.esperio.amqp.ObjectToAMQPCollector;
import com.espertech.esperio.amqp.ObjectToAMQPCollectorContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Map;

/**
 * This class implements Esper's ObjectToAMQPCollector, which is used to collect
 * events coming from Esper and to transform them to format suitable for
 * AMQP queue.
 * This particular implementation collects Map-formatted events and uses Jackson
 * to convert each event to the JSON.
 *
 * @author Filip Gdovin
 * @version 6. 4. 2015
 */
@Service
public class EventToAMQP implements ObjectToAMQPCollector {

    private static String zoneID;

    private static String timestampKey;

    /**
     * Overridden method which is responsible for collecting event in form of Map
     * from EventBus and sending it to AMQP queue properly formatted.
     *
     * @param context Context provided by Esper to manipulate with.
     */
    @Override
    public void collect(ObjectToAMQPCollectorContext context) {
        context.getEmitter().send(fromMap(context.getObject()));
    }

    /**
     * This method is responsible for handling the timestamp attribute, presence
     * of which is mandatory. Name of this attribute is loaded from config file.
     * If map of event contains key with this name, value of such attribute
     * is converted back to String ISO8601 as desired by Esper.
     *
     * @param object Object containing one event in Map format.
     * @return JSON representation of one event, with timestamp again in String.
     */
    private byte[] fromMap(Object object) {
        Map<String, Object> mapOfEvent = (Map<String, Object>) object;
        String key = "timestamp";
        if(mapOfEvent.containsKey(key)) {
            Long timestampLong = Long.parseLong(mapOfEvent.get(key).toString());
            mapOfEvent.remove(key);
            String timestampString = returnTimeStampAsString(timestampLong);
            mapOfEvent.put(timestampKey, timestampString);
        }

        ObjectMapper mapper = new ObjectMapper();
        String result = null;
        try {
            result = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        assert result != null;
        return result.getBytes();
    }

    /**
     * Method used to convert Long-formatted timestamp
     * to its String representation.
     *
     * @param timestamp Long number of milliseconds since the beginning of the Linux epoch.
     * @return String in ISO 8601 format "yyyy-MM-dd'T'HH:mm:ss.SSSz" expressing given input time.
     */
    private String returnTimeStampAsString(Long timestamp) {
        return Instant.ofEpochMilli(timestamp).atZone(ZoneId.of(zoneID)).toString();
    }

    /**
     * Method used by AMQPToEvent to find time zone in event,
     * so it can be properly set in the method above.
     *
     * @param zoneID String representing ZoneID needed by ZonedDateTime.
     */
    public static void setZoneID(String zoneID) {
        EventToAMQP.zoneID = zoneID;
    }

    public static void setTimestampKey(String timestampKey) {
        EventToAMQP.timestampKey = timestampKey;
    }
}
