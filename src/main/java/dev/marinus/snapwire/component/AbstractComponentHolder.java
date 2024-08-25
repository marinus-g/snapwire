package dev.marinus.snapwire.component;

import dev.marinus.snapwire.context.BeanContext;
import dev.marinus.snapwire.context.details.constructor.ConstructorBeanParameterDetails;

/**
 * The ComponentHolder class is a class that holds the component.
 */
public abstract class AbstractComponentHolder<T> {

    public Object newInstance(BeanContext beanContext, Class<?> type) {
        return this.newInstance(this.constructorDetails(beanContext, type));
    }

    public Object newInstance(ConstructorBeanParameterDetails constructorDetails) {
        try {
            return constructorDetails.newInstance();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    /**
     * Called when a component instance is enabled
     *
     * @param t The instance to enable
     */
    public void onEnable(T t) {

    }

    /**
     * Called when a component instance is disabled
     *
     * @param t The instance to disable
     */
    public void onDisable(T t) {

    }

    public ConstructorBeanParameterDetails constructorDetails(BeanContext beanContext, Class<?> type) {
        return new ConstructorBeanParameterDetails(type, beanContext);
    }

}
