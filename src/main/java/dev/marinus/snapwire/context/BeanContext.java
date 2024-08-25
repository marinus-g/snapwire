package dev.marinus.snapwire.context;

import dev.marinus.snapwire.SnapWired;
import dev.marinus.snapwire.annotation.Bean;
import dev.marinus.snapwire.annotation.Component;
import dev.marinus.snapwire.annotation.ComponentHolder;
import dev.marinus.snapwire.annotation.Configuration;
import dev.marinus.snapwire.annotation.Service;
import dev.marinus.snapwire.annotation.WiredPackages;
import dev.marinus.snapwire.component.AbstractComponentHolder;
import dev.marinus.snapwire.component.GenericComponentHolder;
import dev.marinus.snapwire.context.details.BeanDetails;
import dev.marinus.snapwire.context.details.BeanDetailsComparator;
import dev.marinus.snapwire.context.details.ComponentTypeBeanDetails;
import dev.marinus.snapwire.context.details.GenericBeanDetails;
import dev.marinus.snapwire.context.details.MethodBeanDetails;
import dev.marinus.snapwire.context.details.ServiceBeanDetails;
import dev.marinus.snapwire.context.details.TypeBeanDetails;
import dev.marinus.snapwire.context.details.constructor.ConstructorBeanParameterDetails;
import dev.marinus.snapwire.context.details.constructor.MethodConstructorBeanParameterDetails;
import dev.marinus.snapwire.reflect.ReflectLookup;
import dev.marinus.snapwire.validator.ComponentHolderValidator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


@Slf4j
@Getter
@NoArgsConstructor
public class BeanContext {

    @SuppressWarnings("unchecked")
    private static final Class<? extends Annotation>[] BEAN_TYPE_ANNOTATIONS = new Class[]{
            Service.class,
            Component.class,
            Configuration.class
    };

    private final AbstractComponentHolder<Object> genericComponentHolder = new GenericComponentHolder();

    private final Set<BeanDetails> registeredBeans = new HashSet<>();
    private final Map<Class<?>, BeanDetails> registeredBeansMap = new ConcurrentHashMap<>();
    private final Map<Class<?>, AbstractComponentHolder<?>> cachedComponentHolders = new ConcurrentHashMap<>();
    private final BeanContextInitializer initializer = new BeanContextInitializer(this);

    public @Nullable BeanDetails getBeanDetails(Class<?> clazz) {
        return this.registeredBeansMap.get(clazz);
    }

    public void registerWiredParent(SnapWired parent, ClassLoader classLoader) {
        WiredPackages wiredPackages = parent.getClass().getAnnotation(WiredPackages.class);
        final ReflectLookup reflectLookup = new ReflectLookup(Collections.singleton(classLoader),
                Arrays.asList(wiredPackages.value()));
        this.registerBeanDetails(new GenericBeanDetails(parent));
        registerTypeBeans(parent, reflectLookup);
        registerMethodBeans(parent, reflectLookup);
        this.initializer.initialize();
    }

    private void registerMethodBeans(SnapWired parent, ReflectLookup reflectLookup) {
        reflectLookup.getAnnotatedMethods(Bean.class)
                .forEach(method -> {
                    final Bean bean = method.getAnnotation(Bean.class);
                    final Class<?> returnType = method.getReturnType();
                    final MethodBeanDetails methodBeanDetails = new MethodBeanDetails(returnType,
                            getBeanName(returnType, bean), method);
                    methodBeanDetails.setParent(parent);
                    methodBeanDetails.setHolder(this.registeredBeansMap.get(method.getDeclaringClass()));
                    methodBeanDetails.setMethodParameterDetails(new MethodConstructorBeanParameterDetails(method,
                            this));
                    this.registerBeanDetails(methodBeanDetails);
                });
    }

    private void registerTypeBeans(SnapWired parent, ReflectLookup reflectLookup) {
        Arrays.stream(BEAN_TYPE_ANNOTATIONS).forEach(annotation -> {
            reflectLookup.getAnnotatedClasses(annotation).forEach(beanClazz -> {
                TypeBeanDetails beanDetails = null;
                if (annotation.equals(Component.class)) {
                    beanDetails = this.registerComponent(reflectLookup, beanClazz);
                } else if (annotation.equals(Configuration.class)) {
                    //this.registerConfiguration(reflectLookup, beanClazz);
                } else if (annotation.equals(Service.class)) {
                    beanDetails = this.registerService(beanClazz);
                } else {
                    throw new IllegalArgumentException("Unknown bean type");
                }
                if (beanDetails == null) {
                    throw new IllegalArgumentException("Failed to register bean");
                }
                this.registerBeanDetails(beanDetails);
                beanDetails.setConstructorDetails(new ConstructorBeanParameterDetails(beanClazz, this));
                beanDetails.setParent(parent);
            });
        });
    }

