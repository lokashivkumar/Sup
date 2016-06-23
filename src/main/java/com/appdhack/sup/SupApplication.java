package com.appdhack.sup;

import com.appdhack.sup.healthchecks.SupHealthCheck;
import com.appdhack.sup.resources.SupResource;
import com.appdhack.sup.scheduler.SupScheduler;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SupApplication extends Application<Configuration> {
    private SupScheduler supScheduler;

    public static void main(String[] args) throws Exception {
        new SupApplication().run(args);
    }

    @Override
    public void run(Configuration configuration, Environment environment) throws Exception {
        environment.jersey().register(new SupResource());

        final SupHealthCheck healthCheck = new SupHealthCheck();
        environment.healthChecks().register("test", healthCheck);

        log.info("Starting a scheduler");
        supScheduler = new SupScheduler();
    }
}