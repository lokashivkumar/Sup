package com.appdhack.sup.healthchecks;

import com.codahale.metrics.health.HealthCheck;

public class SupHealthCheck extends HealthCheck {

    @Override
    protected Result check() throws Exception {
        return Result.healthy();
    }
}