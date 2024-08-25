package dev.marinus.snapwire.context.details;

import org.jetbrains.annotations.Nullable;

public final class ServiceBeanDetails extends TypeBeanDetails {

    public ServiceBeanDetails(Object instance) {
        super(instance);
    }

    public ServiceBeanDetails(Class<?> type, String name) {
        super(type, name);
    }

    public ServiceBeanDetails(Class<?> type, @Nullable Object instance, String name) {
        super(type, instance, name);
    }
}
