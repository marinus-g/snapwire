package dev.marinus.snapwire.service;

import dev.marinus.snapwire.SnapWire;
import dev.marinus.snapwire.context.BeanContext;
import dev.marinus.snapwire.service.disposal1.Disposal1Parent;
import dev.marinus.snapwire.service.disposal1.service.DisposalService0;
import dev.marinus.snapwire.service.test3.Test3Parent;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.hamcrest.MatcherAssert.assertThat;

class BeansSnapWireTest {

    @SneakyThrows
    @BeforeEach
    void setUp() {
        Field instance = SnapWire.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);    }

    @Test
    void testBeans() {
        Test3Parent parent = new Test3Parent();
        parent.register();
        parent.enableBeans();
        BeanContext context = SnapWire.getInstance().getRootContext();
        dev.marinus.snapwire.service.test3.service.Service2 service2 = context.getBean(dev.marinus.snapwire.service.test3.service.Service2.class);
        assertThat(service2, Matchers.notNullValue());
        assertThat(service2.getTestBean(), Matchers.notNullValue());
        assertThat(service2.getTestBean().getService0(), Matchers.notNullValue());
    }

    @Test
    void testDisposalBean() {
        Disposal1Parent parent = new Disposal1Parent();
        parent.register();
        parent.enableBeans();
        BeanContext context = SnapWire.getInstance().getRootContext();
        DisposalService0 service0 = context.getBean(DisposalService0.class);
        assertThat(service0, Matchers.notNullValue());
        assertThat(service0.getId(), Matchers.notNullValue());
        parent.unregister();
        assertThat(service0.getId(), Matchers.nullValue());
    }

}
