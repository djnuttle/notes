package net.nuttle.notes.parse;

import net.nuttle.notes.model.*;
import org.apache.commons.io.*;
import org.apache.commons.lang3.*;
import org.slf4j.*;

import java.io.*;
import java.nio.charset.*;
import java.util.regex.*;

public class NoteParser {

    private static final Logger logger = LoggerFactory.getLogger(NoteParser.class);
    private static final Pattern KEYWORD_PATTERN = Pattern.compile("@\\[keywords=(.*?)\\]");
    private static final Pattern TITLE_PATTERN = Pattern.compile("@\\[title=(.*?)\\]");
    private static final Pattern TICKET_PATTERN = Pattern.compile("@\\[ticket=(.*?)\\]");
    private static final Pattern HEADING1_PATTERN = Pattern.compile("^# (.*)");
    private static final Pattern HEADING2_PATTERN = Pattern.compile("^#{2,} (.*)");

    public Note parse(File file) {
        Note note = new Note();
        note.setFullpath(file.getAbsolutePath());
        note.setFilename(file.getName());
        try {
            String[] lines = IOUtils.toString(new FileInputStream(file), StandardCharsets.UTF_8).split("\n");
            for (String line : lines) {
                boolean meta = false;
                line = line.trim();
                Matcher matcher = KEYWORD_PATTERN.matcher(line);
                if (matcher.find()) {
                    String keywordList = matcher.group(1);
                    for (String keyword : keywordList.split(" ")) {
                        note.addKeyword(keyword);
                    }
                    meta=true;
                }
                matcher = TITLE_PATTERN.matcher(line);
                if (matcher.find()) {
                    note.setTitle(matcher.group(1));
                    meta = true;
                }
                matcher = TICKET_PATTERN.matcher(line);
                if (matcher.find()) {
                    note.setTicket(matcher.group(1));
                    meta = true;
                }
                matcher = HEADING1_PATTERN.matcher(line);
                if (matcher.find()) {
                    note.addBigHeading(matcher.group(1));
                    meta = true;
                }
                matcher = HEADING2_PATTERN.matcher(line);
                if (matcher.find()) {
                    note.addSmallHeading(matcher.group(1));
                    meta = true;
                }
                if (meta) {
                    continue;
                }
                if (!StringUtils.isBlank(line)) {
                    note.addContent(line);
                }
            }
        } catch (Exception e) {

        }
        return note;
    }
}
