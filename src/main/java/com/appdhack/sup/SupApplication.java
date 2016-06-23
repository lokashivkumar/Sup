package com.appdhack.sup;

import com.appdhack.sup.healthchecks.SupHealthCheck;
import com.appdhack.sup.resources.ExternalServiceResource;
import com.appdhack.sup.resources.SupResource;
import com.appdhack.sup.scheduler.SupScheduler;
import io.dropwizard.Application;
import io.dropwizard.client.HttpClientBuilder;
import io.dropwizard.setup.Environment;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;

@Slf4j
public class SupApplication extends Application<SupApplicationConfiguration> {
    private SupScheduler supScheduler;
    public static void main(String[] args) throws Exception {
        new SupApplication().run(args);
    }

    @Override
    public void run(SupApplicationConfiguration configuration, Environment environment) throws Exception {
        environment.jersey().register(new SupResource());

        final SupHealthCheck healthCheck = new SupHealthCheck();
        environment.healthChecks().register("test", healthCheck);

        log.info("Starting a scheduler");
        supScheduler = new SupScheduler();

        final HttpClient httpClient = new HttpClientBuilder(environment)
                .using(configuration.getHttpClientConfiguration())
                .build(getName());
        environment.jersey().register(new ExternalServiceResource(httpClient));
    }
}
