package de.rmh78.nostr.boundary;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.rmh78.nostr.entity.Event;
import de.rmh78.nostr.entity.CloseSubscriptionIn;
import de.rmh78.nostr.entity.PublishEventIn;
import de.rmh78.nostr.entity.RequestEventsIn;
import de.rmh78.nostr.entity.RequestEventsOut;
import de.rmh78.nostr.entity.Subscription;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.subscription.Cancellable;

@ServerEndpoint("/")         
@ApplicationScoped
public class NostrRelay {
    
    @Inject
    Logger logger;

    @Inject
    ObjectMapper mapper;

    @Inject
    @Channel("publish-event-in")
    Emitter<Event> publishEventEmitter;

    @Inject
    @Channel("request-events-in")
    Emitter<RequestEventsIn> requestEventsEmitter;

    @Inject
    @Channel("request-events-out")
    Multi<List<RequestEventsOut>> requestedEventsStream;

    private Map<Session, List<Subscription>> subscribers = new ConcurrentHashMap<>();
    private Cancellable cancellable;

    @OnOpen
    public void onOpen(Session session) {
        subscribers.put(session, new ArrayList<Subscription>());
    }

    @OnClose
    public void onClose(Session session) {
        subscribers.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.error(throwable);
        subscribers.remove(session);
    }

    @OnMessage
    public void onMessage(Session session, String messageIn) {
        JsonNode node;
        try {
            node = mapper.readTree(messageIn);
        } catch (JsonProcessingException e) {
            logger.error("message is not a valid JSON format", e);
            return;
        }

        if (!node.isArray() && node.size() >= 2) {
            logger.error("message is not a valid JSON array");
            return;
        }

        String messageType = node.get(0).asText();
        try {
            switch (messageType) {
                case "EVENT" -> handleEvent(session, PublishEventIn.fromJson(node.toString()));
                case "REQ" -> handleRequest(session, RequestEventsIn.fromJson(node.toString()));
                case "CLOSE" -> handleClose(session, CloseSubscriptionIn.fromJson(node.toString()));
                default -> handleUnknown(session, node.toString());
            }
        } catch (JsonProcessingException e) {
            logger.error("error on deserialization of JSON array", e);
        }
    }

    @PostConstruct
    public void subscribe() {
        cancellable = requestedEventsStream.subscribe().with(events -> sendEvents(events));
    }

    @PreDestroy
    public void cleanup() {
        cancellable.cancel();
    }

    private void handleEvent(Session session, PublishEventIn message) {
        publishEventEmitter.send(message.event);
        sendEventToOthers(message.event.id);
    }

    private void sendEventToOthers(String eventId) {
        for (Session session : subscribers.keySet()) {
            var subscriptions = subscribers.get(session);

            for (Subscription subscription : subscriptions) {
                var filterCopy = subscription.filter.copy();
                filterCopy.ids = List.of(eventId);
                RequestEventsIn message = new RequestEventsIn("REQ", subscription.id, filterCopy);
                requestEventsEmitter.send(message);
            }
        }
    }

    private void handleRequest(Session session, RequestEventsIn message) throws JsonProcessingException {
        logger.info(message);
        subscribers.get(session).add(new Subscription(message.subscriptionId, message.filter));
        requestEventsEmitter.send(message);
    }

    private void handleClose(Session session, CloseSubscriptionIn message) {
        logger.info(message);
    }

    private void handleUnknown(Session session, String message) {
        logger.error(message);
    }

    private void sendEvents(List<RequestEventsOut> events) {
        events.stream().forEach(event -> {

            Session mySession = getSessionForSubscriptionId(event.subscriptionId);
            if (mySession == null) {
                return;
            }

            try {
                var jsonOut = mapper.writeValueAsString(event);
                mySession.getAsyncRemote().sendText(jsonOut);    
            } catch (JsonProcessingException e) {
                logger.error("error on serialization of Event", e);
            }
        });
    }

    private Session getSessionForSubscriptionId(String subscriptionId) {
        for (Session session : subscribers.keySet()) {
            var subscriptions = subscribers.get(session);
            if (subscriptions.stream().anyMatch(s -> s.id.equals(subscriptionId))) {
                return session;
            }
        }

        return null;
    }
}
