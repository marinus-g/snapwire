package dev.marinus.snapwire.context.details.constructor;

import dev.marinus.snapwire.context.BeanContext;

import java.lang.reflect.Parameter;

public interface BeanParameterDetails {

    Object[] getParameters(BeanContext beanContext);

    Parameter[] getParameters();

}
