package dev.marinus.snapwire.context.details.constructor;


import dev.marinus.snapwire.annotation.Use;
import dev.marinus.snapwire.context.BeanContext;
import dev.marinus.snapwire.context.details.BeanDetails;

import java.lang.reflect.Parameter;
import java.util.Arrays;

public class AbstractBeanParameterDetails implements BeanParameterDetails {

    protected Parameter[] parameters;

    @Override
    public Object[] getParameters(BeanContext beanContext) {
        if (this.parameters == null) {
            return new Object[0];
        }
        return Arrays.stream(getParameterDetails(beanContext))
                .map(BeanDetails::getBean)
                .toArray();
    }

    public BeanDetails[] getParameterDetails(BeanContext beanContext) {
        if (this.parameters == null) {
            return new BeanDetails[0];
        }

        BeanDetails[] parameters = new BeanDetails[this.parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            final Parameter parameter = this.parameters[i];
            if (parameter.isAnnotationPresent(Use.class)) {
                Use use = parameter.getAnnotation(Use.class);
                if (!use.name().isEmpty()) {
                    parameters[i] = beanContext.getBeanDetails(use.name());
                } else {
                    parameters[i] = beanContext.getBeanDetails(use.type());
                }
            } else {
                parameters[i] = beanContext.getBeanDetails(this.parameters[i].getType());
            }
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
