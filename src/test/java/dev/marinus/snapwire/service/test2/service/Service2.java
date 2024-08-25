package dev.marinus.snapwire.service.test2.service;

import dev.marinus.snapwire.annotation.Service;
import lombok.Getter;

@Service
@Getter
public class Service2 {

    private final Service1 service1;

    public Service2(Service1 service1) {
        this.service1 = service1;
    }
}
