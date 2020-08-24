package net.nuttle.notes.json;

import com.fasterxml.jackson.databind.*;
import org.elasticsearch.common.inject.*;
import org.slf4j.*;

public class Jsonifier {
    private ObjectMapper mapper;
    private static final Logger logger = LoggerFactory.getLogger(Jsonifier.class);

    public String jsonify(Object o) {
        try {
            return mapper.writeValueAsString(mapper.valueToTree(o));
        } catch (Exception e) {
            logger.warn("Error in jsonify", e);
            return "";
        }
    }

    public String jsonifyPretty(Object o) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mapper.valueToTree(o));
        } catch (Exception e) {
            logger.warn("Error in jsonify", e);
            return "";
        }

    }


    @Inject
    public Jsonifier(ObjectMapper mapper) {
        this.mapper = mapper;
    }
}
