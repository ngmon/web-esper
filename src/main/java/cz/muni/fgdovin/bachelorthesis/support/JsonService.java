package cz.muni.fgdovin.bachelorthesis.support;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JsonService {

    private ObjectMapper mapper = new ObjectMapper();

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
