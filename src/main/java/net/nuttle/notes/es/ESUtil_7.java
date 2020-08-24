package net.nuttle.notes.es;

import net.nuttle.notes.model.*;
import org.elasticsearch.action.search.*;

import java.io.*;
import java.util.*;

public interface ESUtil_7 extends AutoCloseable {
    SearchResponse search(String index, String query) throws SearchException;

    String fetch(String index, String noteid) throws SearchException;

    Note fetchNote(String index, String noteid) throws SearchException;

    void indexTestNotes(String index) throws SearchException;

    void index(String index, Note note) throws SearchException;

    void upsert(String index, Note note) throws SearchException;

    void bulkUpsert(String index, List<Note> notes) throws SearchException;

    boolean createIndex(String index, InputStream settingsStream) throws SearchException;

    void createMappings(String index, File mappingsFile) throws SearchException;

    void createMappings(String index, InputStream mappingsStream) throws SearchException;

    void createAlias(String index, String alias) throws SearchException;

    boolean isIndexExists(String index) throws SearchException;
}
