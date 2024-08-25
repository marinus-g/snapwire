package dev.marinus.snapwire.component;

import dev.marinus.snapwire.component.test1.Component1Parent;
import dev.marinus.snapwire.SnapWire;
import dev.marinus.snapwire.component.test2.Component2Parent;
import dev.marinus.snapwire.component.test2.component.ExampleComponent;
import dev.marinus.snapwire.context.BeanContext;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.hamcrest.MatcherAssert.assertThat;

class ComponentSnapWireTest {

    @SneakyThrows
    @BeforeEach
    void setUp() {
        Field instance = SnapWire.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test
    void testSimpleComponent() {
        Component1Parent parent = new Component1Parent();
        parent.register();
        parent.preEnableBeans();
        BeanContext context = SnapWire.getInstance().getRootContext();
        assertThat(context.getRegisteredBeans(), Matchers.hasSize(5));
    }

    @Test
    void testComponentHolder() {
        Component2Parent parent = new Component2Parent();
        parent.register();
        parent.preEnableBeans();
        BeanContext context = SnapWire.getInstance().getRootContext();
        ExampleComponent exampleInterface = context.getBean(ExampleComponent.class);
        assertThat(exampleInterface.getComponentName(), Matchers.equalTo("ExampleComponent"));
    }
}
