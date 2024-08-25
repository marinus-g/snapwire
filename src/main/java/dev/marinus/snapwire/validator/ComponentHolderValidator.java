package dev.marinus.snapwire.validator;

import dev.marinus.snapwire.annotation.ComponentHolder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ComponentHolderValidator implements Validator<ComponentHolder> {

    private final Class<?> componentClazz;

    @Override
    public boolean isValid(ComponentHolder componentHolder) {
        for (Class<?> aClass : componentHolder.value()) {
            if (aClass.equals(componentClazz) || aClass.isAssignableFrom(componentClazz)) {
                return true;
            }
        }
        return false;
    }
}
