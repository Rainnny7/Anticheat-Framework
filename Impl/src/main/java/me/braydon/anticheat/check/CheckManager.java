package me.braydon.anticheat.check;

import me.braydon.anticheat.check.impl.fly.FlyA;
import me.braydon.anticheat.check.impl.test.TestA;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Braydon
 */
public class CheckManager {
    /**
     * A {@link List} of registered check classes
     * @see Check
     */
    public static final List<Class<? extends Check>> CHECK_CLASSES = Collections.unmodifiableList(Arrays.asList(
            // Test
            TestA.class,

            // Fly
            FlyA.class
    ));
}