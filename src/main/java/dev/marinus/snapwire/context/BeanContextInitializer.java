package dev.marinus.snapwire.context;

import dev.marinus.snapwire.context.details.BeanDetails;
import dev.marinus.snapwire.context.details.MethodBeanDetails;
import dev.marinus.snapwire.context.details.TypeBeanDetails;
import dev.marinus.snapwire.validator.CyclingDependencyValidator;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

@Slf4j
public class BeanContextInitializer {

    private final BeanContext beanContext;
    private final CyclingDependencyValidator cyclingDependencyValidator;

    public BeanContextInitializer(BeanContext beanContext) {
        this.beanContext = beanContext;
        this.cyclingDependencyValidator = new CyclingDependencyValidator(beanContext);
    }

    public void initialize() {
        final Collection<BeanDetails> uninitializedBeans = beanContext.getUninitializedBeans();
        uninitializedBeans.forEach(beanDetails -> {
            this.initializeBeanConstructor(beanDetails);
            this.queryDependencies(beanDetails);
        });
        for (BeanDetails uninitializedBean : uninitializedBeans) {
            if (!this.cyclingDependencyValidator.isValid(uninitializedBean)) {
                throw new IllegalArgumentException("Cycling dependency detected for bean " + uninitializedBean.getName());
            }
        }
        uninitializedBeans.stream()
                .filter(details -> !isInitialized(details))
                .forEach(this::initializeBean);
        uninitializedBeans.forEach(beanContext::onPreEnable);
        final BeanContextAutowireInjector injector = new BeanContextAutowireInjector(beanContext);
        injector.inject();
    }


    private void initializeBean(BeanDetails details) {
        if (isInitialized(details)) {
            return;
        }
        if (!details.getDependencies().isEmpty()) {
            details.getDependencies().forEach(this::initializeBean);
        }
        details.getDependencies().forEach(beanDetails -> beanDetails.addChildren(details));
        if (details.getStage() == BeanDetails.Stage.PRE_INITIALIZED) { // bean already initialized
            return;
        }
        log.info("Initializing bean {}", details.getType().getName());
        if (details instanceof TypeBeanDetails) {
            TypeBeanDetails typeBeanDetails = (TypeBeanDetails) details;
            Object bean = typeBeanDetails.getConstructorDetails().newInstance();
            details.setBean(bean);
        } else if (details instanceof MethodBeanDetails) {
            MethodBeanDetails methodBeanDetails = (MethodBeanDetails) details;
            Object bean = methodBeanDetails.getMethodParameterDetails().invoke(methodBeanDetails.getHolder().getBean(), beanContext);
            details.setBean(bean);
        }
    }

    private boolean isInitialized(BeanDetails details) {
        return details.getStage() == BeanDetails.Stage.PRE_INITIALIZED || details.getStage() == BeanDetails.Stage.PRE_ENABLE_CALLED;
    }

    private void queryDependencies(BeanDetails details) {
        if (details instanceof TypeBeanDetails) {
            TypeBeanDetails typeBeanDetails = (TypeBeanDetails) details;
            for (BeanDetails parameterDetail : typeBeanDetails.getConstructorDetails()
                    .getParameterDetails(beanContext)) {
                details.addDependency(parameterDetail);
            }
        } else if (details instanceof MethodBeanDetails) {
            MethodBeanDetails methodBeanDetails = (MethodBeanDetails) details;
            details.addDependency(methodBeanDetails.getHolder());
            for (BeanDetails parameterDetail : methodBeanDetails.getMethodParameterDetails()
                    .getParameterDetails(beanContext)) {
                details.addDependency(parameterDetail);
            }
        }
    }

    private void initializeBeanConstructor(BeanDetails details) {
        if (details instanceof TypeBeanDetails) {
            TypeBeanDetails typeBeanDetails = (TypeBeanDetails) details;
            typeBeanDetails.getConstructorDetails().load();
        } else if (details instanceof MethodBeanDetails) {
            MethodBeanDetails methodBeanDetails = (MethodBeanDetails) details;
            methodBeanDetails.getMethodParameterDetails().load();
        }
    }
}