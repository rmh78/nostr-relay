package de.rmh78.nostr.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Event {
    public static int KIND_SET_METADATA = 0;
    public static int KIND_TEXT_NOTE = 1;
    public static int KIND_RECOMMEND_SERVER = 2;

    public String id;
    public String pubkey;
    @JsonProperty("created_at")
    public long createdAt;
    public int kind;
    public List<Tag> tags;
    public String content;
    public String sig;

    public Event() {
        this.id = "0";
        this.pubkey = "abc";
        this.createdAt = 1000;
        this.kind = KIND_SET_METADATA;
    }
}
