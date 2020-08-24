package net.nuttle.notes.es;

import com.fasterxml.jackson.annotation.*;

public class QueryWrapper {
    private Query query;

    public QueryWrapper() {
        query = new Query();
    }

    @JsonProperty("query")
    public Query getQuery() {
        return query;
    }


}
