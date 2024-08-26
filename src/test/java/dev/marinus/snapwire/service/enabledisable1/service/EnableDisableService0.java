package dev.marinus.snapwire.service.enabledisable1.service;

import dev.marinus.snapwire.annotation.PostDestroy;
import dev.marinus.snapwire.annotation.PostEnable;
import dev.marinus.snapwire.annotation.PreEnable;
import dev.marinus.snapwire.annotation.Service;
import lombok.Getter;

import javax.annotation.PreDestroy;

@Service
@Getter
public class EnableDisableService0 {

    private String test = null;

    @PreEnable
    public void preEnable() {
        test = "preEnable";
    }

    @PostEnable
    public void postEnable() {
        test = "postEnable";
    }

    @PreDestroy
    public void postDestroy() {
        test = "preDestroy";
    }

    @PostDestroy
    public void preDestroy() {
        test = null;
    }

}
