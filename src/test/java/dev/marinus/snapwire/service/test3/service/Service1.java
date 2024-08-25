package dev.marinus.snapwire.service.test3.service;

import dev.marinus.snapwire.annotation.Bean;
import dev.marinus.snapwire.annotation.Service;
import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public class Service1 {

    @Bean
    public TestBean testBean(Service0 service0) {
        return new TestBean(service0);
    }
}