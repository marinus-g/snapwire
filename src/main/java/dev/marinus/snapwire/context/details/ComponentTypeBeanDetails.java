package dev.marinus.snapwire.context.details;

import dev.marinus.snapwire.component.AbstractComponentHolder;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
public final class ComponentTypeBeanDetails extends TypeBeanDetails {

    private AbstractComponentHolder<?> componentHolder;


    public ComponentTypeBeanDetails(Object instance) {
        super(instance);
    }

    public ComponentTypeBeanDetails(Class<?> type, String name) {
        super(type, name);
    }

    public ComponentTypeBeanDetails(Class<?> type, @Nullable Object instance, String name) {
        super(type, instance, name);
    }
}
