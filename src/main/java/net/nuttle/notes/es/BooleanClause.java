package net.nuttle.notes.es;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BooleanClause {
    private List<Clause> mustChildClauses;
    private List<Clause> mustNotChildClauses;
    private List<Clause> shouldChildClauses;

    public BooleanClause() {
    }

    /*
    @JsonProperty("bool")
    public List<Clause> getInner() {
        return clauses;
    }

     */

    @JsonProperty("must")
    public void setMustChildClauses(List<Clause> mustChildClauses) {
        this.mustChildClauses = mustChildClauses;
    }

    public void addMustClause(Clause mustClause) {
        if (mustChildClauses == null) {
            mustChildClauses = new ArrayList<>();
        }
        mustChildClauses.add(mustClause);
    }

    @JsonProperty("must")
    public List<Clause> getMustChildClauses() {
        return mustChildClauses;
    }

    @JsonProperty("must_not")
    public void setMustNotChildClauses(List<Clause> mustNotChildClauses) {
        this.mustNotChildClauses = mustNotChildClauses;
    }

    public void addMustNotClause(Clause mustNotClause) {
        if (mustNotChildClauses == null) {
            mustNotChildClauses = new ArrayList<>();
        }
        mustNotChildClauses.add(mustNotClause);
    }

    @JsonProperty("must_not")
    public List<Clause> getMustNotChildClauses() {
        return mustNotChildClauses;
    }

    @JsonProperty("should")
    public void setShouldChildClauses(List<Clause> shouldChildClauses) {
        this.shouldChildClauses = shouldChildClauses;
    }

    public void addShouldClause(Clause shouldClause) {
        if (shouldChildClauses == null) {
            shouldChildClauses = new ArrayList<>();
        }
        shouldChildClauses.add(shouldClause);
    }

    @JsonProperty("should")
    public List<Clause> getShouldChildClauses() {
        return shouldChildClauses;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private BooleanClause clause;

        private Builder() {
            clause = new BooleanClause();
        }

        public Builder addShouldClause(MustMustNotShouldClause shouldClause) {
            clause.addShouldClause(shouldClause);
            return this;
        }

        public Builder addMustClause(MustMustNotShouldClause mustClause) {
            clause.addMustClause(mustClause);
            return this;
        }

        public Builder addMustNotClause(MustMustNotShouldClause mustNotClause) {
            clause.addMustNotClause(mustNotClause);
            return this;
        }

        public BooleanClause build() {
            return clause;
        }
    }
}
