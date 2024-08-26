package dev.marinus.snapwire.service;

import dev.marinus.snapwire.SnapWire;
import dev.marinus.snapwire.context.BeanContext;
import dev.marinus.snapwire.service.enable1.Enable1Parent;
import dev.marinus.snapwire.service.enable1.service.EnableService0;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class EnableServiceSnapWireTest {

    @SneakyThrows
    @BeforeEach
    void setUp() {
        Field instance = SnapWire.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test
    void testEnableService() {
        Enable1Parent parent = new Enable1Parent();
        final BeanContext context = SnapWire.getInstance().getRootContext();
        parent.register();
        EnableService0 service0 = context.getBean(EnableService0.class);
        assertThat(service0, notNullValue());
        assertThat(service0.getStatus(), notNullValue());
        assertThat(service0.getStatus(), is("preEnabled"));
        parent.enableBeans();
        assertThat(service0.getStatus(), is("postEnabled"));
    }

}
