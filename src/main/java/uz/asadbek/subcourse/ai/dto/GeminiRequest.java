package uz.asadbek.subcourse.ai.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeminiRequest {
    private List<Content> contents;
    private GenerationConfig generationConfig;

    @Data
    @Builder
    public static class Content {
        private List<Part> parts;
    }

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Part {
        private String text;
        private InlineData inline_data;
    }

    @Data
    @Builder
    public static class InlineData {
        private String mime_type;
        private String data;
    }

    @Data
    @Builder
    public static class GenerationConfig {
        private String responseMimeType;
        private Double temperature;
    }
}
