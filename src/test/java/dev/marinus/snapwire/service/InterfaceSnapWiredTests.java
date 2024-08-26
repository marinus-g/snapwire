package dev.marinus.snapwire.service;

import dev.marinus.snapwire.SnapWire;
import dev.marinus.snapwire.context.BeanContext;
import dev.marinus.snapwire.service.interface1.Interface1Parent;
import dev.marinus.snapwire.service.interface1.service.InterfaceService0;
import dev.marinus.snapwire.service.interface1.service.InterfaceService1;
import dev.marinus.snapwire.service.interface1.service.impl.InterfaceService0Impl;
import dev.marinus.snapwire.service.interface2.Interface2Parent;
import dev.marinus.snapwire.service.interface2.service.InterfaceService3;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class InterfaceSnapWiredTests {

    @SneakyThrows
    @BeforeEach
    void setUp() {
        Field instance = SnapWire.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test
    void testSimpleInterfaceBeans() {
        Interface1Parent parent = new Interface1Parent();
        parent.register();
        parent.enableBeans();
        final BeanContext context = SnapWire.getInstance().getRootContext();
        assertThat(context.getBean(InterfaceService0.class), notNullValue());
        assertThat(context.getBean(InterfaceService0Impl.class), notNullValue());
        assertThat(context.getBean(InterfaceService0.class).getSomething(), notNullValue());
        assertThat(context.getBean(InterfaceService1.class), notNullValue());
        assertThat(context.getBean(InterfaceService1.class).getInterfaceService0(), notNullValue());
        assertThat(context.getBean(InterfaceService1.class).getInterfaceService0().getSomething(), notNullValue());
    }

    @Test
    void testNamedInterfaceBeans() {
        Interface2Parent parent = new Interface2Parent();
        parent.register();
        parent.enableBeans();
        final BeanContext context = SnapWire.getInstance().getRootContext();
        assertThat(context.getBean("interfaceService2Impl0"), notNullValue());
        assertThat(context.getBean("interfaceService2Impl1"), notNullValue());
        assertThat(context.getBean(InterfaceService3.class).getInterfaceService2_0(), notNullValue());
        assertThat(context.getBean(InterfaceService3.class).getInterfaceService2_1(), notNullValue());
        assertThat(context.getBean(InterfaceService3.class).getInterfaceService2_0().getSomething(), notNullValue());
        assertThat(context.getBean(InterfaceService3.class).getInterfaceService2_1().getSomething(), notNullValue());
        assertThat(context.getBean(InterfaceService3.class).getInterfaceService2_0().getSomething(), is("InterfaceService2Impl0"));
        assertThat(context.getBean(InterfaceService3.class).getInterfaceService2_1().getSomething(), is("InterfaceService2Impl1"));
    }
}
