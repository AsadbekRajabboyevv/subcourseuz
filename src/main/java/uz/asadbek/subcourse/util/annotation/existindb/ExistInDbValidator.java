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
public class ExistInDbValidator implements ConstraintValidator<ExistsInDb, Object> {

    private final Map<String, JpaRepository<?, ?>> repositories;
    private JpaRepository<?, ?> cachedRepository;
    private boolean setNull;
    private boolean optional;
    private String field;

    public ExistInDbValidator(Map<String, JpaRepository<?, ?>> repositories) {
        this.repositories = repositories;
    }

    @Override
    public void initialize(ExistsInDb annotation) {
        this.setNull = annotation.setNull();
        this.optional = annotation.optional();
        this.field = annotation.field();
        this.cachedRepository = getRepositoryByEntity(annotation.entity());
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return optional;
        }

        if (cachedRepository == null) {
            return false;
        }

        boolean exists;

        if ("id".equals(field) && value instanceof Long id) {
            exists = ((JpaRepository<?, Long>) cachedRepository).existsById(id);
        } else {
            exists = existsByField(value);
        }

        if (!exists) {
            if (setNull) {
                return trySetFieldToNull(context);
            }
            return false;
        }

        return true;
    }

    private boolean existsByField(Object value) {
        try {
            var methodName = STR."existsBy\{capitalize(field)}";
            var method = cachedRepository.getClass().getMethod(methodName, value.getClass());
            var result = method.invoke(cachedRepository, value);
            return result instanceof Boolean b && b;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean trySetFieldToNull(ConstraintValidatorContext context) {
        try {
            var ctx = context.unwrap(ConstraintValidatorContextImpl.class);

            var getRootBeanMethod = ConstraintValidatorContextImpl.class.getDeclaredMethod("getRootBean");
            getRootBeanMethod.setAccessible(true);
            var rootBean = getRootBeanMethod.invoke(ctx);

            var getPropertyPathMethod = ConstraintValidatorContextImpl.class.getDeclaredMethod("getPropertyPath");
            getPropertyPathMethod.setAccessible(true);
            var path = (Path) getPropertyPathMethod.invoke(ctx);

            var fieldName = path.toString();

            if (rootBean != null && fieldName != null) {
                var field = findField(rootBean.getClass(), fieldName);
                if (field != null) {
                    field.setAccessible(true);
                    field.set(rootBean, null);
                    return true;
                }
            }
        } catch (Exception ignored) {
            return false;
        }
        return false;
    }

    private Field findField(Class<?> clazz, String fieldName) {
        var current = clazz;
        while (current != null) {
            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                current = current.getSuperclass();
            }
        }
        return null;
    }

    private JpaRepository<?, ?> getRepositoryByEntity(Class<?> entityClass) {
        return repositories.values().stream()
            .filter(repo -> {
                var types = GenericTypeResolver.resolveTypeArguments(repo.getClass(), JpaRepository.class);
                return types != null && types.length > 0 && types[0].equals(entityClass);
            })
            .findFirst()
            .orElse(null);
    }

    private String capitalize(String str) {
        return (str == null || str.isBlank())
            ? str
            : str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
