package cz.muni.fgdovin.bachelorthesis.support;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

@SuppressWarnings("ALL")
public class JsonFlattener {
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

    public static String decode(String flatJson) throws JSONException {
        JSONObject encoded = new JSONObject(flatJson);
        return decodeToString(encoded);
    }

    private static String decodeToString(JSONObject encoded)
            throws JSONException {
        return decodeToObject(encoded).toString();
    }

    private static JSONObject decodeToObject(JSONObject encoded)
            throws JSONException {
        JSONObject decoded = new JSONObject();

        for (Iterator<String> i = encoded.keys(); i.hasNext(); ) {
            String hkey = i.next();
            String[] keys = hkey.split("\\.");

            Object json = decoded;

            for (int j = 0; j < keys.length; j++) {
                if (j == keys.length - 1) {
                    Object val = encoded.get(hkey);
                    if (json instanceof JSONObject) {
                        JSONObject jo = (JSONObject) json;
                        jo.put(keys[j], val);
                    } else if (json instanceof JSONArray) {
                        JSONArray ja = (JSONArray) json;
                        int index = Integer.parseInt(keys[j]);
                        ja.put(index, val);
                    }
                } else {
                    // we're NOT at a leaf key

                    if (!isNumber(keys[j + 1])) {
                        // next index is an object

                        JSONObject joChild;

                        if (json instanceof JSONObject) {
                            // last index was an object
                            // we're creating an object in an object
                            JSONObject jo = (JSONObject) json;
                            if (jo.has(keys[j])) {
                                joChild = jo.getJSONObject(keys[j]);
                            } else {
                                joChild = new JSONObject();
                                jo.put(keys[j], joChild);
                            }
                        } else if (json instanceof JSONArray) {
                            // last index was an array
                            // we're creating an object in an array
                            JSONArray ja = (JSONArray) json;
                            int index = Integer.parseInt(keys[j]);
                            if (!ja.isNull(index)) {
                                joChild = ja.getJSONObject(index);
                            } else {
                                joChild = new JSONObject();
                                ja.put(index, joChild);
                            }
                        } else {
                            throw new AssertionError("unhandled object type");
                        }
                        json = joChild;
                    } else {
                        // next index is an array element

                        JSONArray jaChild;

                        if (json instanceof JSONObject) {
                            // last index was an object,
                            // we're creating an array in an object
                            JSONObject jo = (JSONObject) json;
                            if (jo.has(keys[j])) {
                                jaChild = jo.getJSONArray(keys[j]);
                            } else {
                                jaChild = new JSONArray();
                                jo.put(keys[j], jaChild);
                            }
                        } else if (json instanceof JSONArray) {
                            // last index was an array
                            // we're creating an array in an array
                            JSONArray ja = (JSONArray) json;
                            int index = Integer.parseInt(keys[j + 1]);
                            if (!ja.isNull(index)) {
                                jaChild = ja.getJSONArray(index);
                            } else {
                                jaChild = new JSONArray();
                                ja.put(index, jaChild);
                            }
                        } else {
                            throw new AssertionError("unhandled object type");
                        }
                        json = jaChild;
                    }
                }
            }
        }
        return decoded;
    }

    private static boolean isNumber(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }

    }
}