package dev.marinus.snapwire.context;

import dev.marinus.snapwire.annotation.PostDestroy;
import dev.marinus.snapwire.annotation.PostEnable;
import dev.marinus.snapwire.annotation.PreEnable;
import dev.marinus.snapwire.context.details.BeanDetails;
import dev.marinus.snapwire.context.details.MethodBeanDetails;
import dev.marinus.snapwire.context.details.TypeBeanDetails;
import dev.marinus.snapwire.context.details.constructor.MethodConstructorBeanParameterDetails;
import lombok.Data;

import javax.annotation.Nullable;
import javax.annotation.PreDestroy;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

public class TypeBeanLoader {

    private static final StageAnnotation[] STAGE_ANNOTATIONS = {
            new StageAnnotation(BeanDetails.Stage.PRE_INITIALIZED, PreEnable.class),
            new StageAnnotation(BeanDetails.Stage.PRE_ENABLE_CALLED, PostEnable.class),
            new StageAnnotation(BeanDetails.Stage.INITIALIZED, PreDestroy.class),
            new StageAnnotation(BeanDetails.Stage.PRE_DESTROYED, PostDestroy.class)
    };

    private final TypeBeanDetails beanDetails;
    private final BeanContext beanContext;

    public TypeBeanLoader(TypeBeanDetails beanDetails, BeanContext context) {
        this.beanDetails = beanDetails;
        this.beanContext = context;
    }

    public void load() {
        Arrays.stream(beanDetails.getType()
                .getMethods())
                .filter(this::isStageMethod)
                .map(this::createMethodBeanDetails)
                .forEach(o -> beanDetails.addMethodDetails(o.getStage(), o));

    }

    private MethodBeanDetails createMethodBeanDetails(Method method) {
        final BeanDetails.Stage stage = getStageFromMethod(method);
        if (stage == null) {
            throw new IllegalArgumentException("Method is not a stage method"); // this should never be the case since we filter before?
        }
        if (!isValidMethodConstructor(stage, method)) {
            throw new IllegalArgumentException("Invalid method constructor for stage " + getStageFromMethod(method));
        }
        if (method.getReturnType() != void.class) {
            throw new IllegalArgumentException(String.format("Method %s must return void, but returns %s", method.getName(), method.getReturnType().getName()));
        }
        MethodBeanDetails details = new MethodBeanDetails(void.class, method.getName(), method);
        details.setMethodParameterDetails(new MethodConstructorBeanParameterDetails(method, beanContext));
        details.setHolder(beanDetails);
        details.setStage(stage);
        return details;
    }

    private boolean isStageMethod(Method method) {
        return Arrays.stream(STAGE_ANNOTATIONS)
                .anyMatch(stageAnnotation -> method.isAnnotationPresent(stageAnnotation.getAnnotation()));
    }

    public @Nullable BeanDetails.Stage getStageFromMethod(Method method) {
        return Arrays.stream(STAGE_ANNOTATIONS)
                .filter(stageAnnotation -> method.isAnnotationPresent(stageAnnotation.getAnnotation()))
                .map(StageAnnotation::getStage)
                .findFirst()
                .orElse(null);
    }

    private boolean isValidMethodConstructor(BeanDetails.Stage stage, Method method) {
        switch (stage) {
            case PRE_INITIALIZED:
            case PRE_DESTROYED:
                return method.getParameterCount() == 0;
            case PRE_ENABLE_CALLED:
            case INITIALIZED:
                return true;
            default:
                return false;
        }
    }

    @Data
    private static class StageAnnotation {
        private final BeanDetails.Stage stage;
        private final Class<? extends Annotation> annotation;
    }
}
