package de.rmh78.nostr.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;


@Entity
public class Event extends PanacheEntityBase {
    public static int KIND_SET_METADATA = 0;
    public static int KIND_TEXT_NOTE = 1;
    public static int KIND_RECOMMEND_SERVER = 2;

    @Id
    public String id;
    
    public String pubkey;
    @JsonProperty("created_at")
    public long createdAt;
    public int kind;
    //public List<Tag> tags;
    public String content;
    public String sig;
}
