package dev.marinus.snapwire.service;

import dev.marinus.snapwire.SnapWire;
import dev.marinus.snapwire.context.BeanContext;
import dev.marinus.snapwire.service.test1.Test1Parent;
import dev.marinus.snapwire.service.test1.service.Service1;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

class SingleServiceSnapWireTest {

    @SneakyThrows
    @BeforeEach
    void setUp() {
        Field instance = SnapWire.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test
    void testSingleService() {
        Test1Parent parent = new Test1Parent();
        parent.register();
        parent.enableBeans();
        BeanContext context = SnapWire.getInstance().getRootContext();
        Service1 service1 = context.getBean(Service1.class);
        assertThat(service1, notNullValue());
        assertThat(context.getRegisteredBeans().size(), is(2));
    }

}
