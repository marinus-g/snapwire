package dev.marinus.snapwire.context.details;

import dev.marinus.snapwire.context.details.constructor.ConstructorBeanParameterDetails;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Setter
@Getter
public class TypeBeanDetails extends GenericBeanDetails {


    private ConstructorBeanParameterDetails constructorDetails;

    private final Map<Stage, List<MethodBeanDetails>> methodDetails = new EnumMap<>(Stage.class);

    public TypeBeanDetails(Object instance) {
        super(instance);
    }

    public TypeBeanDetails(Class<?> type, String name) {
        super(type, name);
    }

    public TypeBeanDetails(Class<?> type, @Nullable Object instance, String name) {
        super(type, instance, name);
    }

    public void addMethodDetails(Stage stage, MethodBeanDetails methodDetails) {
        this.methodDetails.computeIfAbsent(stage, k -> new ArrayList<>()).add(methodDetails);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), constructorDetails);
    }

    public List<MethodBeanDetails> getMethodDetails(Stage stage) {
        return this.methodDetails.get(stage);
    }
}
