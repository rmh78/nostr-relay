package de.rmh78.nostr.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonPropertyOrder(value = {"type", "subscriptionId"})
@JsonFormat(shape=JsonFormat.Shape.ARRAY)
public class MessageClose {
    
    private static ObjectMapper mapper = new ObjectMapper();
    
    public String type;
    public String subscriptionId;

    public MessageClose() {}
    public MessageClose(String type, String subscriptionId) {
        this.type = type;
        this.subscriptionId = subscriptionId;
    }

    public static MessageClose fromJson(String json) throws JsonProcessingException  {
        return mapper.readValue(json, MessageClose.class);
    }

    @Override
    public String toString() {
        return "MessageClose [type=" + type + ", subscriptionId=" + subscriptionId + "]";
    }
}
