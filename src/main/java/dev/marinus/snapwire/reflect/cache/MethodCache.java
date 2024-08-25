package dev.marinus.snapwire.reflect.cache;

import lombok.Getter;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MethodCache {

    @Getter
    private final Collection<Method> staticMethods = new HashSet<>();
    private final Collection<Method> classMethods = new HashSet<>();

    public MethodCache(Collection<Method> methods) {
        methods.forEach(method -> {
            if (method.isBridge() || method.isSynthetic()) {
                return;
            }
            if (Modifier.isStatic(method.getModifiers())) {
                this.staticMethods.add(method);
            } else {
                this.classMethods.add(method);
            }
        });
    }

    public Collection<Method> getMethods() {
        return this.classMethods;
    }
}
