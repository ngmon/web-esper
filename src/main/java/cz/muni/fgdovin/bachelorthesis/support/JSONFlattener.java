package cz.muni.fgdovin.bachelorthesis.support;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.Iterator;

/**
 * Created by Filip Gdovin on 1. 3. 2015.
 */
public class JSONFlattener {

    public static String encode(String json) throws JSONException {
        JSONObject jo = new JSONObject(json);
        return encode(jo);
    }

    private static String encode(JSONObject jo) throws JSONException {
        return "{" + encode(null, jo) + "}";
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
