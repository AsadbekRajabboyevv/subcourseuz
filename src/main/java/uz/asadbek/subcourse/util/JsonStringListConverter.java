package uz.asadbek.subcourse.util;

//@Converter
//public class JsonStringListConverter implements AttributeConverter<List, String> {
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Override
//    public String convertToDatabaseColumn(List attribute) {
//        try {
//            return objectMapper.writeValueAsString(attribute);
//        } catch (JsonProcessingException e) {
//            return "[]";
//        }
//    }
//
//    @Override
//    public List convertToEntityAttribute(String dbData) {
//        try {
//            return objectMapper.readValue(dbData, List.class);
//        } catch (JsonProcessingException e) {
//            return List.of();
//        }
//    }
//}
