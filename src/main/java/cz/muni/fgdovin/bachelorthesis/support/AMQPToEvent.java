package cz.muni.fgdovin.bachelorthesis.support;

import com.espertech.esper.dataflow.interfaces.EPDataFlowEmitter;
import com.espertech.esperio.amqp.AMQPToObjectCollector;
import com.espertech.esperio.amqp.AMQPToObjectCollectorContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by Filip Gdovin on 6. 2. 2015.
 */
public class AMQPToEvent implements AMQPToObjectCollector {

    @Override
    public void collect(final AMQPToObjectCollectorContext context) {
        final byte[] input = context.getBytes();
        final List<Map> received = this.extractMessage(input);
        final EPDataFlowEmitter emmiter = context.getEmitter();

        for (final Map oneEvent : received) {
            emmiter.submit(oneEvent);
        }
    }

    List<Map> extractMessage(final byte[] input) {
        final List<Map> list = new ArrayList<>(); //ak bude jedna sprava = jeden JSON, ani netreba list

        String inputString = new String(input);
        String JSON = JsonFlattener.encode(inputString);

        Map oneEvent = this.receiveEventAsMap(JSON);
        list.add(oneEvent);

        return list;
    }

    Map receiveEventAsMap(String input) throws JSONException {

        JSONObject json = new JSONObject(JsonFlattener.encode(input));
        return JSONtoMap(json);
    }

    private static Map JSONtoMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<>();

        Iterator<String> keysItr = object.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = JSONtoMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    private static List toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = JSONtoMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }
}
