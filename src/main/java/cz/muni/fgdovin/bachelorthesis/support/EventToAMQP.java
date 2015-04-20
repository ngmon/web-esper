package cz.muni.fgdovin.bachelorthesis.support;

import com.espertech.esperio.amqp.ObjectToAMQPCollector;
import com.espertech.esperio.amqp.ObjectToAMQPCollectorContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.time.ZoneId;

public class EventToAMQP implements ObjectToAMQPCollector {

    private static String zoneID;

    @Override
    public void collect(ObjectToAMQPCollectorContext context) {
        context.getEmitter().send(fromMap(context.getObject()));
    }

    private byte[] fromMap(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        String result = null;
        try {
            result = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        result = returnTimeStampAsString(result);
        return result.getBytes();
    }

    private String returnTimeStampAsString(String input) {
        String pattern = "timestamp\":";
        int startIndex = input.indexOf(pattern);
        if(startIndex < 0) { //pattern not found, which is very bad because value can be there :(
            return input;
        }
        StringBuilder timestampValue = new StringBuilder();
        int endIndex = startIndex + pattern.length();
        char current = input.charAt(endIndex);
        while (Character.isDigit(current) || (current == ' ')) {
            if(current == ' ') {
                endIndex++;
                current = input.charAt(endIndex);
                continue;
            }
            timestampValue.append(current);
            endIndex++;
            current = input.charAt(endIndex);
        }
        Long timestamp = Long.parseLong(timestampValue.toString());
        String correctTimestamp = "\"" + Instant.ofEpochMilli(timestamp).atZone(ZoneId.of(zoneID)).toString() + "\"";
        String result = input.replace("timestamp", "@timestamp");
        result = result.replace(timestampValue.toString(), correctTimestamp);
        return result;
    }

    public static void setZoneID(String zoneID) {
        EventToAMQP.zoneID = zoneID;
    }
}
