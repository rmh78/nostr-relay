package de.rmh78.nostr.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonPropertyOrder(value = {"type", "subscriptionId", "filter"})
@JsonFormat(shape=JsonFormat.Shape.ARRAY)
public class RequestEventsIn {

    private static ObjectMapper mapper = new ObjectMapper();

    public String type;
    public String subscriptionId;
    public Filter filter;

    public RequestEventsIn() {}
    public RequestEventsIn(String type, String subscriptionId, Filter filter) {
        this.type = type;
        this.subscriptionId = subscriptionId;
        this.filter = filter;
    }

    public static RequestEventsIn fromJson(String json) throws JsonProcessingException  {
        return mapper.readValue(json, RequestEventsIn.class);
    }

    @Override
    public String toString() {
        return "RequestEventsIn [type=" + type + ", subscriptionId=" + subscriptionId + ", filter=" + filter + "]";
    }
}
