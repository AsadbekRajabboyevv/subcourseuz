package uz.asadbek.subcourse.util.annotation.existindb;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Path;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.springframework.core.GenericTypeResolver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

@Component
public class ExistInDbValidator implements ConstraintValidator<ExistsInDb, Long> {

    private final Map<String, JpaRepository<?, Long>> repositories;
    private JpaRepository<?, Long> cachedRepository;
    private boolean setNull;
    private boolean optional;

    public ExistInDbValidator(Map<String, JpaRepository<?, Long>> repositories) {
        this.repositories = repositories;
    }

    @Override
    public void initialize(ExistsInDb annotation) {
        this.setNull = annotation.setNull();
        this.optional = annotation.optional();
        this.cachedRepository = getRepositoryByEntity(annotation.entity());
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if (value == null) {
            return optional;
        }

        if (cachedRepository == null) {
            return false;
        }

        boolean exists = cachedRepository.existsById(value);

        if (!exists) {
            if (setNull) {
                return trySetFieldToNull(context);
            }
            return false;
        }

        return true;
    }

    private boolean trySetFieldToNull(ConstraintValidatorContext context) {
        try {
            ConstraintValidatorContextImpl hibernateContext = context.unwrap(ConstraintValidatorContextImpl.class);

            Method getRootBeanMethod = ConstraintValidatorContextImpl.class.getDeclaredMethod("getRootBean");
            getRootBeanMethod.setAccessible(true);
            Object rootBean = getRootBeanMethod.invoke(hibernateContext);

            Method getPropertyPathMethod = ConstraintValidatorContextImpl.class.getDeclaredMethod("getPropertyPath");
            getPropertyPathMethod.setAccessible(true);
            Path path = (Path) getPropertyPathMethod.invoke(hibernateContext);

            String fieldName = path.toString();

            if (rootBean != null && fieldName != null) {
                Field field = findField(rootBean.getClass(), fieldName);
                if (field != null) {
                    field.setAccessible(true);
                    field.set(rootBean, null);
                    return true;
                }
            }
        } catch (Exception e) {
            // Loglash tavsiya etiladi
            return false;
        }
        return false;
    }

    private Field findField(Class<?> clazz, String fieldName) {
        Class<?> current = clazz;
        while (current != null) {
            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                current = current.getSuperclass();
            }
        }
        return null;
    }

    private JpaRepository<?, Long> getRepositoryByEntity(Class<?> entityClass) {
        return repositories.values().stream()
            .filter(repo -> {
                Class<?>[] typeArguments = GenericTypeResolver.resolveTypeArguments(repo.getClass(), JpaRepository.class);

                return typeArguments != null && typeArguments.length > 0 && typeArguments[0].equals(entityClass);
            })
            .findFirst()
            .orElse(null);
    }
}
