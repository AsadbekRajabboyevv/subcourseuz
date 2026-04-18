package uz.asadbek.subcourse.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

@Converter
public class JsonStringListConverter implements AttributeConverter<List, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            return "[]";
        }
    }

    @Override
    public List convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, List.class);
        } catch (JsonProcessingException e) {
            return List.of();
        }
    }
}
