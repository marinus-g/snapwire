package dev.marinus.snapwire.context.details;

import dev.marinus.snapwire.SnapWired;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public interface BeanDetails {

    Class<?> getType();

    String getName();

    @Nullable
    Object getBean();

    SnapWired getParent();

    void setParent(@Nullable SnapWired parent);

    void setBean(@NotNull Object bean);

    void addChildren(BeanDetails beanDetails);

    void removeChildren(BeanDetails beanDetails);

    void addDependency(BeanDetails beanDetails);

    Set<BeanDetails> getDependencies();

    void removeDependency(BeanDetails beanDetails);

    BeanDetails[] getChildren();

    Stage getStage();

    void setStage(Stage stage);

    boolean hasDependencies();

    boolean hasChildren();


    enum Stage {
        CREATED,
        PRE_INITIALIZED,
        INITIALIZED,
        PRE_DESTROYED,
        DESTROYED
    }

}
