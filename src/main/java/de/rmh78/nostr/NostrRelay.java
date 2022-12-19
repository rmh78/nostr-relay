package de.rmh78.nostr;

import java.util.ArrayList;
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

@ServerEndpoint("/")         
@ApplicationScoped
public class NostrRelay {
    
    @Inject
    Logger logger;

    @Inject
    ObjectMapper mapper;

    Map<Session, ConcurrentMap<String, List<String>>> subscribers = new ConcurrentHashMap<>(); 

    @OnOpen
    public void onOpen(Session session) {
        var subscriptionsWithFilters = new ConcurrentHashMap<String, List<String>>();
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
    public void onMessage(Session session, String messageIn) throws JsonProcessingException {
        JsonNode jsonArray = mapper.readTree(messageIn);
        String messageType = jsonArray.get(0).asText();
        switch (messageType) {
            case "EVENT" -> handleEvent(session, jsonArray);
            case "REQ" -> handleRequest(session, jsonArray);
            case "CLOSE" -> handleClose(session, jsonArray);
            default -> handleUnknown(session, jsonArray);
        }
    }

    private void handleEvent(Session session, JsonNode jsonArray) {
        logger.info(jsonArray);
    }

    private void handleRequest(Session session, JsonNode jsonArray) throws JsonProcessingException {
        logger.info(jsonArray);

        var subscriptionId = jsonArray.get(1).asText();

        // TODO: extract filter out of json array
        var filter = new ArrayList<String>();

        // store filter for subscription-id
        subscribers.get(session).put(subscriptionId, filter);

        var events = queryEvents(filter);
        sendEvents(session, events);
    }

    private void handleClose(Session session, JsonNode jsonArray) {
        logger.info(jsonArray);
    }

    private void handleUnknown(Session session, JsonNode jsonArray) {
        logger.error(jsonArray);
    }

    private List<Event> queryEvents(List<String> filter) {
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
