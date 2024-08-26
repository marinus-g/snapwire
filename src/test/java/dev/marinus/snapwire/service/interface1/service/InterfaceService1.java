package dev.marinus.snapwire.service.interface1.service;

import dev.marinus.snapwire.annotation.Service;
import lombok.Getter;

@Service
@Getter
public class InterfaceService1 {

    private final InterfaceService0 interfaceService0;


    public InterfaceService1(InterfaceService0 interfaceService0) {
        this.interfaceService0 = interfaceService0;
    }
}
