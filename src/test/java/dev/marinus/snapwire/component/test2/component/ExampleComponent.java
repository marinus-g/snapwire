package dev.marinus.snapwire.component.test2.component;

import dev.marinus.snapwire.annotation.Component;
import dev.marinus.snapwire.component.test2.ExampleInterface;

@Component
public class ExampleComponent implements ExampleInterface {

    private String name = null;

    @Override
    public String getComponentName() {
        return this.name;
    }

    @Override
    public void setComponentName(String name) {
        this.name = name;
    }
}
