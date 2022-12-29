package de.rmh78.nostr.boundary;

import java.net.URI;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import org.jboss.logging.Logger;
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

            var request1 = """
                [
                    "EVENT", 
                    {
                        "id":"ad328ed1-7f1b-42d0-94f6-625cc809a5d7",
                        "created_at":1671348685,
                        "kind":0,
                        "content":"hallo echo",
                        "tags":[
                            ["e", "111", ""],
                            ["p", "333", ""]
                        ]
                    }
                ]
                """;
            session.getAsyncRemote().sendText(request1);

            var request2 = """
                [
                    "REQ", 
                    "1234567890", 
                    {
                        "kinds":[0,1,2,7],
                        "#e":["111","222"],
                        "#p":["333","444"],
                        "since":1671348685,
                        "until":1671348685,
                        "limit":450
                    }
                ]
                """;
            session.getAsyncRemote().sendText(request2);
            var response = """
                [
                    "EVENT",
                    "1234567890",
                    {
                        "id":"ad328ed1-7f1b-42d0-94f6-625cc809a5d7",
                        "pubkey":null,
                        "created_at":1671348685,
                        "kind":0,
                        "content":"hallo echo",
                        "sig":null,
                        "tags":[
                            ["e", "111", ""],
                            ["p", "333", ""]
                        ]
                    }
                ]
                """;
            JSONAssert.assertEquals(response, MESSAGES.poll(10, TimeUnit.SECONDS), true);
            
            //session.getAsyncRemote().sendText("[]");
            //Assertions.assertEquals(json, MESSAGES.poll(10, TimeUnit.SECONDS));
        }
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
