package net.nuttle.notes.model;

import com.fasterxml.jackson.annotation.*;

public class Doc {
    private Note note;


    public Doc(Note note) {
        this.note = note;
    }

    @JsonProperty("doc_as_upsert")
    public String getDocAsUpsert() {
        return "true";
    }



    @JsonProperty("doc")
    public Note getDoc() {
        return note;
    }

}
