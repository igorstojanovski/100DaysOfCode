package co.igorski.centralcommittee.serialization;

import co.igorski.centralcommittee.model.events.Event;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.util.Map;

public class EventDeserializer implements Deserializer<Event> {

    public static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public Event deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }

        try {
            String json = new String(data);
            System.out.println("JSON: " + json);
            return MAPPER.readValue(json, Event.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void close() {

    }
}
