package ai;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import uz.asadbek.subcourse.SubcourseApplication;

@ActiveProfiles("test")
@SpringBootTest(classes = SubcourseApplication.class)
class AiServiceTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testGenerateByAi() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.pdf",
            "text/plain",
            "Savol: 2+2=? A) 4 B) 5".getBytes()
        );

        String configJson = """
            {
                "name": "Test",
                "count": 1,
                "duration": 10,
                "lang": "uz",
                "gradeId": 1,
                "scienceId": 1
            }
            """;
        MockMultipartFile requestPart = new MockMultipartFile(
            "request",
            "",
            "application/json",
            configJson.getBytes()
        );

        mockMvc.perform(multipart("/v1/api/ai/test-generate")
                .file(file)
                .file(requestPart))
            .andExpect(status().isOk());
    }
}
