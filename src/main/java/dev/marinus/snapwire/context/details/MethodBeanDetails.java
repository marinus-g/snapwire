package dev.marinus.snapwire.context.details;


import dev.marinus.snapwire.context.details.constructor.MethodConstructorBeanParameterDetails;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;

@Getter
@Setter
public final class MethodBeanDetails extends GenericBeanDetails {

    private MethodConstructorBeanParameterDetails methodParameterDetails;
    private BeanDetails holder; // A method bean needs to have a holder


    public MethodBeanDetails(Class<?> type, String name, Method method) {
        super(type, name);
    }
}
