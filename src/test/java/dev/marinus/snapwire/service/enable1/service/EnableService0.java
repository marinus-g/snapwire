package dev.marinus.snapwire.service.enable1.service;

import dev.marinus.snapwire.annotation.PostEnable;
import dev.marinus.snapwire.annotation.PreEnable;
import dev.marinus.snapwire.annotation.Service;
import lombok.Getter;

@Getter
@Service
public class EnableService0 {

    private String status = null;

    @PreEnable
    public void preEnable() {
        status = "preEnabled";
    }

    @PostEnable
    public void postEnable() {
        status = "postEnabled";
    }
}
