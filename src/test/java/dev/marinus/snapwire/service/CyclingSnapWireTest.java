package dev.marinus.snapwire.service;

import dev.marinus.snapwire.SnapWire;
import dev.marinus.snapwire.service.cycling.CyclingParent;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.hamcrest.MatcherAssert.assertThat;

class CyclingSnapWireTest {

    @SneakyThrows
    @BeforeEach
    void setUp() {
        Field instance = SnapWire.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);    }

    @Test
    void testCyclingDependency() {
        CyclingParent parent = new CyclingParent();
        boolean exceptionThrown = false;
        try {
            parent.register();
            parent.preEnableBeans();
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        assertThat(exceptionThrown, Matchers.is(true));
    }

}
