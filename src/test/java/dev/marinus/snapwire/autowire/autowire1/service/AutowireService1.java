package dev.marinus.snapwire.autowire.autowire1.service;

import dev.marinus.snapwire.annotation.Autowired;
import dev.marinus.snapwire.annotation.Service;
import dev.marinus.snapwire.autowire.autowire1.component.AutowireComponent0;
import dev.marinus.snapwire.autowire.autowire1.component.AutowireComponent1;
import dev.marinus.snapwire.autowire.autowire1.configuration.AutowireConfiguration1;
import lombok.Getter;

@Service
@Getter
public class AutowireService1 {

    private AutowireConfiguration1 configuration;

    private AutowireComponent0 component0;
    private AutowireComponent1 component1;

    @Autowired
    public void setConfiguration(AutowireConfiguration1 configuration) {
        this.configuration = configuration;
    }

    @Autowired
    public void setComponents(AutowireComponent0 component0, AutowireComponent1 component1) {
        this.component0 = component0;
        this.component1 = component1;
    }
}
