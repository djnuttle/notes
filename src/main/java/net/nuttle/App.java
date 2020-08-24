package net.nuttle;

import net.nuttle.notes.es.*;
import net.nuttle.notes.guice.*;
import net.nuttle.notes.io.FileUtil;
import net.nuttle.notes.json.*;
import net.nuttle.notes.model.*;
import net.nuttle.notes.parse.*;
import org.apache.commons.cli.*;
import org.elasticsearch.common.inject.*;
import org.slf4j.*;

import java.io.*;
import java.util.*;

/**
 * Hello world!
 *
 */
public class App 
{
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    public static final String INDEX_NAME = "notes";
    private Jsonifier jsonifier;
    private NoteParser parser;

    public static void main( String[] args ) throws ParseException
    {
        Injector injector = Guice.createInjector(new NotesModule());
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();
        options.addOption(new Option("i", "index", false, "Index data"));
        options.addOption(new Option(null, "maxDepth", true, "Max depth for notes files"));
        options.addOption(new Option("c", "check", false, "Check for index"));
        CommandLine cmd = parser.parse(options, args);
        App app = injector.getInstance(App.class);
        app.run(cmd);
    }

    @Inject
    public App(Jsonifier jsonifier, NoteParser parser) {
        this.jsonifier = jsonifier;
        this.parser = parser;
    }

    public void run(CommandLine cmd) {
        QueryWrapper q = new QueryWrapper();
        MultiMatchClause matchClause2 = new MultiMatchClause("test");
        MustMustNotShouldClause shouldClause = new MustMustNotShouldClause();
        shouldClause.setMultiMatchClause(matchClause2);

        BooleanClause boolClause = BooleanClause.builder()
                .addMustClause(MustMustNotShouldClause.builder()
                    .setMatchClause(MatchClause.builder()
                        .setFieldAndValue("ticket", "nt-105685")
                        .build())
                    .build())
                .addShouldClause(MustMustNotShouldClause.builder()
                    .setMultiMatchClause(MultiMatchClause.builder()
                        .setQuery("test")
                        .setFields("keywords", "content", "title")
                        .build())
                    .build())
                .build();

        q.getQuery().setBooleanClause(boolClause);
        logger.info(jsonifier.jsonifyPretty(q));
        if (true) {
            return;
        }
        try (ESUtil_7 es = new ESUtil_7_Impl()) {
            if (cmd.hasOption("c")) {
                logger.info("notes index exists: {}", es.isIndexExists(INDEX_NAME));
                return;
            }
            if (!es.isIndexExists(INDEX_NAME)) {
                logger.info("Creating index");
                es.createIndex(INDEX_NAME, App.class.getResourceAsStream("/settings.json"));
                es.createMappings(INDEX_NAME, App.class.getResourceAsStream("/mappings.json"));
            }
            if (cmd.hasOption("maxDepth")) {
                logger.info("max depth: " + cmd.getOptionValue("maxDepth"));
                FileUtil reader = new FileUtil(Integer.parseInt(cmd.getOptionValue("maxDepth")));
                List<File> files = reader.getFiles(new File("/Users/dan.nuttle/Documents/Notes"));
                List<Note> notes = new ArrayList<>(files.size());
                for (File file : files) {
                    Note note = parser.parse(file);
                    notes.add(note);
                }
                es.bulkUpsert(INDEX_NAME, notes);
                logger.info(files.size() + " files found");
            }
        } catch (Exception e) {
            logger.error("Error", e);
        }
    }
}
