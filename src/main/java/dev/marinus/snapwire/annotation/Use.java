package dev.marinus.snapwire.annotation;

public @interface Use {

    String name() default "";

    Class<?> type();

}
