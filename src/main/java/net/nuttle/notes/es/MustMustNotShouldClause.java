package net.nuttle.notes.es;

import com.fasterxml.jackson.annotation.*;
import com.sun.org.apache.xpath.internal.operations.*;

import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MustMustNotShouldClause extends Clause {
    private MatchClause matchClause;
    private MultiMatchClause multiMatchClause;

    @JsonProperty("match")
    public MatchClause getMatchClause() {
        return matchClause;
    }

    @JsonProperty("match")
    public void setMatchClause(MatchClause matchClause) {
        this.matchClause = matchClause;
    }

    @JsonProperty("multi_match")
    public MultiMatchClause getMultiMatchClause() {
        return multiMatchClause;
    }

    @JsonProperty("multi_match")
    public void setMultiMatchClause(MultiMatchClause multiMatchClause) {
        this.multiMatchClause = multiMatchClause;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private MustMustNotShouldClause clause;

        private Builder() {
            clause = new MustMustNotShouldClause();
        }

        public Builder setMatchClause(MatchClause matchClause) {
            clause.setMatchClause(matchClause);
            return this;
        }

        public Builder setMultiMatchClause(MultiMatchClause multiMatchClause) {
            clause.setMultiMatchClause(multiMatchClause);
            return this;
        }

        public MustMustNotShouldClause build() {
            return clause;
        }
    }
}
