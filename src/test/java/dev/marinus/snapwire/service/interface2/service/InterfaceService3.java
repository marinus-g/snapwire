package dev.marinus.snapwire.service.interface2.service;

import dev.marinus.snapwire.annotation.Service;
import dev.marinus.snapwire.annotation.Use;
import lombok.Getter;

@Getter
@Service
public class InterfaceService3 {

    private final InterfaceService2 interfaceService2_0;
    private final InterfaceService2 interfaceService2_1;

    public InterfaceService3(@Use(name = "interfaceService2Impl0") InterfaceService2 interfaceService20,
                             @Use(name = "interfaceService2Impl1") InterfaceService2 interfaceService21) {
        interfaceService2_0 = interfaceService20;
        interfaceService2_1 = interfaceService21;
    }
}
