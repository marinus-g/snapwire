package dev.marinus.snapwire;

import dev.marinus.snapwire.annotation.WiredPackages;
import dev.marinus.snapwire.context.BeanContext;
import lombok.Getter;

import javax.annotation.Nullable;

public class SnapWire {

    private static SnapWire instance;

    @Getter
    private final BeanContext rootContext;

    private SnapWire() {
        rootContext = new BeanContext();
    }

    public static SnapWire getInstance() {
        if (instance == null) {
            instance = new SnapWire();
        }
        return instance;
    }

    public void register(Class<? extends SnapWired> clazz, SnapWired parent) {

        this.register(clazz, clazz.getClassLoader(), parent, null);
    }

    public void register(Class<? extends SnapWired> clazz, ClassLoader classLoader, SnapWired parent, @Nullable BeanContext context) {
        if (clazz.isInterface()) {
            throw new IllegalArgumentException(String.format("Class %s is an interface and cannot be registered as a parent", clazz.getName()));
        }
        if (!clazz.isAnnotationPresent(WiredPackages.class)) {
            throw new IllegalArgumentException(String.format("Class %s is not annotated with @WiredPackages", clazz.getName()));
        }
        if (context == null) {
            context = this.rootContext;
        }
        context.registerWiredParent(parent, classLoader);
    }

    public void unregister(Class<? extends SnapWired> clazz, boolean disable) {

    }

    public void onEnable(SnapWired snapWired, @Nullable BeanContext context) {
        if (context == null) {
            context = this.rootContext;
        }
        context.onEnable(snapWired);

    }
}