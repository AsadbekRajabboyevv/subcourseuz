package uz.asadbek.subcourse.test.session;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.asadbek.base.dto.BaseResponseDto;
import uz.asadbek.subcourse.test.session.answer.dto.SubmitAnswerRequestDto;
import uz.asadbek.subcourse.test.session.dto.TestSessionResponseDto;
import uz.asadbek.subcourse.test.test.dto.TestResultDto;
import uz.asadbek.subcourse.test.test.dto.TestReviewDto;

@RequestMapping("/v1/api/test-sessions")
@Tag(
    name = "Test Sessions",
    description = "APIs for managing test solving sessions"
)
public interface TestSessionApi {

    @Operation(summary = "Start test session", description = "Creates a new test session and generates random questions")
    @PostMapping("/start/{testId}")
    BaseResponseDto<Long> start(
        @Parameter(description = "Test ID")
        @PathVariable Long testId
    );

    @Operation(summary = "Submit answer", description = "Submits or updates answer for session question")
    @PutMapping("/submit")
    BaseResponseDto<Boolean> submitAnswer(
        @RequestBody @Valid SubmitAnswerRequestDto request
    );

    @Operation(summary = "Finish test session", description = "Finishes session and calculates final result")
    @PostMapping("/finish/{sessionId}")
    BaseResponseDto<TestResultDto> finish(
        @Parameter(description = "Session ID")
        @PathVariable Long sessionId
    );

    @Operation(summary = "Get test review", description = "Returns completed session review")
    @GetMapping("/review/{sessionId}")
    BaseResponseDto<TestReviewDto> getReview(
        @Parameter(description = "Session ID")
        @PathVariable Long sessionId
    );

    @GetMapping("/{sessionId}")
    BaseResponseDto<TestSessionResponseDto> getSession(@PathVariable Long sessionId);
}
