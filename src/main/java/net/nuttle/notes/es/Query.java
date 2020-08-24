package net.nuttle.notes.es;

import com.fasterxml.jackson.annotation.*;

public class Query {
    private BooleanClause boolClause;

    @JsonProperty("bool")
    public void setBooleanClause(BooleanClause boolClause) {
        this.boolClause = boolClause;
    }

    @JsonProperty("bool")
    public BooleanClause getBooleanClause() {
        return boolClause;
    }
}
