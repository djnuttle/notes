package net.nuttle.notes.es;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import net.nuttle.notes.model.*;
import org.apache.commons.io.*;
import org.apache.commons.lang3.*;
import org.apache.http.*;
import org.apache.http.client.config.*;
import org.apache.http.entity.*;
import org.elasticsearch.action.bulk.*;
import org.elasticsearch.action.get.*;
import org.elasticsearch.action.index.*;
import org.elasticsearch.action.search.*;
import org.elasticsearch.action.update.*;
import org.elasticsearch.client.*;
import org.elasticsearch.common.xcontent.*;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.builder.*;
import org.slf4j.*;

import java.io.*;
import java.nio.charset.*;
import java.util.*;

public class ESUtil_7_Impl implements ESUtil_7, AutoCloseable {

    //public static final String TYPE = "note";
    private static final Logger LOG = LoggerFactory.getLogger(ESUtil_7_Impl.class);
    private final ObjectMapper mapper;
    private RestHighLevelClient highLevelClient;
    private RestClient lowLevelClient;
    //private MarkdownRenderer renderer;

    //@Autowired
    public ESUtil_7_Impl(/*MarkdownRenderer renderer*/) {
        //this.renderer = renderer;
        mapper = new ObjectMapper();
        highLevelClient = getClient();
        lowLevelClient = getLowLevelClient();
    }

    @Override
    public void close() throws Exception {
        highLevelClient.close();
        lowLevelClient.close();

    }

    @Override
    public SearchResponse search(String index, String query) throws SearchException {
        try {
            SearchRequest req = new SearchRequest(index);
            SearchSourceBuilder builder = new SearchSourceBuilder();
            builder.query(QueryBuilders.queryStringQuery(query));
            req.source(builder);
            RequestOptions options = RequestOptions.DEFAULT;
            return highLevelClient.search(req, options);
        } catch (IOException e) {
            throw new SearchException("Error searching", e);
        }
    }

    //@Override
    public String fetch(String index, String id) throws SearchException {
        try {
            GetRequest req = new GetRequest(index, id);
            RequestOptions options = RequestOptions.DEFAULT;
            GetResponse resp = highLevelClient.get(req, options);
            String note = (String) resp.getSource().get("note");
            return null;
            //return renderer.renderToHTML(note);
        } catch (IOException e) {
            throw new SearchException("Error fetching note", e);
        }
    }

    @Override
    public Note fetchNote(String index, String id) throws SearchException {
        try {
            GetRequest req = new GetRequest(index, id);
            RequestOptions options = RequestOptions.DEFAULT;
            GetResponse resp = highLevelClient.get(req, options);
            String strNote = (String) resp.getSourceAsString();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(strNote);
            return mapper.treeToValue(node, Note.class);
        } catch (IOException e) {
            throw new SearchException("Error fetching note", e);
        }
    }

    @Override
    public void bulkUpsert(String index, List<Note> notes) throws SearchException {
        try {
            BulkRequest bulkReq = new BulkRequest();
            for (Note note : notes) {
                String json = mapper.writeValueAsString((mapper.valueToTree(note)));
                LOG.info(json);
                UpdateRequest req = new UpdateRequest(index, String.valueOf(note.hashCode()));
                req.doc(json, XContentType.JSON);
                req.upsert(json, XContentType.JSON);
                bulkReq.add(req);
            }
            RequestOptions options = RequestOptions.DEFAULT;
            highLevelClient.bulk(bulkReq, options);
        } catch (IOException e) {
            throw new SearchException("Error bulk upserting notes", e);
        }
    }

    @Override
    public void upsert(String index, Note note) throws SearchException {
        try {
            UpdateRequest req = new UpdateRequest(index, String.valueOf(note.hashCode()));
            String json = mapper.writeValueAsString((mapper.valueToTree(note)));
            LOG.info(json);
            req.doc(json, XContentType.JSON);
            RequestOptions options = RequestOptions.DEFAULT;
            req.upsert(json, XContentType.JSON);
            highLevelClient.update(req, options);
        } catch (IOException e) {
            throw new SearchException("Error updating note", e);
        }
    }

    @Override
    public void index(String index, Note note) throws SearchException {
        try {
            JsonNode node = mapper.valueToTree(note);
            IndexRequest req = new IndexRequest(index);
            req = req.id(String.valueOf(note.hashCode()));
            req.source(mapper.writeValueAsString(node), XContentType.JSON);
            RequestOptions options = RequestOptions.DEFAULT;
            IndexResponse resp = highLevelClient.index(req, options);
            LOG.info("Indexed ID: " + resp.getId());
        } catch (IOException e) {
            throw new SearchException("Error indexing note", e);
        }
    }

