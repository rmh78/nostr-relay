package de.rmh78.nostr.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonPropertyOrder(value = {"type", "subscriptionId"})
@JsonFormat(shape=JsonFormat.Shape.ARRAY)
public class CloseSubscriptionIn {
    
    private static ObjectMapper mapper = new ObjectMapper();
    
    public String type;
    public String subscriptionId;

    public CloseSubscriptionIn() {}
    public CloseSubscriptionIn(String type, String subscriptionId) {
        this.type = type;
        this.subscriptionId = subscriptionId;
    }

    public static CloseSubscriptionIn fromJson(String json) throws JsonProcessingException  {
        return mapper.readValue(json, CloseSubscriptionIn.class);
    }

    @Override
    public String toString() {
        return "CloseSubscriptionIn [type=" + type + ", subscriptionId=" + subscriptionId + "]";
    }
}
