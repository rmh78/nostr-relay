package de.rmh78.nostr.boundary;

import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.rmh78.nostr.entity.Event;
import de.rmh78.nostr.entity.Tag;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.search.CreateArgs;
import io.quarkus.redis.datasource.search.FieldType;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.json.JsonObject;

@QuarkusTest
public class RedisTestResource {

    @Inject
    Logger logger;
    
    @Inject
    RedisDataSource ds;

    @Test
    public void testStoreAndSearchJson() {
        var jsonCommands = ds.json();
        var searchCommands = ds.search(Event.class);

        searchCommands.ftCreate("eventFilterIndex", 
            new CreateArgs()
                .onJson()
                .prefixes("event:")
                .indexedField("$.id", "id", FieldType.TEXT)
                .indexedField("$.kind", "kind", FieldType.NUMERIC)
                .indexedField("$.content", "content", FieldType.TEXT)
                .indexedField("$.tags[?(@[0]=='e')][1]", "etags", FieldType.TEXT)
                .indexedField("$.tags[?(@[0]=='p')][1]", "ptags", FieldType.TEXT));
        
        var e1 = new Event("111", "pubkey", 1671348685, Event.KIND_TEXT_NOTE, "hello world", null);
        e1.tags.add(new Tag("p", "xyz", null));
        
        var e2 = new Event("222", "pubkey", 1671348685, Event.KIND_TEXT_NOTE, "hello echo", null);
        e2.tags.add(new Tag("e", "999", null));

        logger.info(JsonObject.mapFrom(e1));
                
        jsonCommands.jsonSet("event:" + e1.id, JsonObject.mapFrom(e1));
        jsonCommands.jsonSet("event:" + e2.id, JsonObject.mapFrom(e2));

        var response1 = searchCommands.ftSearch("eventFilterIndex", "@kind:[0,1] @ptags:(xyz)");
        Assertions.assertEquals(1, response1.count());
        Assertions.assertEquals("event:111", response1.documents().get(0).key());

        var response2 = searchCommands.ftSearch("eventFilterIndex", "@kind:[0,1] @etags:(999)");
        Assertions.assertEquals(1, response2.count());
        Assertions.assertEquals("event:222", response2.documents().get(0).key());
    }
}
