package dev.marinus.snapwire.service.named1.service;

import dev.marinus.snapwire.annotation.Service;
import dev.marinus.snapwire.annotation.Use;
import lombok.Getter;

@Service("named1Service1")
@Getter
public class Named1Service1 {

    private final Object object;

    public Named1Service1(@Use(name = "named1Service0") Object object) {
        this.object = object;
    }
}
