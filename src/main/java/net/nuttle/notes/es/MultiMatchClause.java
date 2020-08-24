package net.nuttle.notes.es;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MultiMatchClause {
    private String query;
    private List<String> fields;

    public MultiMatchClause() {}

    public MultiMatchClause(String query) {
        this.query = query;
    }

    @JsonProperty("query")
    public String getQuery() {
        return query;
    }

    @JsonProperty("query")
    public void setQuery(String query) {
        this.query = query;
    }

    @JsonProperty("fields")
    public List<String> getFields() {
        return fields;
    }

    @JsonProperty("fields")
    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private MultiMatchClause clause;

        private Builder() {
            clause = new MultiMatchClause();
        }

        public Builder setQuery(String query) {
            clause.setQuery(query);
            return this;
        }

        public Builder setFields(List<String> fields) {
            clause.setFields(fields);
            return this;
        }

        public Builder setFields(String... fields) {
            clause.setFields(Arrays.asList(fields));
            return this;
        }

        public MultiMatchClause build() {
            return clause;
        }
    }


}
