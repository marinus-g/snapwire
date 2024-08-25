package dev.marinus.snapwire.component.test1.service;

import dev.marinus.snapwire.component.test1.component.ExampleComponent;
import dev.marinus.snapwire.annotation.Service;
import lombok.Getter;

@Service
@Getter
public class ExampleComponentService {

    private final ExampleComponent exampleComponent;

    public ExampleComponentService(ExampleComponent exampleComponent) {
        this.exampleComponent = exampleComponent;
    }
}
