package dev.marinus.snapwire.context.details;

import dev.marinus.snapwire.DisposableBean;
import dev.marinus.snapwire.SnapWired;
import dev.marinus.snapwire.reflect.ClassUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class GenericBeanDetails implements BeanDetails {

    private String name;
    private Stage stage;

    private @Nullable Object instance;
    private Class<?> type;
    private final boolean disposable;
    private SnapWired parent;

    private final Set<BeanDetails> children = new HashSet<>();
    private final Set<BeanDetails> dependencies = new HashSet<>();
    private final Set<Class<?>> superTypes = new HashSet<>();

    public GenericBeanDetails(Object instance) {
        this(instance.getClass(), instance.getClass().getSimpleName());
        setBean(instance);
    }

    public GenericBeanDetails(Class<?> type, String name) {
        this.type = type;
        this.name = name;
        this.stage = Stage.CREATED;
        this.disposable = DisposableBean.class.isAssignableFrom(type);
        this.superTypes.addAll(ClassUtils.getSuperClassesAndInterfaces(type));
        this.superTypes.remove(DisposableBean.class); // Remove the DisposableBean interface, since it´s internal
    }

    public GenericBeanDetails(Class<?> type, @Nullable Object instance, String name) {
        this(type, name);
        this.instance = instance;
    }


    @Override
    public Class<?> getType() {
        return this.type;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Nullable
    @Override
    public Object getBean() {
        return this.instance;
    }

    @Override
    public SnapWired getParent() {
        return this.parent;
    }

    @Override
    public void setParent(@Nullable SnapWired parent) {
        this.parent = parent;
    }

    @Override
    public void setBean(@Nullable Object bean) {
        this.instance = bean;
        this.stage = bean == null ? Stage.DESTROYED : Stage.PRE_INITIALIZED;
    }

    @Override
    public void addChildren(BeanDetails beanDetails) {
        this.children.add(beanDetails);
    }

    @Override
    public void removeChildren(BeanDetails beanDetails) {
        this.children.remove(beanDetails);
    }

    @Override
    public void addDependency(BeanDetails beanDetails) {
        this.dependencies.add(beanDetails);
    }

    @Override
    public Set<BeanDetails> getDependencies() {
        return Collections.unmodifiableSet(this.dependencies);
    }

    @Override
    public void removeDependency(BeanDetails beanDetails) {
        this.dependencies.remove(beanDetails);
    }

    @Override
    public BeanDetails[] getChildren() {
        return this.children.toArray(new BeanDetails[0]);
    }

    @Override
    public Stage getStage() {
        return this.stage;
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public boolean hasDependencies() {
        return !this.dependencies.isEmpty();
    }

    @Override
    public boolean hasChildren() {
        return !this.children.isEmpty();
    }

    @Override
    public Collection<Class<?>> getSuperTypes() {
        return Collections.unmodifiableSet(this.superTypes);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        GenericBeanDetails that = (GenericBeanDetails) obj;
        return Objects.equals(this.name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.name);
    }
}
