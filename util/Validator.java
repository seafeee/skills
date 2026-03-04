package net.flyinggroup.util.validate;

import jakarta.annotation.Nonnull;
import net.flyinggroup.util.exception.InvalidArgumentException;
import net.flyinggroup.util.lang.CollectionUtil;
import net.flyinggroup.util.lang.MapUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * 业务层参数校验工具。
 * 不满足时抛出 {@link InvalidArgumentException}，便于接口层统一处理并返回业务错误（如 4xx）。
 */
public final class Validator {

    static final String DEFAULT_NAME = "argument";
    static final String DEFAULT_MESSAGE = "invalid value";
    static final String MESSAGE_MUST_NOT_BE_NULL = "must not be null";
    static final String MESSAGE_MUST_NOT_BE_BLANK = "must not be blank";
    static final String MESSAGE_MUST_NOT_BE_EMPTY = "must not be null or empty";

    private Validator() {
    }

    @Nonnull
    public static <T> T validateNotNullOrThrow(T value) {
        return validateNotNullOrThrow(value, DEFAULT_NAME);
    }

    @Nonnull
    public static <T> T validateNotNullOrThrow(T value, String name) {
        return validateOrThrow(value, Objects::nonNull, MESSAGE_MUST_NOT_BE_NULL, name);
    }

    @Nonnull
    public static String validateNotBlankOrThrow(String value) {
        return validateNotBlankOrThrow(value, DEFAULT_NAME);
    }

    @Nonnull
    public static String validateNotBlankOrThrow(String value, String name) {
        return validateOrThrow(value, StringUtils::isNotBlank, MESSAGE_MUST_NOT_BE_BLANK, name);
    }

    @Nonnull
    public static <E> Collection<E> validateNotEmptyOrThrow(Collection<E> value) {
        return validateNotEmptyOrThrow(value, DEFAULT_NAME);
    }

    @Nonnull
    public static <E> Collection<E> validateNotEmptyOrThrow(Collection<E> value, String name) {
        return validateOrThrow(value, CollectionUtil::isNotEmpty, MESSAGE_MUST_NOT_BE_EMPTY, name);
    }

    @Nonnull
    public static <K, V> Map<K, V> validateNotEmptyOrThrow(Map<K, V> value) {
        return validateNotEmptyOrThrow(value, DEFAULT_NAME);
    }

    @Nonnull
    public static <K, V> Map<K, V> validateNotEmptyOrThrow(Map<K, V> value, String name) {
        return validateOrThrow(value, MapUtil::isNotEmpty, MESSAGE_MUST_NOT_BE_EMPTY, name);
    }

    public static <T> T validateOrThrow(T value, Predicate<T> validator, String message, String name) {
        if (validator.test(value)) {
            return value;
        }
        throw new InvalidArgumentException(message, name, value);
    }

    public static <T> T validateOrThrow(T value, Predicate<T> validator, String message) {
        return validateOrThrow(value, validator, message, DEFAULT_NAME);
    }

    public static <T> T validateOrThrow(T value, Predicate<T> validator) {
        return validateOrThrow(value, validator, DEFAULT_MESSAGE, DEFAULT_NAME);
    }

    public static <T> T validateOrDefault(T value, Predicate<T> validator, T fallback) {
        if (validator.test(value)) {
            return value;
        }
        return fallback;
    }
}
