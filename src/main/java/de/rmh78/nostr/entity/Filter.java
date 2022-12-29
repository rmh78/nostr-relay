package de.rmh78.nostr.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Filter {
    /**
     * a list of event ids or prefixes
     */
    public List<String> ids;

    /**
     * a list of pubkeys or prefixes, the pubkey of an event must be one of these
     */
    public List<String> authors;

    /**
     * a list of a kind numbers
     */
    public List<Integer> kinds;

    /**
     * a list of event ids that are referenced in an "e" tag
     */
    @JsonProperty("#e")
    public List<String> eTags;

    /**
     * a list of pubkeys that are referenced in a "p" tag
     */
    @JsonProperty("#p")
    public List<String> pTags;

    /**
     * a timestamp, events must be newer than this to pass
     */
    public Long since;

    /**
     * a timestamp, events must be older than this to pass
     */
    public Long until;

    /**
     * maximum number of events to be returned in the initial query
     */
    public Integer limit;

    public List<String> getAllTagsWithKey() {
        var e = eTags.stream().map(t -> "e#" + t).collect(Collectors.toList());
        var p = pTags.stream().map(t -> "p#" + t).collect(Collectors.toList());
        var allTags = new ArrayList<String>();
        allTags.addAll(e);
        allTags.addAll(p);
        return allTags;
    }

    @Override
    public String toString() {
        return "Filter [ids=" + ids + ", authors=" + authors + ", kinds=" + kinds + ", eTags=" + eTags + ", pTags="
                + pTags + ", since=" + since + ", until=" + until + ", limit=" + limit + "]";
    }
}
