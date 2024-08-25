package dev.marinus.snapwire.component.test1.component;

import dev.marinus.snapwire.component.test1.service.ExampleBean;
import dev.marinus.snapwire.annotation.Component;
import lombok.Getter;

@Component
@Getter
public class ExampleComponent {

    private final ExampleBean exampleBean;

    public ExampleComponent(ExampleBean exampleBean) {
        this.exampleBean = exampleBean;
    }
}
