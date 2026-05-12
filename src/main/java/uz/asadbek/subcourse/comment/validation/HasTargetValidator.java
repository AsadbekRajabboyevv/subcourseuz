package uz.asadbek.subcourse.comment.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import uz.asadbek.subcourse.comment.dto.CommentRequestDto;

public class HasTargetValidator implements ConstraintValidator<HasTarget, CommentRequestDto> {

    @Override
    public boolean isValid(CommentRequestDto dto, ConstraintValidatorContext ctx) {
        if (dto == null) return true;
        return dto.getCourseSlug() != null
            || dto.getLessonId() != null
            || dto.getTestId() != null;
    }
}
