package degreesapp.models;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public abstract class Notification {
    public abstract String notificationType();

    public ObjectNode notificationBody() {
        JsonMapper jsonMapper = new JsonMapper();
        ObjectNode tree = jsonMapper.valueToTree(this);
        if (tree.has("type"))
            throw new RuntimeException("Notification body has 'type' key");
        return tree;
    }
}