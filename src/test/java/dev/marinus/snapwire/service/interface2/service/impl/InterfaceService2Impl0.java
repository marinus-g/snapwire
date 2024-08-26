package dev.marinus.snapwire.service.interface2.service.impl;

import dev.marinus.snapwire.annotation.Service;
import dev.marinus.snapwire.service.interface2.service.InterfaceService2;

@Service("interfaceService2Impl0")
public class InterfaceService2Impl0 implements InterfaceService2 {
    @Override
    public String getSomething() {
        return "InterfaceService2Impl0";
    }
}
