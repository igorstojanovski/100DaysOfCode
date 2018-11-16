package co.igorski.centralcommittee.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class EventSerializer implements Serializer {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void configure(Map configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String topic, Object data) {
        try {
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(data).getBytes();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    @Override
    public void close() {

    }
}
