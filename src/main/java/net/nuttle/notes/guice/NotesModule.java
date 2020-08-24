package net.nuttle.notes.guice;

import com.fasterxml.jackson.databind.*;
import net.nuttle.notes.json.*;
import org.elasticsearch.common.inject.*;

public class NotesModule extends AbstractModule {

    @Override
    public void configure() {
        bind(ObjectMapper.class).asEagerSingleton();;
        bind(Jsonifier.class).asEagerSingleton();
    }
}
