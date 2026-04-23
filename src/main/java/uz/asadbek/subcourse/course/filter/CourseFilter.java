package uz.asadbek.subcourse.course.filter;

import lombok.Data;
import uz.asadbek.base.filter.BaseFilter;
import uz.asadbek.subcourse.course.dto.DurationType;

@Data
public class CourseFilter extends BaseFilter {

    private String name;
    private String search;
    private Long gradeId;
    private Long scienceId;
    private Long priceTo;
    private Long priceFrom;
    private String lang;
    private DurationType durationType;
    private Integer duration;
    private Boolean isPublished;
}
