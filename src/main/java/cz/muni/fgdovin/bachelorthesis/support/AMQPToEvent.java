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
        final Map received = this.extractMessage(input);
        final EPDataFlowEmitter emmiter = context.getEmitter();

        emmiter.submit(received);
    }

    Map extractMessage(final byte[] input) {
        String inputString = new String(input);
        String JSON = JsonFlattener.encode(inputString);
        Map oneEvent = this.receiveEventAsMap(JSON);
        return oneEvent;
    }

    Map receiveEventAsMap(String input) throws JSONException {

        JSONObject json = new JSONObject(JsonFlattener.encode(input));
        return JSONtoMap(json);
    }

    private static Map JSONtoMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

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
        List<Object> list = new ArrayList<Object>();
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
