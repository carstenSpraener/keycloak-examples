package de.spraener.keycloak.demo;

import de.spraener.keycloak.demo.payload.LinkRequest;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.keycloak.models.*;
import org.keycloak.services.managers.AuthenticationSessionManager;
import org.keycloak.services.resource.RealmResourceProvider;
import org.keycloak.sessions.AuthenticationSessionModel;
import org.keycloak.sessions.AuthenticationSessionProvider;
import org.keycloak.sessions.RootAuthenticationSessionModel;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Map;
import java.util.logging.Logger;

public class RestDemoResourceProvider implements RealmResourceProvider {
    private static final Logger LOGGER = Logger.getLogger(RestDemoResourceProvider.class.getName());

    private KeycloakSession session;

    public RestDemoResourceProvider(KeycloakSession session) {
        this.session = session;
    }

    @Override
    public Object getResource() {
        return this;
    }

    @Override
    public void close() {
    }

    @GET
    @Path("hello")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get() {
        LOGGER.info("Session-Context URI is: "+this.session.getContext().getUri());
        return Response.ok(Map.of(
                "message","Hello World!",
                "sessionContextUriPath", this.session.getContext().getUri().getPath(true),
                "realm", this.session.getContext().getRealm().getDisplayName(),
                "authLinkASCII", this.session.getContext().getAuthServerUrl().toASCIIString()
        )).build();
    }

    @POST
    @Path("link")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createOneTimeLink(
            LinkRequest req
    ) throws Exception {
        String link = "";
        return Response.ok(Map.of(
                "link", link
        )).build();
    }
}