    private void registerBeanDetails(final BeanDetails beanDetails) {
        if (this.registeredBeans.contains(beanDetails)) {
            throw new IllegalArgumentException("Bean already registered");
        }
        if (this.registeredBeans.stream()
                .anyMatch(beanDetails1 -> beanDetails1.getName().equals(beanDetails.getName()))) {
            throw new IllegalArgumentException(String.format("Bean with name %s already registered",
                    beanDetails.getName()));
        }
        this.registeredBeans.add(beanDetails);
        if (!this.registeredBeansMap.containsKey(beanDetails.getType())) { // their can be multiple beans with the same type, but if we reference a bean by type, we can only save the first one
            this.registeredBeansMap.put(beanDetails.getType(), beanDetails);
        }
    }

    private TypeBeanDetails registerComponent(ReflectLookup reflectLookup, Class<?> componentClazz) {
        Component component = componentClazz.getAnnotation(Component.class);
        ComponentTypeBeanDetails componentTypeBeanDetails = new ComponentTypeBeanDetails(componentClazz,
                this.getComponentName(componentClazz, component));
        final ComponentHolderValidator validator = new ComponentHolderValidator(componentClazz);
        reflectLookup.getAnnotatedClasses(ComponentHolder.class).forEach(holderClazz -> {
            System.out.println("found component holder " + holderClazz);
            if (validator.isValid(holderClazz.getAnnotation(ComponentHolder.class))) {
                final AbstractComponentHolder<?> componentHolder = this.constructComponentHolder(holderClazz);
                componentTypeBeanDetails.setComponentHolder(componentHolder);
            }
        });
        if (componentTypeBeanDetails.getComponentHolder() == null) {
            componentTypeBeanDetails.setComponentHolder(this.genericComponentHolder);
        }
        return componentTypeBeanDetails;
    }

    /**
     * Construct a component holder instance
     * A component holder should always have an empty constructor
     *
     * @param holderClazz the class of the component holder
     * @return the component holder instance
     */
    private AbstractComponentHolder<?> constructComponentHolder(Class<?> holderClazz) {
        return this.cachedComponentHolders
                .computeIfAbsent(holderClazz, clazz -> {
                    try {
                        return (AbstractComponentHolder<?>) clazz.getConstructor().newInstance();
                    } catch (Exception e) {
                        throw new IllegalArgumentException("Failed to create component holder", e);
                    }
                });
    }

    private TypeBeanDetails registerService(Class<?> genericTypeClazz) {
        final TypeBeanDetails beanDetails = new ServiceBeanDetails(genericTypeClazz,
                this.getServiceName(genericTypeClazz, genericTypeClazz.getAnnotation(Service.class)));
        return beanDetails;
    }

    public <T> T getBean(Class<T> clazz) {
        final BeanDetails beanDetails = this.registeredBeansMap.get(clazz);
        if (beanDetails == null) {
            throw new IllegalArgumentException("Bean not found");
        }
        //noinspection unchecked
        return (T) beanDetails.getBean();
    }

    public <T> T getBean(String name) {
        //noinspection unchecked
        return (T) this.registeredBeans.stream()
                .filter(beanDetails -> beanDetails.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Bean with name not found"))
                .getBean();
    }

    public boolean isBean(Class<?> type) {
        return this.registeredBeansMap.containsKey(type);
    }

    private String getComponentName(Class<?> clazz, Component component) {
        return component.value().isEmpty() ? clazz.getName() : component.value();
    }

    private String getServiceName(Class<?> clazz, Service service) {
        return service.value().isEmpty() ? clazz.getName() : service.value();
    }

    private String getBeanName(Class<?> clazz, Bean bean) {
        return bean.value().isEmpty() ? clazz.getName() : bean.value();
    }

    public Collection<BeanDetails> getUninitializedBeans() {
        return Collections.unmodifiableList(this.registeredBeans
                .stream()
                .filter(beanDetails -> beanDetails.getStage() == BeanDetails.Stage.CREATED)
                .collect(Collectors.toList()));
    }

    public List<BeanDetails> getSortedBeans(SnapWired parent, BeanDetails.Stage stage) {
        return this.registeredBeans
                .stream()
                .filter(beanDetails -> beanDetails.getStage() == stage)
                .filter(beanDetails -> beanDetails.getParent() == parent)
                .sorted(new BeanDetailsComparator())
                .collect(Collectors.toList());
    }

    private AbstractComponentHolder<?> getComponentHolder(Class<?> clazz) {
        return this.cachedComponentHolders.getOrDefault(clazz, genericComponentHolder);
    }

    public void onEnable(SnapWired snapWired) {
        for (BeanDetails sortedBean : getSortedBeans(snapWired, BeanDetails.Stage.INITIALIZED)) {
            System.out.println("Enabling bean " + sortedBean.getName());
            if (sortedBean instanceof ComponentTypeBeanDetails) {
                System.out.println("Enabling component " + sortedBean.getName());
                ComponentTypeBeanDetails componentTypeBeanDetails = (ComponentTypeBeanDetails) sortedBean;
                System.out.println("component onEnable for " + sortedBean);
                AbstractComponentHolder<Object> componentHolder = (AbstractComponentHolder<Object>) componentTypeBeanDetails.getComponentHolder();
                System.out.println("component holder onEnable for " + componentHolder);
                componentHolder.onEnable(sortedBean.getBean());
            }
        }
    }
}
