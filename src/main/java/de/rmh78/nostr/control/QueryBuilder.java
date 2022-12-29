package de.rmh78.nostr.control;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class QueryBuilder {
    private static String QUERY_JOINER = " and ";

    private Map<String, String> queryConditions = new HashMap<>();
    private Map<String, Object> parameters = new HashMap<>();

    private QueryBuilder() {}

    public static QueryBuilder create() {
        return new QueryBuilder();
    }

    public QueryBuilder addCriteria(String queryCondition, Object parameterValue) {
        String parameterName = queryCondition.replaceAll(".*:(\\w+).*", "$1");

        if (parameterValue != null) {
            queryConditions.put(parameterName, queryCondition);
            parameters.put(parameterName, parameterValue);
        }

        return this;
    }

    public String getQuery() {
        return parameters.keySet().stream()
            .map(parameterName -> queryConditions.get(parameterName))
            .collect(Collectors.joining(QUERY_JOINER));
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }
}

