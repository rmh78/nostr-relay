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
    public void saveEvent(Event event) {
        // TODO: cleanup
        if (event.id == null) {
            event.id = UUID.randomUUID().toString();
        }
        event.persist();
    }

    @Transactional
    @Incoming("request-events-in")
    @Outgoing("request-events-out")
    public List<RequestEventsOut> queryEvents(RequestEventsIn message) {

        // TODO: #e
        // TODO: #p
        // TODO: handle limit with panache paging?

        var queryBuilder = QueryBuilder.create()
            .addCriteria("id in :ids", message.filter.ids)
            .addCriteria("pubkey in :authors", message.filter.authors)
            .addCriteria("kind in :kinds", message.filter.kinds)
            .addCriteria("createdAt >= :since", message.filter.since)
            .addCriteria("createdAt <= :until", message.filter.until);

        List<Event> events;
        if (queryBuilder.getParameters().isEmpty()) {
            events = Event.listAll();
        } else {
            events =  Event.list(queryBuilder.getQuery(), queryBuilder.getParameters());
        }

        return events.stream()
            .map(event -> new RequestEventsOut("EVENT", message.subscriptionId, event))
            .collect(Collectors.toList());
    }
}
