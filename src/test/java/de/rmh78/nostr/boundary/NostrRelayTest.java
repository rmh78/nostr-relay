package de.rmh78.nostr.boundary;

import java.net.URI;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import org.jboss.logging.Logger;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class NostrRelayTest {

    @Inject
    Logger logger;

    private static final LinkedBlockingDeque<String> MESSAGES = new LinkedBlockingDeque<>();
    
    @TestHTTPResource("/")
    URI uri;  

    @Test
    public void testWebsocketChat() throws Exception {

        try (Session session = ContainerProvider.getWebSocketContainer().connectToServer(Client.class, uri)) {
            Assertions.assertEquals("CONNECT", MESSAGES.poll(10, TimeUnit.SECONDS));

            publishEvent(session, "000", 0, "first message");
            publishEvent(session, "001", 0, "hallo echo");

            Thread.sleep(500);

            requestEvents(session, "1234567890", List.of(0,1,2,7));
            validateRequestedEvent(session, "1234567890", "000", 0, "first message");
            validateRequestedEvent(session, "1234567890", "001", 0, "hallo echo");
            
            publishEvent(session, "002", 0, "second message");
            validateRequestedEvent(session, "1234567890", "002", 0, "second message");
        }
    }

    private void publishEvent(Session session, String id, int kind, String content) {
        var message = """
            [
                "EVENT", 
                {
                    "id":"%s",
                    "created_at":1671348685,
                    "kind":%s,
                    "content":"%s",
                    "tags":[
                        ["e", "111", ""],
                        ["p", "333", ""]
                    ]
                }
            ]
            """.formatted(id, kind, content);

        session.getAsyncRemote().sendText(message);
    }

    private void requestEvents(Session session, String subscriptionId, List<Integer> kinds) {
        var message = """
            [
                "REQ", 
                "%s", 
                {
                    "kinds":%s,
                    "#e":["111","222"],
                    "#p":["333","444"],
                    "since":1671348685,
                    "until":1671348685,
                    "limit":450
                }
            ]
            """.formatted(subscriptionId, kinds);

        session.getAsyncRemote().sendText(message);
    }

    private void validateRequestedEvent(Session session, String subscriptionId, String eventId, int kind, String content) throws JSONException, InterruptedException {
        var response = """
            [
                "EVENT",
                "%s",
                {
                    "id":"%s",
                    "pubkey":null,
                    "created_at":1671348685,
                    "kind":%s,
                    "content":"%s",
                    "sig":null,
                    "tags":[
                        ["e", "111", ""],
                        ["p", "333", ""]
                    ]
                }
            ]
            """.formatted(subscriptionId, eventId, kind, content);

        JSONAssert.assertEquals(response, MESSAGES.poll(10, TimeUnit.SECONDS), true);
    }

    @ClientEndpoint
    public static class Client {

        @OnOpen
        public void open(Session session) {
            MESSAGES.add("CONNECT");
        }

        @OnMessage
        void message(String msg) {
            MESSAGES.add(msg);
        }
    }
}
