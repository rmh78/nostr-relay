package de.rmh78.nostr.entity;

import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

public class RequestMessageTest {

    @Inject
    Logger logger;

    @Test
    public void testJsonDeserialization() throws JsonProcessingException {
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

        var message = MessageRequest.fromJson(json);
        Assertions.assertEquals("REQ", message.type);
        Assertions.assertEquals("1234567890", message.subscriptionId);
        Assertions.assertNotNull(message.filter);
    }  
}
