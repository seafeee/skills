package net.flyinggroup.util.validate;

import jakarta.annotation.Nonnull;
import net.flyinggroup.util.exception.InvalidArgumentException;
import net.flyinggroup.util.lang.CollectionUtil;
import net.flyinggroup.util.lang.MapUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

/**
 * 多参数链式校验器,支持 AND/OR 逻辑组合。
 * <p>
 * AND 模式:所有检查都必须通过,任一失败则抛出异常。
 * OR 模式:至少一个检查通过即可,全部失败才抛出异常。
 * <p>
 * 用于业务层多字段联合校验,不满足时抛出 {@link InvalidArgumentException}。
 * <p>
 * 使用示例:
 * <pre>
 * MultiValidator.and()
 *     .checkNotBlank(username, "username")
 *     .checkNotBlank(email, "email")
 *     .check(age, v -> v >= 18, "must be adult", "age")
 *     .validateOrThrow();
 * </pre>
 */
public final class MultiValidator {
    private final Set<InvalidArgumentException.InvalidArgument> invalids = new HashSet<>();
    private final Operation operation;
    private boolean hasSuccess = false;

    private MultiValidator(Operation operation) {
        this.operation = operation;
    }

    @Nonnull
    public static MultiValidator or() {
        return new MultiValidator(Operation.OR);
    }

    @Nonnull
    public static MultiValidator and() {
        return new MultiValidator(Operation.AND);
    }

    @Nonnull
    public <T> MultiValidator check(T value, Predicate<T> validator, String message, String name) {
        if (validator.test(value)) {
            hasSuccess = true;
        } else {
            invalids.add(new InvalidArgumentException.InvalidArgument(message, name, value));
        }
        return this;
    }

    @Nonnull
    public <T> MultiValidator checkNotNull(T value) {
        return check(value, Objects::nonNull, Validator.MESSAGE_MUST_NOT_BE_NULL, Validator.DEFAULT_NAME);
    }

    @Nonnull
    public <T> MultiValidator checkNotNull(T value, String name) {
        return check(value, Objects::nonNull, Validator.MESSAGE_MUST_NOT_BE_NULL, name);
    }

    @Nonnull
    public MultiValidator checkNotBlank(String value) {
        return check(value, StringUtils::isNotBlank, Validator.MESSAGE_MUST_NOT_BE_BLANK, Validator.DEFAULT_NAME);
    }

    @Nonnull
    public MultiValidator checkNotBlank(String value, String name) {
        return check(value, StringUtils::isNotBlank, Validator.MESSAGE_MUST_NOT_BE_BLANK, name);
    }

    @Nonnull
    public MultiValidator checkNotEmpty(Collection<?> value) {
        return check(value, CollectionUtil::isNotEmpty, Validator.MESSAGE_MUST_NOT_BE_EMPTY, Validator.DEFAULT_NAME);
    }

    @Nonnull
    public MultiValidator checkNotEmpty(Collection<?> value, String name) {
        return check(value, CollectionUtil::isNotEmpty, Validator.MESSAGE_MUST_NOT_BE_EMPTY, name);
    }

    @Nonnull
    public MultiValidator checkNotEmpty(Map<?, ?> value) {
        return check(value, MapUtil::isNotEmpty, Validator.MESSAGE_MUST_NOT_BE_EMPTY, Validator.DEFAULT_NAME);
    }

    @Nonnull
    public MultiValidator checkNotEmpty(Map<?, ?> value, String name) {
        return check(value, MapUtil::isNotEmpty, Validator.MESSAGE_MUST_NOT_BE_EMPTY, name);
    }

    public void validateOrThrow() {
        if ((operation == Operation.AND && !invalids.isEmpty()) || (operation == Operation.OR && !hasSuccess)) {
            throw new InvalidArgumentException(invalids);
        }
    }

    private enum Operation {
        AND, OR
    }
}
