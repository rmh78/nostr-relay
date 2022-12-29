package de.rmh78.nostr.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
@Table(name="events")
public class Event extends PanacheEntityBase {
    public static int KIND_SET_METADATA = 0;
    public static int KIND_TEXT_NOTE = 1;
    public static int KIND_RECOMMEND_SERVER = 2;
    public static int KIND_CONTACTS = 3;
    public static int KIND_ENCRYPTED_CHAT = 4;

    /**
     * 32-bytes sha256 of the the serialized event data
     */
    @Id
    public String id;
    
    /**
     * 32-bytes hex-encoded public key of the event creator
     */
    public String pubkey;

    /**
     * unix timestamp in seconds
     */
    @JsonProperty("created_at")
    @Column(name="created_at")
    public long createdAt;

    /**
     * number of the event kind
     */
    public int kind;
    
    /**
     * arbitrary string
     */
    public String content;
    
    /**
     * 64-bytes signature of the sha256 hash of the serialized event data, which is the same as the "id" field
     */
    public String sig;

    /**
     * list of tags
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "tags", joinColumns=@JoinColumn(name = "event_id"))
    public List<Tag> tags = new ArrayList<>();
}
