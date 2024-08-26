package dev.marinus.snapwire.service;

import dev.marinus.snapwire.SnapWire;
import dev.marinus.snapwire.context.BeanContext;
import dev.marinus.snapwire.service.enabledisable1.EnableDisable1Parent;
import dev.marinus.snapwire.service.enabledisable1.service.EnableDisableService0;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class EnableDisableServiceSnapWireTest {

    @SneakyThrows
    @BeforeEach
    void setUp() {
        Field instance = SnapWire.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }


    @Test
    void testEnableDisableService() {
        EnableDisable1Parent parent = new EnableDisable1Parent();
        final BeanContext context = SnapWire.getInstance().getRootContext();
        parent.register();
        EnableDisableService0 service0 = context.getBean(EnableDisableService0.class);
        assertThat(service0, notNullValue());
        assertThat(service0.getTest(), notNullValue());
        assertThat(service0.getTest(), is("preEnable"));
        parent.enableBeans();
        assertThat(service0.getTest(), is("postEnable"));
        parent.unregister();
        assertThat(service0.getTest(), nullValue());
    }

}
