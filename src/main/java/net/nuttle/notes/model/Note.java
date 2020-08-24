package net.nuttle.notes.model;

import com.fasterxml.jackson.annotation.*;
import org.slf4j.*;

import java.util.*;

@JsonPropertyOrder({"title", "keywords", "ticket", "filename", "fullpath", "content"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Note {
    private static final Logger logger = LoggerFactory.getLogger(Note.class);
    private String fullpath;
    private String filename;
    private String title;
    private List<String> contents;
    private List<String> keywords;
    private String ticket;
    private List<String> bigHeadings;
    private List<String> smallHeadings;

    public Note() {
        contents = new ArrayList<>();
    }

    @JsonProperty
    public void setFilename(String filename) {
        this.filename = filename;
    }

    @JsonProperty
    public String getFilename() {
        return filename;
    }

    @JsonProperty
    public void setFullpath(String fullpath) {
        this.fullpath = fullpath;
    }

    @JsonProperty
    public String getFullpath() {
        return fullpath;
    }

    @JsonProperty
    public List<String> getContents() {
        return contents;
    }

    public void addContent(String content) {
        contents.add(content);
    }

    @JsonProperty
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty
    public String getTitle() {
        return title;
    }

    @JsonProperty
    public List<String> getKeywords() {
        return keywords;
    }

    @JsonProperty
    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public void addKeyword(String keyword) {
        if (keywords == null) {
            keywords = new ArrayList<>();
        }
        keywords.add(keyword);
    }

    @JsonProperty
    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    @JsonProperty
    public String getTicket() {
        return ticket;
    }

    public void addBigHeading(String heading) {
        if (bigHeadings == null) {
            bigHeadings = new ArrayList<>();
        }
        bigHeadings.add(heading);
    }

    public void addSmallHeading(String heading) {
        if (smallHeadings == null) {
            smallHeadings = new ArrayList<>();
        }
        smallHeadings.add(heading);
    }

    @JsonProperty
    public List<String> getHeading1() {
        return bigHeadings;
    }

    @JsonProperty
    public List<String> getHeading2() {
        return smallHeadings;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Note)) {
            return false;
        }
        Note other = (Note) o;
        return other.hashCode() == hashCode();
    }

    @Override
    public int hashCode() {
        return fullpath.hashCode();
    }

}
