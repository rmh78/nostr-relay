package de.rmh78.nostr.boundary;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.jboss.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.rmh78.nostr.entity.Event;
import de.rmh78.nostr.entity.Filter;
import de.rmh78.nostr.entity.MessageClose;
import de.rmh78.nostr.entity.MessageEvent;
import de.rmh78.nostr.entity.MessageRequest;

@ServerEndpoint("/")         
@ApplicationScoped
public class NostrRelay {
    
    @Inject
    Logger logger;

    @Inject
    ObjectMapper mapper;

    // TODO: create class
    Map<Session, ConcurrentMap<String, Filter>> subscribers = new ConcurrentHashMap<>(); 

    @OnOpen
    public void onOpen(Session session) {
        var subscriptionsWithFilters = new ConcurrentHashMap<String, Filter>();
        subscribers.put(session, subscriptionsWithFilters);
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
                case "EVENT" -> handleEvent(session, MessageEvent.fromJson(node.toString()));
                case "REQ" -> handleRequest(session, MessageRequest.fromJson(node.toString()));
                case "CLOSE" -> handleClose(session, MessageClose.fromJson(node.toString()));
                default -> handleUnknown(session, node.toString());
            }
        } catch (JsonProcessingException e) {
            logger.error("error on deserialization of JSON array", e);
        }
    }

    private void handleEvent(Session session, MessageEvent message) {
        logger.info(message);
    }

    private void handleRequest(Session session, MessageRequest message) throws JsonProcessingException {
        // store filter for subscription-id
        subscribers.get(session).put(message.subscriptionId, message.filter);

        // query events by filter
        var events = queryEvents(message.filter);

        // send events to 
        sendEvents(session, events);
    }

    private void handleClose(Session session, MessageClose message) {
        logger.info(message);
    }

    private void handleUnknown(Session session, String message) {
        logger.error(message);
    }

    private List<Event> queryEvents(Filter filter) {
        // TODO: query database
        var event = new Event();
        return Arrays.asList(event);
    }

    private void sendEvents(Session session, List<Event> events) {
        events.stream().forEach(event -> {
            try {
                var jsonOut = mapper.writeValueAsString(event);
                session.getAsyncRemote().sendText(jsonOut);    
            } catch (JsonProcessingException e) {
                logger.error(e);
            }
        });
    }
}
