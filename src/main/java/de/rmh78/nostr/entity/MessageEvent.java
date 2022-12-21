package de.rmh78.nostr.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonPropertyOrder(value = {"type", "event"})
@JsonFormat(shape=JsonFormat.Shape.ARRAY)
public class MessageEvent {

    private static ObjectMapper mapper = new ObjectMapper();
    
    public String type;
    public Event event;

    public MessageEvent() {}
    public MessageEvent(String type, Event event) {
        this.type = type;
        this.event = event;
    }

    public static MessageEvent fromJson(String json) throws JsonProcessingException  {
        return mapper.readValue(json, MessageEvent.class);
    }

    @Override
    public String toString() {
        return "MessageEvent [type=" + type + ", event=" + event + "]";
    }
}
