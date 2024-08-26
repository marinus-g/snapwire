package dev.marinus.snapwire.autowire;

import dev.marinus.snapwire.SnapWire;
import dev.marinus.snapwire.autowire.autowire0.Autowire0Parent;
import dev.marinus.snapwire.autowire.autowire0.configuration.AutowireConfiguration0;
import dev.marinus.snapwire.autowire.autowire0.service.AutowireService0;
import dev.marinus.snapwire.autowire.autowire1.Autowire1Parent;
import dev.marinus.snapwire.autowire.autowire1.service.AutowireService1;
import dev.marinus.snapwire.context.BeanContext;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class AutoWireTests {

    @SneakyThrows
    @BeforeEach
    void setUp() {
        Field instance = SnapWire.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test
    void testAutowireFields() {
        Autowire0Parent parent = new Autowire0Parent();
        parent.register();
        parent.enableBeans();
        BeanContext context = SnapWire.getInstance().getRootContext();
        AutowireConfiguration0 configuration = context.getBean(AutowireConfiguration0.class);
        assertThat(configuration, notNullValue());
        assertThat(configuration.getTestString(), notNullValue());
        AutowireService0 service = context.getBean(AutowireService0.class);
        assertThat(service, notNullValue());
        assertThat(service.getConfiguration(), notNullValue());
        assertThat(service.getConfiguration(), equalTo(configuration));
        assertThat(service.getConfiguration().getTestString(), is(configuration.getTestString()));
    }

    @Test
    void testAutowireSetters() {
        Autowire1Parent parent = new Autowire1Parent();
        parent.register();
        parent.enableBeans();
        BeanContext context = SnapWire.getInstance().getRootContext();
        AutowireService1 service = context.getBean(AutowireService1.class);
        assertThat(service, notNullValue());
        assertThat(service.getConfiguration(), notNullValue());
        assertThat(service.getConfiguration().getTestString(), is("test"));
        assertThat(service.getComponent0(), notNullValue());
        assertThat(service.getComponent1(), notNullValue());

    }

}
