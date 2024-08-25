package dev.marinus.snapwire.service.cycling.service;

import dev.marinus.snapwire.annotation.Service;

@Service
public class CyclingService1 {

    private final CyclingService1 cyclingService2;

    public CyclingService1(CyclingService1 cyclingService2) {
        this.cyclingService2 = cyclingService2;
    }

}
