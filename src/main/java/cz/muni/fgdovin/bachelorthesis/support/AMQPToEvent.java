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
        Map oneEvent = this.receiveEventAsMap(inputString);
        return oneEvent;
    }

    Map receiveEventAsMap(String input) throws JSONException {

        JSONObject json = new JSONObject(encode(input));
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

    private static String encode(JSONObject jo) throws JSONException {
        return "{" + encode(null, jo) + "}";

    }

    public static String encode(String json) throws JSONException {
        JSONObject jo = new JSONObject(json);
        return encode(jo);
    }

    private static String encode(String parent, Object val)
            throws JSONException {
        StringBuilder sb = new StringBuilder();
        if (val instanceof JSONObject) {
            JSONObject jo = (JSONObject) val;
            for (Iterator<String> i = jo.keys(); i.hasNext(); ) {
                String key = i.next();
                String hkey = (parent == null) ? key : parent + "." + key;
                Object jval = jo.get(key);
                String json = encode(hkey, jval);
                sb.append(json);
                if (i.hasNext()) {
                    sb.append(",");
                }
            }
        } else if (val instanceof JSONArray) {
            JSONArray ja = (JSONArray) val;
            for (int i = 0; i < ja.length(); i++) {
                String hkey = (parent == null) ? "" + i : parent + "." + i;
                Object aval = ja.get(i);
                String json = encode(hkey, aval);
                sb.append(json);
                if (i < ja.length() - 1) {
                    sb.append(",");
                }
            }
        } else if (val instanceof String) {
            sb.append("\"").append(parent).append("\"").append(":");
            String s = (String) val;
            sb.append(JSONObject.quote(s));
        } else if (val instanceof Integer) {
            sb.append("\"").append(parent).append("\"").append(":");
            Integer integer = (Integer) val;
            sb.append(integer);
        }

        return sb.toString();
    }
}
