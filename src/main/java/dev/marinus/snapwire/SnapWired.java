package dev.marinus.snapwire;

public interface SnapWired {


    default void register() {
        ClassLoader originalContextClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(this.getClassLoader());
        SnapWire snapWire = SnapWire.getInstance();
        final Class<? extends SnapWired> clazz = this.getClass();
        snapWire.register(clazz, this);
        Thread.currentThread().setContextClassLoader(originalContextClassLoader);
    }

    default void enableBeans() {
        SnapWire snapWire = SnapWire.getInstance();
        final Class<? extends SnapWired> clazz = this.getClass();
        snapWire.onEnable(this, null);
    }

    default void disableBeans() {
        SnapWire snapWire = SnapWire.getInstance();
        final Class<? extends SnapWired> clazz = this.getClass();
    }

    default ClassLoader getClassLoader() {
        return this.getClass().getClassLoader();
    }
}