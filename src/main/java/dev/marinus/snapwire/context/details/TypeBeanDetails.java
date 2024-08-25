package dev.marinus.snapwire.context.details;

import dev.marinus.snapwire.context.details.constructor.ConstructorBeanParameterDetails;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

@Setter
@Getter
public class TypeBeanDetails extends GenericBeanDetails {


    private ConstructorBeanParameterDetails constructorDetails;

    public TypeBeanDetails(Object instance) {
        super(instance);
    }

    public TypeBeanDetails(Class<?> type, String name) {
        super(type, name);
    }

    public TypeBeanDetails(Class<?> type, @Nullable Object instance, String name) {
        super(type, instance, name);
    }

}
