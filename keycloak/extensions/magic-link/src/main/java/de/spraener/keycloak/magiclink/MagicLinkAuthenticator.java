package de.spraener.keycloak.magiclink;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.common.util.KeycloakUriBuilder;
import org.keycloak.email.EmailException;
import org.keycloak.email.EmailTemplateProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.UserProvider;
import org.keycloak.models.utils.KeycloakModelUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MagicLinkAuthenticator implements Authenticator {
    private static final Logger LOGGER = Logger.getLogger(MagicLinkAuthenticator.class.getName());

    private static final String SESSION_KEY = "magic-email-key";
    private static final String QUERY_PARAM = "magickey";
    private static final String MAGIC_LINK_TEMPLATE = "magic-link.ftl";

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        String sessionKey = context.getAuthenticationSession().getAuthNote(SESSION_KEY);
        if (sessionKey != null) {
            String requestKey = context.getHttpRequest().getUri().getQueryParameters().getFirst(QUERY_PARAM);
            if (requestKey != null) {
                if (requestKey.equals(sessionKey)) {
                    context.success();
                } else {
                    context.failure(AuthenticationFlowError.INVALID_CREDENTIALS);
                }
            } else {
                displayMagicLinkSuccessPage(context);
            }
        } else if( isOneTimeLink(context) ) {
            String userID = context.getHttpRequest().getUri().getQueryParameters().getFirst("cs-user");
            UserProvider up = context.getSession().getProvider(UserProvider.class);
            UserModel userModel = up.getUserByEmail(context.getRealm(), userID);
            context.setUser(userModel);
            context.success();
        } else {
            sendMagicLink(context);
        }
    }

    private boolean isOneTimeLink(AuthenticationFlowContext context) {
        return context.getHttpRequest().getUri().getQueryParameters().getFirst("cs-user")!= null;
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        LOGGER.info("MagicLinkAuthenticator.action("+context+")");
    }

    private void sendMagicLink(AuthenticationFlowContext context) {
        RealmModel realm = context.getRealm();
        UserModel user = context.getUser();
        if (user == null) {
            displayMagicLinkSuccessPage(context);
            return;
        }

        String key = KeycloakModelUtils.generateId();
        context.getAuthenticationSession().setAuthNote(SESSION_KEY, key);

        String link = KeycloakUriBuilder.fromUri(context.getRefreshExecutionUrl()).queryParam(QUERY_PARAM, key).build().toString();
        LOGGER.info("Created MagicLink is: "+link);

        EmailTemplateProvider emailTemplateProvider = context.getSession().getProvider(EmailTemplateProvider.class);
        emailTemplateProvider.setRealm(realm);
        emailTemplateProvider.setUser(user);

        // for further processing we need a mutable map here
        Map<String, Object> msgParams = new HashMap<>();
        msgParams.put("name", user.getUsername());
        msgParams.put("link", link);

        try {
            emailTemplateProvider.send("magicLinkEmailSubject", MAGIC_LINK_TEMPLATE, msgParams);
            displayMagicLinkSuccessPage(context);
        } catch (EmailException e) {
            LOGGER.log(Level.SEVERE,"Exception during sending email occurred.", e);
            context.failure(AuthenticationFlowError.INTERNAL_ERROR);
        }
    }

    private void displayMagicLinkSuccessPage(AuthenticationFlowContext context) {
        context.challenge(
                context.form()
                        .setInfo("magicLinkText")
                        .createInfoPage()
        );
    }

    @Override
    public boolean requiresUser() {
        return false;
    }

    @Override
    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
        return true;
    }

    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {
    }

    @Override
    public void close() {
    }

}
