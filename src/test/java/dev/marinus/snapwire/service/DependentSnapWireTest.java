package dev.marinus.snapwire.service;

import dev.marinus.snapwire.SnapWire;
import dev.marinus.snapwire.context.BeanContext;
import dev.marinus.snapwire.service.test2.Test2Parent;
import dev.marinus.snapwire.service.test2.service.Service1;
import dev.marinus.snapwire.service.test2.service.Service2;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.hamcrest.MatcherAssert.assertThat;

class DependentSnapWireTest {

    @SneakyThrows
    @BeforeEach
    void setUp() {
        Field instance = SnapWire.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test
    void testDependentService() {
        Test2Parent parent = new Test2Parent();
        parent.register();
        parent.preEnableBeans();
        BeanContext context = SnapWire.getInstance().getRootContext();
        assertThat(context.getRegisteredBeans(), Matchers.hasSize(3));
        assertThat(context.getBean(Service1.class), Matchers.notNullValue());
        assertThat(context.getBeanDetails(Service1.class), Matchers.notNullValue());
        //noinspection DataFlowIssue
        assertThat(context.getBean(Service1.class), Matchers.notNullValue());
        assertThat(context.getBean(Service2.class), Matchers.notNullValue());
        //noinspection DataFlowIssue
        assertThat(context.getBeanDetails(Service2.class).getDependencies(), Matchers.hasSize(1));
        assertThat(context.getBean(Service2.class).getService1(), Matchers.notNullValue());

    }

}
