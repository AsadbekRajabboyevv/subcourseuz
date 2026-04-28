package uz.asadbek.subcourse.util.annotation.existindb;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Path;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.GenericTypeResolver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.stream.Stream;

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

        boolean exists = existsByField(value);

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
            String methodName = "id".equalsIgnoreCase(field)
                ? "existsById"
                : "existsBy" + capitalize(field);
            System.out.println(value.toString());
            Method method = findMethodInInterfaces(cachedRepository.getClass(), methodName);
            if (method == null) {
                throw new NoSuchMethodException("Method not found: " + methodName);
            }

            Object result = method.invoke(cachedRepository, value);
            return result instanceof Boolean b && b;
        } catch (Exception e) {
            System.err.println("Validation Error: " + e.getMessage());
            return false;
        }
    }

    private Method findMethodInInterfaces(Class<?> clazz, String methodName) {
        for (Class<?> iface : clazz.getInterfaces()) {
            for (Method m : iface.getMethods()) {
                if (m.getName().equals(methodName)) {
                    return m;
                }
            }

            Method found = findMethodInInterfaces(iface, methodName);
            if (found != null) return found;
        }

        return Stream.of(clazz.getMethods())
            .filter(m -> m.getName().equals(methodName))
            .findFirst()
            .orElse(null);
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
                Class<?> targetClass = AopUtils.getTargetClass(repo);
                var types = GenericTypeResolver.resolveTypeArguments(targetClass, JpaRepository.class);
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
