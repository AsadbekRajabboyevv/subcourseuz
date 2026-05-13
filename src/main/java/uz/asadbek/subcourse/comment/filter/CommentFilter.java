package uz.asadbek.subcourse.comment.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import uz.asadbek.base.filter.BaseFilter;

@Data
@EqualsAndHashCode(callSuper = true)
public class CommentFilter extends BaseFilter {

    private String courseSlug;
    private Long lessonId;
    private Long testId;
    private Integer ratingFrom;
    private Integer ratingTo;
}
