package cn.caofanqi.service.impl;

import cn.caofanqi.service.EntitlementCalculationService;

public class StubEntitlementCalculationService implements EntitlementCalculationService {

    @Override
    public void calculateEntitlement() {
        try {
            System.out.println("sleep");
            Thread.sleep(1234);
            System.out.println("hello");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}