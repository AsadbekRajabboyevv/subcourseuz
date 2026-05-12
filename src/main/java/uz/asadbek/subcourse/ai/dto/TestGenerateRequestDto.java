package uz.asadbek.subcourse.ai.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TestGenerateRequestDto {

    @NotNull
    private String name;
    private String description;
    @NotNull
    private Integer duration;
    private Integer count;
    private Long price;
    private Boolean isPublished;
    @NotNull
    private String lang;
    @NotNull
    private Long gradeId;
    @NotNull
    private Long scienceId;
}
