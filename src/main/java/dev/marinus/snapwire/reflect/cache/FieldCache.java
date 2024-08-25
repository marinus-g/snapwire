package dev.marinus.snapwire.reflect.cache;

import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class FieldCache {

    @Getter
    private final Collection<Field> staticFields = new HashSet<>();
    private final Map<Class<?>, Collection<Field>> classFields = new ConcurrentHashMap<>();

    public FieldCache(Set<Field> fields) {
        fields.forEach(field -> {
            if (Modifier.isStatic(field.getModifiers())) {
                this.staticFields.add(field);
            } else {
                Class<?> type = field.getDeclaringClass();
                final Collection<Field> declared = this.classFields.computeIfAbsent(type, k -> new HashSet<>());
                declared.add(field);
            }
        });
    }

    public Collection<Field> getFields(Class<?> clazz) {
        return this.classFields.getOrDefault(clazz, Collections.emptySet());
    }
}
