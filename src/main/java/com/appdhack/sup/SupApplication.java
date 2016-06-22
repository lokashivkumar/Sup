package com.appdhack.sup;

import com.appdhack.sup.healthchecks.SupHealthCheck;
import com.appdhack.sup.resources.SupResource;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SupApplication extends Application<Configuration> {

    public static final Logger logger = LoggerFactory.getLogger(SupApplication.class);

    public static void main(String[] args) throws Exception {
        new SupApplication().run(args);
    }

    @Override
    public void run(Configuration configuration, Environment environment) throws Exception {
        environment.jersey().register(new SupResource());

        final SupHealthCheck healthCheck = new SupHealthCheck();
        environment.healthChecks().register("test", healthCheck);
    }
}
