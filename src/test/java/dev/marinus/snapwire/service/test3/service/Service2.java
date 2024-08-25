package dev.marinus.snapwire.service.test3.service;

import dev.marinus.snapwire.annotation.Service;
import lombok.Getter;

@Getter
@Service
public class Service2 {


    private final TestBean testBean;

    public Service2(TestBean testBean) {
        this.testBean = testBean;
    }
}
