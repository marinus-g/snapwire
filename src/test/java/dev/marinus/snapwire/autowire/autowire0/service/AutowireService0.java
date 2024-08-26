package dev.marinus.snapwire.autowire.autowire0.service;

import dev.marinus.snapwire.annotation.Autowired;
import dev.marinus.snapwire.annotation.Service;
import dev.marinus.snapwire.autowire.autowire0.configuration.AutowireConfiguration0;
import lombok.Getter;

@Service
@Getter
public class AutowireService0 {

    @Autowired
    private AutowireConfiguration0 configuration;

}
