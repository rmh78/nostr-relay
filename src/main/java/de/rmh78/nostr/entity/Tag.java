package de.rmh78.nostr.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Embeddable
@JsonPropertyOrder(value = {"tagKey", "tagValue", "recommendedRelayUrl"})
@JsonFormat(shape=JsonFormat.Shape.ARRAY)
public class Tag {

    public Tag() {}

    public Tag(String tagKey, String tagValue, String recommendedRelayUrl) {
        this.tagKey = tagKey;
        this.tagValue = tagValue;
        this.recommendedRelayUrl = recommendedRelayUrl;
    }

    @Column(name="tag_key")
    public String tagKey;

    @Column(name="tag_value")
    public String tagValue;
    
    @Column(name="recommended_relay_url")
    public String recommendedRelayUrl;
}
