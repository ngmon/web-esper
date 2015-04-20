package cz.muni.fgdovin.bachelorthesis.support;

import com.espertech.esperio.amqp.ObjectToAMQPCollector;
import com.espertech.esperio.amqp.ObjectToAMQPCollectorContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Map;

public class EventToAMQP implements ObjectToAMQPCollector {

    private static String zoneID;

    @Override
    public void collect(ObjectToAMQPCollectorContext context) {
        context.getEmitter().send(fromMap(context.getObject()));
    }

    private byte[] fromMap(Object object) {
        Map<String, Object> mapOfEvent = (Map<String, Object>) object;
        String key = "timestamp";
        if(mapOfEvent.containsKey(key)) {
            Long timestampLong = Long.parseLong(mapOfEvent.get(key).toString());
            mapOfEvent.remove(key);
            String timestampString = returnTimeStampAsString(timestampLong);
            mapOfEvent.put("@timestamp", timestampString);
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

    private String returnTimeStampAsString(Long timestamp) {
        return Instant.ofEpochMilli(timestamp).atZone(ZoneId.of(zoneID)).toString();
    }

    public static void setZoneID(String zoneID) {
        EventToAMQP.zoneID = zoneID;
    }
}
