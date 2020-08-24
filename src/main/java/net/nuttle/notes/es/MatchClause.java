package net.nuttle.notes.es;

import com.fasterxml.jackson.annotation.*;
import org.elasticsearch.index.query.*;

import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MatchClause extends Clause {

    Map<String,String> fieldAndValue;

    public MatchClause(String field, String value) {
        fieldAndValue = new HashMap<>();
        fieldAndValue.put(field, value);
    }

    @JsonAnyGetter
    public Map<String,String> getFieldAndValue() {
        return fieldAndValue;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private MatchClause clause;

        public Builder setFieldAndValue(String field, String value) {
            clause = new MatchClause(field, value);
            return this;
        }

        public MatchClause build() {
            return clause;
        }
    }
}
