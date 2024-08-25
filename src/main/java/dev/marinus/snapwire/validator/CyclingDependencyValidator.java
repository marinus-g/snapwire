package dev.marinus.snapwire.validator;

import dev.marinus.snapwire.context.BeanContext;
import dev.marinus.snapwire.context.details.BeanDetails;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * Validates if a bean has a cycling dependency.
 */
@RequiredArgsConstructor
public class CyclingDependencyValidator implements BeanValidator {

    private final BeanContext beanContext;

    /**
     * Validates if a bean has a cycling dependency.
     *
     * @param beanDetails the bean to validate
     * @return true if the bean does not have a cycling dependency, false otherwise
     */
    @Override
    public boolean isValid(BeanDetails beanDetails) {
        return !hasCyclingDependency(beanDetails);
    }

    private boolean hasCyclingDependency(BeanDetails beanDetails) {
        return hasCyclingDependencies(beanDetails, new HashSet<>());
    }

    private boolean hasCyclingDependencies(BeanDetails beanDetails, Set<BeanDetails> path) {
        if (!path.add(beanDetails)) {
            return true;
        }
        System.out.println("Checking for cycling dependency for bean " + beanDetails);
        for (BeanDetails dependency : beanDetails.getDependencies()) {
            if (hasCyclingDependencies(dependency, path)) {
                return true;
            }
        }
        path.remove(beanDetails);
        return false;
    }
}