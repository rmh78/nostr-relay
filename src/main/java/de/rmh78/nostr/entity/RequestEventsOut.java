package de.rmh78.nostr.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonPropertyOrder(value = {"type", "subscriptionId", "event"})
@JsonFormat(shape=JsonFormat.Shape.ARRAY)
public class RequestEventsOut {
    private static ObjectMapper mapper = new ObjectMapper();

    public String type;
    public String subscriptionId;
    public Event event;

    public RequestEventsOut() {}
    public RequestEventsOut(String type, String subscriptionId, Event event) {
        this.type = type;
        this.subscriptionId = subscriptionId;
        this.event = event;
    }

    public static RequestEventsOut fromJson(String json) throws JsonProcessingException  {
        return mapper.readValue(json, RequestEventsOut.class);
    }

    @Override
    public String toString() {
        return "RequestEventsOut [type=" + type + ", subscriptionId=" + subscriptionId + ", event=" + event + "]";
    }
}
