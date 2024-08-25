package dev.marinus.snapwire.reflect;

import dev.marinus.snapwire.reflect.cache.FieldCache;
import dev.marinus.snapwire.reflect.cache.MethodCache;
import lombok.Getter;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class ReflectLookup {

    private final Map<Class<? extends Annotation>, Set<Class<?>>> cachedAnnotatedClasses = new ConcurrentHashMap<>();
    private final Map<Class<? extends Annotation>, FieldCache> cachedAnnotatedFields = new ConcurrentHashMap<>();
    private final Map<Class<? extends Annotation>, MethodCache> cachedAnnotatedMethods = new ConcurrentHashMap<>();
    private final Reflections reflections;

    public ReflectLookup(Collection<ClassLoader> classLoaders, Collection<String> packages) {
        final FilterBuilder filterBuilder = new FilterBuilder();
        packages.forEach(filterBuilder::includePackage);
        this.reflections = new Reflections(ConfigurationBuilder.build()
                .setScanners(Scanners.TypesAnnotated, Scanners.MethodsAnnotated, Scanners.FieldsAnnotated)
                .addClassLoaders(classLoaders.toArray(new ClassLoader[0]))
                .forPackages(packages.toArray(new String[0]))
                .filterInputsBy(filterBuilder));
    }

    public Set<Class<?>> getAnnotatedClasses(Class<? extends Annotation> annotation) {
        return this.cachedAnnotatedClasses.computeIfAbsent(annotation, k -> this.reflections.getTypesAnnotatedWith(annotation));
    }


    public Collection<Method> getAnnotatedMethods(Class<? extends Annotation> annotation) {
        return this.cachedAnnotatedMethods.computeIfAbsent(annotation,
                        k -> new MethodCache(this.reflections.getMethodsAnnotatedWith(annotation)))
                .getMethods();
    }

    public Collection<Field> getAnnotatedFields(Class<? extends Annotation> annotation, Class<?> clazz) {
        return this.cachedAnnotatedFields.computeIfAbsent(annotation,
                        k -> new FieldCache(this.reflections.getFieldsAnnotatedWith(annotation)))
                .getFields(clazz);
    }


}
