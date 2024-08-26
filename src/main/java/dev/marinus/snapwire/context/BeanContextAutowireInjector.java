package dev.marinus.snapwire.context;

import dev.marinus.snapwire.annotation.Autowired;
import dev.marinus.snapwire.annotation.Use;
import dev.marinus.snapwire.context.details.BeanDetails;
import dev.marinus.snapwire.context.details.constructor.MethodConstructorBeanParameterDetails;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Stream;

public class BeanContextAutowireInjector {

    private final BeanContext beanContext;

    public BeanContextAutowireInjector(BeanContext beanContext) {
        this.beanContext = beanContext;
    }

    void inject() {
        beanContext.getRegisteredBeans()
                .stream()
                .filter(beanDetails -> beanDetails.getBean() != null)
                .forEach(beanDetails -> {
                    injectAutoWiredFields(beanDetails);
                    injectAutoWiredSetters(beanDetails);
                });
    }

    private void injectAutoWiredFields(BeanDetails beanDetails) {
        if (beanDetails.getBean() == null) {
            throw new IllegalStateException("Bean is not initialized");
        }
        Field[] fields = Stream.concat(Arrays.stream(beanDetails.getBean().getClass().getFields()), Arrays.stream(beanDetails.getBean().getClass().getDeclaredFields()))
                .toArray(Field[]::new);
        for (Field field : fields) {
            if (!isAutowireable(field)) {
                continue;
            }
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            final Object bean = field.isAnnotationPresent(Use.class)
                    ? beanContext.getBean(field.getAnnotation(Use.class).name()) : beanContext.getBean(field.getType());
            if (bean == null) {
                throw new IllegalStateException("Bean not found for field " + field.getName());
            }
            if (!field.getType().isAssignableFrom(bean.getClass())) {
                throw new IllegalStateException("Bean type mismatch for field " + field.getName());
            }
            try {
                field.set(beanDetails.getBean(), bean);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void injectAutoWiredSetters(BeanDetails beanDetails) {
        if (beanDetails.getBean() == null) {
            throw new IllegalStateException("Bean is not initialized");
        }
        for (Method method : beanDetails.getBean().getClass().getMethods()) {
            if (!isAutowireable(method)) {
                continue;
            }
            if (Modifier.isPrivate(method.getModifiers())) {
                throw new IllegalStateException(String.format("Autowired Setter %s is private and cannot be invoked",
                        method.getName()));
            }
            if (Modifier.isProtected(method.getModifiers())) {
                throw new IllegalStateException(String.format("Autowired Setter %s is protected and cannot be invoked",
                        method.getName()));
            }
            final MethodConstructorBeanParameterDetails methodParameterDetails =
                    new MethodConstructorBeanParameterDetails(method, beanContext);
            methodParameterDetails.load();
            try {
                method.invoke(beanDetails.getBean(), methodParameterDetails.getParameters(beanContext));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean isAutowireable(Field field) {
        return !field.getType().isPrimitive() && !field.getType().isArray() && !field.getType().isEnum()
                && !field.getType().isAnnotation() && !Modifier.isStatic(field.getModifiers())
                && !Modifier.isFinal(field.getModifiers()) && field.isAnnotationPresent(Autowired.class);
    }

    private boolean isAutowireable(Method method) {
        return method.getReturnType() == void.class && method.getParameterCount() != 0 && method.isAnnotationPresent(Autowired.class) && !Modifier.isStatic(method.getModifiers());
    }

}