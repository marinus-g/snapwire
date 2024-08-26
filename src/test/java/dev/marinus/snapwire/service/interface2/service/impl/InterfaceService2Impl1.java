package dev.marinus.snapwire.service.interface2.service.impl;

import dev.marinus.snapwire.annotation.Service;
import dev.marinus.snapwire.service.interface2.service.InterfaceService2;

@Service("interfaceService2Impl1")
public class InterfaceService2Impl1 implements InterfaceService2 {
    @Override
    public String getSomething() {
        return "InterfaceService2Impl1";
    }
}
