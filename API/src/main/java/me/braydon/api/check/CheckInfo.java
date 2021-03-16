package me.braydon.api.check;

import lombok.NonNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation holds information for each check.
 *
 * @author Braydon
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CheckInfo {
    /**
     * The display name of the check.
     *
     * @return the display name
     */
    @NonNull String name();

    /**
     * The type of the check.
     *
     * @return the type
     * @see CheckType
     */
    @NonNull CheckType type();

    /**
     * Whether or not this check is experimental.
     *
     * @return the experimental status
     */
    boolean experimental() default false;

    /**
     * The maximum amount of violations the player must reach before being banned.
     * <p>
     * For this to be used, the ban value must be true
     *
     * @return the max violations
     */
    int maxVl() default 10;

    /**
     * Whether or not this check will ban if the maximum violations is reached.
     *
     * @return the ban status
     */
    boolean ban() default true;
}