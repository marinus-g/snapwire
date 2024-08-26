package dev.marinus.snapwire.service;

import dev.marinus.snapwire.SnapWire;
import dev.marinus.snapwire.context.BeanContext;
import dev.marinus.snapwire.service.named1.Named1Parent;
import dev.marinus.snapwire.service.named1.service.Named1Service0;
import dev.marinus.snapwire.service.named1.service.Named1Service1;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;

class NamedSnapWireTests {

    @SneakyThrows
    @BeforeEach
    void setUp() {
        Field instance = SnapWire.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test
    void testSimpleNamedBeans() {
        Named1Parent parent = new Named1Parent();
        parent.register();
        parent.enableBeans();
        BeanContext context = SnapWire.getInstance().getRootContext();
        assertThat(context.getBean("named1Service0"), notNullValue());
        assertThat(context.getBean("named1Service1"), notNullValue());
        Named1Service1 service1 = (Named1Service1) context.getBean("named1Service1");
        assertThat(service1.getObject(), notNullValue());
        assertThat(service1.getObject(), instanceOf(Named1Service0.class));
    }

}
