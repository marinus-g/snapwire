package dev.marinus.snapwire.service.interface1.service.impl;

import dev.marinus.snapwire.annotation.Service;
import dev.marinus.snapwire.service.interface1.service.InterfaceService0;

@Service
public class InterfaceService0Impl implements InterfaceService0 {
    @Override
    public String getSomething() {
        return "InterfaceService1Impl";
    }
}
