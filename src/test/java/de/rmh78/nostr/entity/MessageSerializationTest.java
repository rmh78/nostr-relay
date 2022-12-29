package de.rmh78.nostr.entity;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

public class MessageSerializationTest {

    Logger logger = Logger.getLogger(getClass());

    @Test
    public void requestEvents() throws JsonProcessingException {
        var json = """
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

        var message = RequestEventsIn.fromJson(json);
        Assertions.assertEquals("REQ", message.type);
        Assertions.assertEquals("1234567890", message.subscriptionId);
        Assertions.assertNotNull(message.filter);
    }

    @Test
    public void publishEvent() throws JsonProcessingException {
        var json = """
            [
                "EVENT", 
                {
                    "id":"ad328ed1-7f1b-42d0-94f6-625cc809a5d7",
                    "created_at":1671348685,
                    "kind":0,
                    "content":"hallo echo",
                    "tags":[
                        ["e", "123", ""],
                        ["p", "456", ""]
                    ]
                }
            ]
            """;

        var message = PublishEventIn.fromJson(json);
        Assertions.assertEquals("EVENT", message.type);
        Assertions.assertNotNull(message.event);
        Assertions.assertEquals("hallo echo", message.event.content);
        Assertions.assertEquals(2, message.event.tags.size());
    }
}
