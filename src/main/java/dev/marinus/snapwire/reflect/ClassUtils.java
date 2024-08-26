package dev.marinus.snapwire.reflect;

import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class ClassUtils {

    /**
     * Get all super classes and interfaces from a class
     * Also gets the super classes and interfaces from the super classes and interfaces
     *
     * @param clazz The class to get the super classes and interfaces from
     * @return a set of all super classes and interfaces
     */
    public Set<Class<?>> getSuperClassesAndInterfaces(Class<?> clazz) {
        final Set<Class<?>> classes = new HashSet<>();
        for (Class<?> anInterface : clazz.getInterfaces()) {
            classes.add(anInterface);
            classes.addAll(getSuperClassesAndInterfaces(anInterface));
        }
        if (clazz.getSuperclass() != null) {
            classes.add(clazz.getSuperclass());
            classes.addAll(getSuperClassesAndInterfaces(clazz.getSuperclass()));
        }
        return classes;
    }
}
