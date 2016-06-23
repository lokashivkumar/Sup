package com.appdhack.sup.resources;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Path("/slackservice")
public class ExternalServiceResource {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(ExternalServiceResource.class);
    HttpClient client;
    private final String slackToken = "xoxp-53450742471-53446019440-53511957973-3a16fb0426";
    private final String botToken = "xoxb-53472513298-zi8L5Dao0Ztx1FGXfwjbI3s3";

    public ExternalServiceResource(HttpClient httpClient) {
        this.client = httpClient;
    }

    @GET
    public Response getSlackTeam() throws IOException {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https").setHost("slack.com").setPath("/api/team.info")
                .setParameter("token", slackToken);
        URI uri = null;
        try {
            uri = builder.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        System.out.println(uri.toString());

        HttpGet httpget = new HttpGet(uri);
        HttpResponse response = null;
        try {
            response = client.execute(httpget);
            logger.info("Response is: " +response.getEntity().getContentType());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Response.ok(response.getEntity().getContent()).build();
    }

    @GET
    public Response getBotInfo() {

        return Response.ok().build();
    }
}
