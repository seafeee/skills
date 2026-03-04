package net.flyinggroup.util.validate;

import jakarta.annotation.Nonnull;
import net.flyinggroup.util.exception.FlyingRuntimeException;
import net.flyinggroup.util.lang.CollectionUtil;
import net.flyinggroup.util.lang.MapUtil;
import net.flyinggroup.util.lang.ObjectUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * 系统层前置条件检查工具，用于编程/契约层面的约束（如内部状态、配置、不可达分支）。
 * 不满足时抛出 {@link FlyingRuntimeException}，表示系统级/编程错误，应尽快暴露并修复。
 * <p>
 * 与 {@link Validator} / {@link MultiValidator} 区分：后者用于业务层校验，抛出 {@link net.flyinggroup.util.exception.InvalidArgumentException}。
 * 参数风格与 Validator 一致：value、predicate/message、以及可选的 name。
 */
public final class Preconditions {

    private Preconditions() {
    }

    @Nonnull
    public static <T> T requireNotNull(T value) {
        return requireNotNull(value, Validator.DEFAULT_NAME);
    }

    @Nonnull
    public static <T> T requireNotNull(T value, String name) {
        return require(value, Objects::nonNull, Validator.MESSAGE_MUST_NOT_BE_NULL, name);
    }

    @Nonnull
    public static String requireNotBlank(String value) {
        return requireNotBlank(value, Validator.DEFAULT_NAME);
    }

    @Nonnull
    public static String requireNotBlank(String value, String name) {
        return require(value, StringUtils::isNotBlank, Validator.MESSAGE_MUST_NOT_BE_BLANK, name);
    }

    @Nonnull
    public static <E> Collection<E> requireNotEmpty(Collection<E> value) {
        return requireNotEmpty(value, Validator.DEFAULT_NAME);
    }

    @Nonnull
    public static <E> Collection<E> requireNotEmpty(Collection<E> value, String name) {
        return require(value, CollectionUtil::isNotEmpty, Validator.MESSAGE_MUST_NOT_BE_EMPTY, name);
    }

    @Nonnull
    public static <K, V> Map<K, V> requireNotEmpty(Map<K, V> value) {
        return requireNotEmpty(value, Validator.DEFAULT_NAME);
    }

    @Nonnull
    public static <K, V> Map<K, V> requireNotEmpty(Map<K, V> value, String name) {
        return require(value, MapUtil::isNotEmpty, Validator.MESSAGE_MUST_NOT_BE_EMPTY, name);
    }

    public static <T> T require(T value, Predicate<T> predicate, String message, String name) {
        if (!predicate.test(value)) {
            throw new FlyingRuntimeException(formatMessage(name, message, value));
        }
        return value;
    }

    public static <T> T require(T value, Predicate<T> predicate, String message) {
        return require(value, predicate, message, Validator.DEFAULT_NAME);
    }

    public static <T> T require(T value, Predicate<T> predicate) {
        return require(value, predicate, Validator.DEFAULT_MESSAGE, Validator.DEFAULT_NAME);
    }

    private static String formatMessage(String name, String message, Object value) {
        return "%s: %s (value=%s)".formatted(name, message, ObjectUtil.toDisplayString(value));
    }
}
