package cz.muni.fgdovin.bachelorthesis.support;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JSONFlattener {

    private ObjectMapper objectMapper;

    public JSONFlattener(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * This method converts one event saved in JSON format as String
     * to Map. Jackson library is used to do the conversion.
     *
     * @param json String containing valid JSON in format {"val1":8, "val2":"eight"}
     * @return Map representation of input event.
     * @throws java.io.IOException in case Jackson throws it, so in case of malformed input.
     */
    public Map<String, Object> jsonToFlatMap(String json) throws IOException {
        return process(this.objectMapper.readValue(json, JsonNode.class).fields());
    }

    /**
     * Method used to process map, changing nested attributes
     * into simple ones.
     *
     * @param nodeIterator iterator over one map entry
     * @return map without nested attributes
     */
    private Map<String, Object> process(Iterator<Map.Entry<String, JsonNode>> nodeIterator) {
        Map<String, Object> map1 = new HashMap<>();

        while (nodeIterator.hasNext()) {
            Map.Entry<String, JsonNode> entry1 = nodeIterator.next();

            String key = entry1.getKey();
            JsonNode value = entry1.getValue();

            if (value.getNodeType().equals(JsonNodeType.OBJECT)) {
                Map<String, Object> map2 = process(value.fields()); // recursion

                for (Map.Entry<String, Object> entry2 : map2.entrySet()) {
                    map1.put(key.concat(".").concat(entry2.getKey()), entry2.getValue());
                }

            } else {
                map1.put(key, this.objectMapper.convertValue(value, Object.class));
            }
        }
        return map1;
    }
}
