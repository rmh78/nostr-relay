package de.rmh78.nostr.entity;

import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Embeddable
@JsonPropertyOrder(value = {"key", "value", "recommendedRelayUrl"})
@JsonFormat(shape=JsonFormat.Shape.ARRAY)
public class Tag {
    public String key;
    public String value;
    public String recommendedRelayUrl;
}
