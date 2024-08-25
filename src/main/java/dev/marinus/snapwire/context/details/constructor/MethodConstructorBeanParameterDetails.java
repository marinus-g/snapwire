package dev.marinus.snapwire.context.details.constructor;

import dev.marinus.snapwire.context.BeanContext;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class MethodConstructorBeanParameterDetails extends AbstractBeanParameterDetails {

    private final Method method;
    private final BeanContext beanContext;
    public MethodConstructorBeanParameterDetails(Method method, BeanContext beanContext) {
        this.method = method;
        this.beanContext = beanContext;
    }

    public void load() {
        this.parameters = this.method.getParameters();
        for (Parameter parameter : this.parameters) {
            if (!beanContext.isBean(parameter.getType())) {
                throw new IllegalArgumentException(String.format("The type %s is not a bean, it's not supposed to be in bean method!", parameter.getType().getName()));
            }
        }
    }

    public Object invoke(Object instance, BeanContext beanContext) {
        try {
            return this.method.invoke(instance, this.getParameters(beanContext));
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("Could not invoke method %s", this.method.getName()), e);
        }
    }
}