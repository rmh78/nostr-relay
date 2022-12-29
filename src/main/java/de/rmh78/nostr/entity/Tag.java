package de.rmh78.nostr.entity;

import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Embeddable
@JsonPropertyOrder(value = {"tagKey", "tagValue", "recommendedRelayUrl"})
@JsonFormat(shape=JsonFormat.Shape.ARRAY)
public class Tag {
    public String tagKey;
    public String tagValue;
    public String recommendedRelayUrl;
}
