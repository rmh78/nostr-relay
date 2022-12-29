package de.rmh78.nostr.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonPropertyOrder(value = {"type", "event"})
@JsonFormat(shape=JsonFormat.Shape.ARRAY)
public class PublishEventIn {

    private static ObjectMapper mapper = new ObjectMapper();
    
    public String type;
    public Event event;

    public PublishEventIn() {}
    public PublishEventIn(String type, Event event) {
        this.type = type;
        this.event = event;
    }

    public static PublishEventIn fromJson(String json) throws JsonProcessingException  {
        return mapper.readValue(json, PublishEventIn.class);
    }

    @Override
    public String toString() {
        return "PublishEventIn [type=" + type + ", event=" + event + "]";
    }
}
