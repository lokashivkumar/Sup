package com.appdhack.sup;

import be.tomcools.dropwizard.websocket.WebsocketBundle;
import com.appdhack.sup.healthchecks.SupHealthCheck;
import com.appdhack.sup.resources.SupResource;
import com.appdhack.sup.scheduler.SupScheduler;
import com.appdhack.sup.slack.SlackRTMEndpoint;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.ClientEndpoint;
import javax.websocket.server.ServerEndpointConfig;

@Slf4j
@ClientEndpoint
public class SupApplication extends Application<SupApplicationConfiguration> {
    private SupScheduler supScheduler;

    public static void main(String[] args) throws Exception {
        new SupApplication().run(args);
    }

    private WebsocketBundle websocket = new WebsocketBundle();

    @Override
    public void initialize(Bootstrap<SupApplicationConfiguration> bootstrap) {
        super.initialize(bootstrap);
        bootstrap.addBundle(websocket);
    }


    @Override
    public void run(SupApplicationConfiguration configuration, Environment environment) throws Exception {
        environment.jersey().register(new SupResource());

        final SupHealthCheck healthCheck = new SupHealthCheck();
        environment.healthChecks().register("test", healthCheck);

        log.info("Starting a scheduler");
        supScheduler = new SupScheduler();

        websocket.addEndpoint(SlackRTMEndpoint.class);
//        //programmatic endpoint
//        ServerEndpointConfig serverEndpointConfig = ServerEndpointConfig.Builder.create(SlackRTMEndpoint.class, "/slackrtm").build();
//        websocket.addEndpoint(serverEndpointConfig);
    }
}
