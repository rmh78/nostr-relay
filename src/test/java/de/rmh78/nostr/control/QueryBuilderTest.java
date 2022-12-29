package de.rmh78.nostr.control;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class QueryBuilderTest {
    @Test
    public void simpleQueryWithValue() {
        var builder = QueryBuilder.create().addCriteria("identifier = :id", 123);
        Assertions.assertEquals("identifier = :id", builder.getQuery());
        Assertions.assertEquals(1, builder.getParameters().size());
        Assertions.assertEquals("[id]", builder.getParameters().keySet().toString());
        Assertions.assertEquals("[123]", builder.getParameters().values().toString());
    }

    @Test
    public void simpleQueryWithoutValue() {
        var builder = QueryBuilder.create().addCriteria("identifier = :id", null);
        Assertions.assertEquals("", builder.getQuery());
        Assertions.assertEquals(0, builder.getParameters().size());
    }

    @Test
    public void complexQuery() {
        var builder = QueryBuilder.create()
            .addCriteria("identifier = :id", 123)
            .addCriteria("name = :name", null)
            .addCriteria("age = :age", 45);
            
        Assertions.assertEquals("identifier = :id and age = :age", builder.getQuery());
        Assertions.assertEquals(2, builder.getParameters().size());
        Assertions.assertEquals("[id, age]", builder.getParameters().keySet().toString());
        Assertions.assertEquals("[123, 45]", builder.getParameters().values().toString());
    }
}
