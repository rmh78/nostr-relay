package de.rmh78.nostr.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonPropertyOrder(value = {"type", "subscriptionId", "filter"})
@JsonFormat(shape=JsonFormat.Shape.ARRAY)
public class MessageRequest {

    private static ObjectMapper mapper = new ObjectMapper();

    public String type;
    public String subscriptionId;
    public Filter filter;

    public MessageRequest() {}
    public MessageRequest(String type, String subscriptionId, Filter filter) {
        this.type = type;
        this.subscriptionId = subscriptionId;
        this.filter = filter;
    }

    public static MessageRequest fromJson(String json) throws JsonProcessingException  {
        return mapper.readValue(json, MessageRequest.class);
    }

    @Override
    public String toString() {
        return "RequestMessage [type=" + type + ", subscriptionId=" + subscriptionId + ", filter=" + filter + "]";
    }
}
