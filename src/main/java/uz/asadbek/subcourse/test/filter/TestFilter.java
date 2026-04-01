package uz.asadbek.subcourse.test.filter;

import lombok.Getter;
import lombok.Setter;
import uz.asadbek.base.filter.BaseFilter;

@Getter
@Setter
public class TestFilter extends BaseFilter {
    private String name;
    private String scienceName;
    private String lang;
    private Integer durationFrom;
    private Integer durationTo;
    private Boolean myTests;
    private Long gradeId;
    private Long priceFrom;
    private Long priceTo;

}