    public void indexTestNotes(String index) throws SearchException {
        try {
            String jsonFile = System.getProperty("user.dir") + "/src/main/resources/notes.json";
            ArrayNode notes = (ArrayNode) mapper.readTree(new File(jsonFile));
            for (JsonNode node : notes) {
                IndexRequest req = new IndexRequest(index);
                req.source(mapper.writeValueAsString(node), XContentType.JSON);
                RequestOptions options = RequestOptions.DEFAULT;
                IndexResponse resp = highLevelClient.index(req, options);
                LOG.info("Indexed ID: " + resp.getId());
            }
        } catch (IOException e) {
            throw new SearchException("Error indexing test notes", e);
        }
        return;
    }

    @Override
    public boolean createIndex(String index, InputStream settingsStream) throws SearchException {
        try {
            if (isIndexExists(lowLevelClient, index)) {
                LOG.info("Index already exists");
                return false;
            }
            Request request = new Request("PUT", "/" + index);
            StringEntity entity = new StringEntity(IOUtils.toString(settingsStream, StandardCharsets.UTF_8), ContentType.APPLICATION_JSON);
            request.setEntity(entity);
            Response resp = lowLevelClient.performRequest(request);
            LOG.info("Status code: " + resp.getStatusLine().getStatusCode());
            if (resp.getStatusLine().getStatusCode() < 400) {
                return true;
            }
            return false;
        } catch (IOException e) {
            throw new SearchException("Error creating index", e);
        }
    }

    @Override
    public void createMappings(String index, File mappingsFile) throws SearchException {
        try {
            createMappings(index, IOUtils.toString(new FileInputStream(mappingsFile), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new SearchException("Error creating mappings", e);
        }
    }

    @Override
    public void createMappings(String index, InputStream mappingsStream) throws SearchException {
        try {
            String mappings = IOUtils.toString(mappingsStream, StandardCharsets.UTF_8);
            createMappings(index, mappings);
        } catch (IOException e) {
            throw new SearchException("Error creating mappings", e);
        }
    }

    private void createMappings(String index, String mappings) throws IOException {
        try (RestClient client = getLowLevelClient()) {
            StringEntity entity = new StringEntity(mappings, ContentType.APPLICATION_JSON);
            Request request = new Request("PUT", "/" + index + "/_mappings");
            request.setEntity(entity);
            client.performRequest(request);
        }
    }

    @Override
    public void createAlias(String index, String alias) throws SearchException {
        return;
    }

    private boolean isIndexExists(RestClient client, String index) throws SearchException {
        try {
            Request request = new Request("GET", "/_cat/indices?format=json");
            Response resp = client.performRequest(request);
            StringWriter writer = new StringWriter    ();
            IOUtils.copy(resp.getEntity().getContent(), writer, "utf-8");
            ObjectMapper mapper = new ObjectMapper();
            ArrayNode indices = (ArrayNode) mapper.readTree(writer.toString());
            for (JsonNode node : indices) {
                if (node.get("index").asText().equals(index)) {
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            throw new SearchException("Error checking index", e);
        }
    }

    @Override
    public boolean isIndexExists(String index) throws SearchException {
        try (RestClient client = getLowLevelClient()) {
            return isIndexExists(client, index);
        } catch (IOException e) {
            throw new SearchException("Error checking index", e);
        }
    }

    public RestHighLevelClient getClient() {
        return new RestHighLevelClient(getRestClientBuilder());
    }

    public RestClient getLowLevelClient() {
        return getRestClientBuilder().build();
    }

    public RestClientBuilder getRestClientBuilder() {
        String host = StringUtils.defaultIfBlank(System.getenv("NOTES_ES_HOST"), System.getenv("DISCOVERY_HOST"));
        int port = Integer.parseInt(StringUtils.defaultIfBlank(System.getenv("NOTES_ES_PORT"), "9200"));
        String scheme = StringUtils.defaultIfBlank(System.getenv("NOTES_ES_SCHEME"), "http");
        return RestClient.builder(new HttpHost(host, port, scheme)).setRequestConfigCallback(
                new RestClientBuilder.RequestConfigCallback() {
                    @Override
                    public RequestConfig.Builder customizeRequestConfig(
                            RequestConfig.Builder requestConfigBuilder) {
                        return requestConfigBuilder
                                .setConnectTimeout(5000)
                                .setSocketTimeout(60000);
                    }
                });
    }
}
