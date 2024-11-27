package org.example.alvin.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class JsonFlatteningAndUnflattening {

    public static void main(String[] args) throws IOException {
        String flatJson =
            "{ \"name\": \"John\", \"age\": 30, \"address.street\": \"123 Main St\", \"address.city\": \"Anytown\" }";

        ObjectMapper mapper = new ObjectMapper();
        JsonNode flatJsonNode = mapper.readTree(flatJson);
        JsonNode originalJsonNode = unflatten(flatJsonNode);
        String originalJson = mapper.writeValueAsString(originalJsonNode);


        System.out.println(originalJson);
        User user = mapper.readValue(originalJson, User.class);
        System.out.print(user);
    }

    private static JsonNode unflatten(JsonNode flatJsonNode) {
        ObjectNode rootNode = mapper.createObjectNode();

        Iterator<Map.Entry<String, JsonNode>> fields = flatJsonNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            String key = entry.getKey();
            JsonNode value = entry.getValue();

            String[] keys = key.split("\\.");
            ObjectNode currentNode = rootNode;
            for (int i = 0; i < keys.length - 1; i++) {
                String currentKey = keys[i];
                if (!currentNode.has(currentKey)) {
                    currentNode.putObject(currentKey);
                }
                currentNode = (ObjectNode)currentNode.get(currentKey);
            }
            currentNode.put(keys[keys.length - 1], value);
        }

        return rootNode;
    }

    // Note: This mapper instance should be reused across your application to avoid unnecessary object creation.
    private static final ObjectMapper mapper = new ObjectMapper();
}
