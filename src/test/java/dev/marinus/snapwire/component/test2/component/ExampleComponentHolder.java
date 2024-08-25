package dev.marinus.snapwire.component.test2.component;


import dev.marinus.snapwire.annotation.ComponentHolder;
import dev.marinus.snapwire.component.AbstractComponentHolder;
import dev.marinus.snapwire.component.test2.ExampleInterface;

@ComponentHolder(ExampleInterface.class)
public class ExampleComponentHolder extends AbstractComponentHolder<ExampleInterface> {

    private static final String COMPONENT_NAME = "ExampleComponent";

    @Override
    public void onEnable(ExampleInterface exampleInterface) {
        exampleInterface.setComponentName(COMPONENT_NAME);
    }
}
