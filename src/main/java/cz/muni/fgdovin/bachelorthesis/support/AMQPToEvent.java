package cz.muni.fgdovin.bachelorthesis.support;

import com.espertech.esperio.amqp.AMQPToObjectCollector;
import com.espertech.esperio.amqp.AMQPToObjectCollectorContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * This class implements Esper's AMQPToObjectCollector, which is used to collect
 * events coming from AMQP queue and to transform them to format suitable for
 * further processing.
 * This particular implementation collects JSON-formatted events and uses Jackson
 * to convert each event to the Map and, if present, remove nested attributes.
 *
 * @author Filip Gdovin
 * @version 6. 2. 2015
 */
public class AMQPToEvent implements AMQPToObjectCollector {
    private static final Log log = LogFactory.getLog(CustomAMQPSink.class);

    private JSONFlattener flattener = new JSONFlattener(new ObjectMapper());

    /**
     * Overridden method which is responsible for collecting event in form of byte[]
     * from AMQP queue and sending it to EventBus properly formatted.
     *
     * @param context Context provided by Esper to manipulate with.
     */
    @Override
    public void collect(final AMQPToObjectCollectorContext context) {
        String input = new String(context.getBytes());
        try {
            context.getEmitter().submit(alterMap(input));
        } catch (IOException e) {
            log.info("Malformed input: " + input + "Exception: " + e.getMessage());
        }
    }

    /**
     * This method is responsible for handling the "@timestamp" attribute, presence
     * of which is mandatory. If map of event contains no key with this name,
     * event is not sent for further processing. Otherwise, value of such attribute
     * is converted to Long as desired by Esper.
     *
     * @param input String containing one event in JSON format.
     * @return Map of one event, with proper timestamp and no nested attributes.
     * @throws IOException in case event doesn't have valid structure.
     */
    private Map<String, Object> alterMap(String input) throws IOException {
        Map<String, Object> mapOfEvent = flattener.jsonToFlatMap(input);

        // TODO: Deal with invalid input. Done?
        String key = "@timestamp";
        if(!mapOfEvent.containsKey(key)) {
            throw new IOException("Event did not contain \"@timestamp\" attribute, which is mandatory");
        } else {
            String timestampString = (mapOfEvent.get(key).toString());
            mapOfEvent.remove(key);
            Long timestampLong = parseDate(timestampString);
            mapOfEvent.put("timestamp", timestampLong);
        }
        return mapOfEvent;
    }

    /**
     * Method used to convert String-formatted timestamp
     * to Long number of milliseconds since
     * the beginning of the Linux epoch.
     * Esper needs it to be able to use external timestamps.
     *
     * @param input String in ISO 8601 format "yyyy-MM-dd'T'HH:mm:ss.SSSz"
     * @return Long number of milliseconds expressing given input time.
     */
    private Long parseDate(String input) {
        final DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME; //"yyyy-MM-dd'T'HH:mm:ss.SSSz" format
        final ZonedDateTime zonedDateTime = ZonedDateTime.parse(input, formatter);
        EventToAMQP.setZoneID(zonedDateTime.getZone().getId());
        return zonedDateTime.toInstant().toEpochMilli();
    }
}
