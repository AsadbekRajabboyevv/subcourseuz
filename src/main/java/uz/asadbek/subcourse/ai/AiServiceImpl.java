package uz.asadbek.subcourse.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.subcourse.ai.dto.GeminiRawResponse;
import uz.asadbek.subcourse.ai.dto.GeminiRequest;
import uz.asadbek.subcourse.ai.dto.GeminiTestGenerateResponseDto;
import uz.asadbek.subcourse.ai.dto.TestGenerateRequestDto;
import uz.asadbek.subcourse.exception.BadRequestException;
import uz.asadbek.subcourse.filestorage.FileStorageService;
import uz.asadbek.subcourse.filestorage.dto.FileUploadOptions;
import uz.asadbek.subcourse.test.test.TestEntity;
import uz.asadbek.subcourse.test.test.TestService;
import uz.asadbek.subcourse.util.ExceptionUtil;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiServiceImpl implements AiService {

    private final FileStorageService fileStorageService;
    @Value("${app.gemini.api-key}")
    private String geminiApiKey;

    @Value("${app.gemini.api-path}")
    private String geminiApiPath;

    @Value("${app.gemini.prompt.generate-test}")
    private String generateTestPrompt;

    private final static String GEMINI_API_KEY_HEADER_NAME = "x-goog-api-key";

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    private final TestService testService;

    @Override
    public void testGenerate(MultipartFile file,
        MultipartFile mainImage,
        TestGenerateRequestDto request) {

        String mainImageUrl = null;
        if (mainImage != null) {
            mainImageUrl  = fileStorageService.upload(mainImage, FileUploadOptions.TEST_IMAGE).getUrl();
        }
        var newTest = initializeTestEntity(request, mainImageUrl);
        testService.save(newTest);
        try {
            var base64File = Base64.getEncoder().encodeToString(file.getBytes());

            var geminiRequest = GeminiRequest.builder()
                .contents(List.of(
                    GeminiRequest.Content.builder()
                        .parts(List.of(
                            GeminiRequest.Part.builder().text(generateTestPrompt).build(),
                            GeminiRequest.Part.builder()
                                .inline_data(GeminiRequest.InlineData.builder()
                                    .mime_type(file.getContentType())
                                    .data(base64File)
                                    .build())
                                .build()
                        )).build()
                ))
                .generationConfig(GeminiRequest.GenerationConfig.builder()
                    .responseMimeType(MediaType.APPLICATION_JSON_VALUE)
                    .temperature(0.1)
                    .build())
                .build();

            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set(GEMINI_API_KEY_HEADER_NAME, geminiApiKey);

            var entity = new HttpEntity<>(geminiRequest, headers);
            var response = restTemplate.postForEntity(geminiApiPath, entity,
                GeminiRawResponse.class);

            var rawBody = Objects.requireNonNull(response.getBody());
            var aiJsonResponse = rawBody.getCandidates().getFirst().getContent().getParts()
                .getFirst().getText();

            log.info("Gemini response: {}", aiJsonResponse);

            var generatedTest = objectMapper.readValue(aiJsonResponse, GeminiTestGenerateResponseDto.class);
            testService.saveAiGeneratedTest(generatedTest, newTest.getId());

        } catch (Exception e) {
            log.error("Error generating test: {}", ExceptionUtils.getStackTrace(e));
            throw ExceptionUtil.build(BadRequestException.class, "error.ai.test_generate_error");
        }
    }

    private static TestEntity initializeTestEntity(TestGenerateRequestDto request, String mainImageUrl) {
        var newTest = new TestEntity();
        newTest.setCount(request.getCount());
        newTest.setName(request.getName());
        newTest.setDescription(request.getDescription());
        newTest.setDuration(request.getDuration());
        newTest.setPrice(request.getPrice());
        newTest.setLang(request.getLang());
        newTest.setIsPublished(request.getIsPublished());
        newTest.setGradeId(request.getGradeId());
        newTest.setScienceId(request.getScienceId());
        newTest.setImagePath(mainImageUrl);
        return newTest;
    }
}
