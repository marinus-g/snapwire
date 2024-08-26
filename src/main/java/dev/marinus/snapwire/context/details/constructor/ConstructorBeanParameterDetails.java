package dev.marinus.snapwire.context.details.constructor;

import dev.marinus.snapwire.annotation.BeanConstructor;
import dev.marinus.snapwire.annotation.Use;
import dev.marinus.snapwire.context.BeanContext;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;

public class ConstructorBeanParameterDetails extends AbstractBeanParameterDetails {

    private final Class<?> type;
    private Constructor<?> constructor;
    private final BeanContext beanContext;
    public ConstructorBeanParameterDetails(Class<?> type, BeanContext beanContext) {
        this.type = type;
        this.beanContext = beanContext;
    }

    public void load() {
        Constructor<?> constructorToUse = getConstructor();
        if (constructorToUse == null) {
            throw new IllegalArgumentException("No constructor found for " + this.type.getName());
        }
        super.parameters = constructorToUse.getParameters();
        for (Parameter parameter : super.parameters) {
            if (parameter.isAnnotationPresent(Use.class)) {
                Use use = parameter.getAnnotation(Use.class);
                if (!use.name().isEmpty()) {
                    if (!beanContext.isBean(use.name())) {
                        throw new IllegalArgumentException("Couldn't find bean " + use.name() + "!");
                    }
                } else {
                    if (!beanContext.isBean(use.type())) {
                        throw new IllegalArgumentException(String.format("The type %s is not a bean, it's not supposed to be in bean constructor!", use.type().getName()));
                    }
                }
            } else {
                if (!beanContext.isBean(parameter.getType())) {
                    throw new IllegalArgumentException(String.format("The type %s is not a bean, it's not supposed to be in bean constructor!", parameter.getType().getName()));
                }
            }
        }
        constructor =  constructorToUse;
    }

    @Nullable
    private Constructor<?> getConstructor() {
        Constructor<?> constructorToUse = null;
        int currentPriority = -1;
        for (Constructor<?> foundConstructor : this.type.getDeclaredConstructors()) {

            int priority = -1;
            BeanConstructor annotation = foundConstructor.getAnnotation(BeanConstructor.class);
            if (annotation != null) {
                priority = annotation.priority();
            }
            if (constructorToUse == null || currentPriority < priority) {
                constructorToUse = foundConstructor;
                currentPriority = priority;
            }
        }
        return constructorToUse;
    }

    public Object newInstance() {
        try {
            return this.constructor.newInstance(this.getParameters(beanContext));
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("Could not create a new instance for %s", this.type.getName()), e);
        }
    }
}
