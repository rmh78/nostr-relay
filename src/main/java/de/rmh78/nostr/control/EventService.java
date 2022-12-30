package de.rmh78.nostr.control;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import de.rmh78.nostr.entity.Event;
import de.rmh78.nostr.entity.RequestEventsIn;
import de.rmh78.nostr.entity.RequestEventsOut;

@ApplicationScoped
public class EventService {
    @Inject
    Logger logger;

    @Transactional
    @Incoming("publish-event-in")
    //TODO: add outgoing to channel request-events-out (applying filter)
    public void saveEvent(Event event) {
        // TODO: cleanup
        if (event.id == null) {
            event.id = UUID.randomUUID().toString();
        }
        event.persist();

        // use same query-builder from before and add the id
    }

    @Transactional
    @Incoming("request-events-in")
    @Outgoing("request-events-out")
    public List<RequestEventsOut> queryEvents(RequestEventsIn message) {

        // TODO: handle limit with panache paging?

        var queryBuilder = QueryBuilder.create()
            .addCriteria("e.id in :ids", message.filter.ids)
            .addCriteria("e.pubkey in :authors", message.filter.authors)
            .addCriteria("e.kind in :kinds", message.filter.kinds)
            .addCriteria("e.createdAt >= :since", message.filter.since)
            .addCriteria("e.createdAt <= :until", message.filter.until)
            .addCriteria("t.tagKey || '#' || t.tagValue in :tags", message.filter.getAllTagsWithKey());

        List<Event> events;
        if (queryBuilder.getParameters().isEmpty()) {
            events = Event.listAll();
        } else {
            events =  Event.list("from Event e join e.tags t where " + queryBuilder.getQuery(), queryBuilder.getParameters());
        }

        return events.stream()
            .map(event -> new RequestEventsOut("EVENT", message.subscriptionId, event))
            .collect(Collectors.toList());
    }
}
