package de.rmh78.nostr.entity;

public class Subscription {
    public String id;
    public Filter filter;

    public Subscription(String id, Filter filter) {
        this.id = id;
        this.filter = filter;
    }
}
