package dev.marinus.snapwire.component.test1.service;

import dev.marinus.snapwire.annotation.Bean;
import dev.marinus.snapwire.annotation.Service;

@Service
public class ExampleService {

    @Bean
    public ExampleBean getExampleBean() {
        return new ExampleBean();
    }
}