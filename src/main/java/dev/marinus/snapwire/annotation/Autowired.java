package dev.marinus.snapwire.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provides a way to autowire a field or method.
 * <p>
 * This annotation is used to mark a field or method as autowired.
 * When a class is registered as a bean, SnapWire will attempt to autowire the fields and methods annotated with
 * this annotation.
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface Autowired {
}
