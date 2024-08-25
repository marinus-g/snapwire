package dev.marinus.snapwire.context.details.constructor;


import dev.marinus.snapwire.context.BeanContext;
import dev.marinus.snapwire.context.details.BeanDetails;

import java.lang.reflect.Parameter;

public class AbstractBeanParameterDetails implements BeanParameterDetails {

    protected Parameter[] parameters;

    @Override
    public Object[] getParameters(BeanContext beanContext) {
        if (this.parameters == null) {
            return new Object[0];
        }

        Object[] parameters = new Object[this.parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Object bean = beanContext.getBean(this.parameters[i].getType());
            if (bean == null) {
                throw new IllegalArgumentException("Couldn't find bean " + this.parameters[i].getName() + "!");
            }
            parameters[i] = bean;
        }
        return parameters;
    }

    public BeanDetails[] getParameterDetails(BeanContext beanContext) {
        if (this.parameters == null) {
            return new BeanDetails[0];
        }

        BeanDetails[] parameters = new BeanDetails[this.parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            parameters[i] = beanContext.getBeanDetails(this.parameters[i].getType());
            if (parameters[i] == null) {
                throw new IllegalArgumentException("Couldn't find bean " + this.parameters[i].getName() + "!");
            }
        }
        return parameters;
    }

    @Override
    public Parameter[] getParameters() {
        return this.parameters;
    }

}
