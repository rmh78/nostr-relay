package de.rmh78.nostr;

import java.net.URI;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class NostrRelayTest {

    private static final LinkedBlockingDeque<String> MESSAGES = new LinkedBlockingDeque<>();
    
    @TestHTTPResource("/")
    URI uri;

    @Test
    public void testWebsocketChat() throws Exception {

        try (Session session = ContainerProvider.getWebSocketContainer().connectToServer(Client.class, uri)) {
            Assertions.assertEquals("CONNECT", MESSAGES.poll(10, TimeUnit.SECONDS));

            // send first request
            var request = """
                [
                    "REQ", 
                    "1234567890", 
                    {
                        "kinds":[0,1,2,7],
                        "since":1671348685,
                        "limit":450
                    }
                ]
                """;
            session.getAsyncRemote().sendText(request);
            var response = """
                {
                    "id":"0",
                    "pubkey":"abc",
                    "createdAt":1000,
                    "kind":0,
                    "tags":null,
                    "content":null,
                    "sig":null
                }
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
