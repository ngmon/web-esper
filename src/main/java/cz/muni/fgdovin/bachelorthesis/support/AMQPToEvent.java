package cz.muni.fgdovin.bachelorthesis.support;

import com.espertech.esperio.amqp.AMQPToObjectCollector;
import com.espertech.esperio.amqp.AMQPToObjectCollectorContext;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Filip Gdovin
 * @version 6. 2. 2015
 */
public class AMQPToEvent implements AMQPToObjectCollector {
    private static final Log log = LogFactory.getLog(CustomAMQPSink.class);

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void collect(final AMQPToObjectCollectorContext context) {
        String input = new String(context.getBytes());
        try {
            context.getEmitter().submit(alterMap(input));
        } catch (IOException e) {
            log.info("Malformed input: " + input + "Exception: " + e.getMessage());
        }
    }

    // TODO: Deal with invalid input
    private Map<String, Object> alterMap(String input) throws IOException {
        Map<String, Object> mapOfEvent = jsonToFlatMap(input);

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

    private Long parseDate(String input) {
        final DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME; //"yyyy-MM-dd'T'HH:mm:ss.SSSz" format
        final ZonedDateTime zonedDateTime = ZonedDateTime.parse(input, formatter);
        EventToAMQP.setZoneID(zonedDateTime.getZone().getId());
        return zonedDateTime.toInstant().toEpochMilli();
    }

    public Map<String, Object> jsonToFlatMap(String json) throws IOException {
        return process(this.mapper.readValue(json, JsonNode.class).fields());
    }

    private Map<String, Object> process(Iterator<Map.Entry<String, JsonNode>> nodeIterator) {
        Map<String, Object> map1 = new HashMap<>();

        while (nodeIterator.hasNext()) {
            Map.Entry<String, JsonNode> entry1 = nodeIterator.next();

            String key = entry1.getKey();
            JsonNode value = entry1.getValue();

            if (value.getNodeType().equals(JsonNodeType.OBJECT)) {
                Map<String, Object> map2 = process(value.fields());

                for (Map.Entry<String, Object> entry2 : map2.entrySet()) {
                    map1.put(key.concat(".").concat(entry2.getKey()), entry2.getValue());
                }

            } else {
                map1.put(key, this.mapper.convertValue(value, Object.class));
            }
        }
        return map1;
    }
}
