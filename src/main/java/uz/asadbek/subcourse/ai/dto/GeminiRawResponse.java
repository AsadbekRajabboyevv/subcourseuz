package uz.asadbek.subcourse.ai.dto;

import java.util.List;
import lombok.Data;

@Data
public class GeminiRawResponse {
    private List<Candidate> candidates;

    @Data
    public static class Candidate {
        private Content content;
    }

    @Data
    public static class Content {
        private List<Part> parts;
    }

    @Data
    public static class Part {
        private String text;
    }
}
