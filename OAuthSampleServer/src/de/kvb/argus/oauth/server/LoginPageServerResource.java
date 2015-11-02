package de.kvb.argus.oauth.server;

import java.util.HashMap;

import org.restlet.data.MediaType;
import org.restlet.ext.freemarker.*;
import org.restlet.ext.oauth.*;
import org.restlet.representation.*;
import org.restlet.resource.*;
import org.restlet.security.SecretVerifier;

import freemarker.template.Configuration;

public class LoginPageServerResource extends AuthorizationBaseServerResource {

    @Get("html")
    @Post("html")
    public Representation getPage() throws OAuthException {
        getLogger().info("Get Login");
        String userId = getQueryValue("user_id");
        HashMap<String, Object> data = new HashMap<String, Object>();
        if (userId != null && !userId.isEmpty()) {
            String password = getQueryValue("password");
            getLogger().info("User=" + userId + ", Pass=" + password);
            SampleUser sampleUser = OAuth2Sample.getSampleUserManager().findUserById(userId);
            if (sampleUser == null) {
                data.put("error", "Authentication failed.");
                data.put("error_description", "ID is invalid.");
            } else {
                boolean result = SecretVerifier.compare(password.toCharArray(), sampleUser.getPassword());
                if (result) {
                    getAuthSession().setScopeOwner(userId);
                    String uri = getQueryValue("continue");
                    getLogger().info("URI: " + uri);
                    redirectTemporary(uri);
                    return new EmptyRepresentation();
                } else {
                    data.put("error", "Authentication failed.");
                    data.put("error_description", "Password is invalid.");
                }
            }
        }

        String continueURI = getQueryValue("continue");
        TemplateRepresentation response = getLoginPage("login.html");
        data.put("continue", continueURI);
        response.setDataModel(data);

        return response;
    }

    protected TemplateRepresentation getLoginPage(String loginPage) {
        Configuration config = new Configuration();
        config.setTemplateLoader(new ContextTemplateLoader(getContext(), "clap:///"));
        getLogger().fine("loading: " + loginPage);
        return new TemplateRepresentation(loginPage, config, MediaType.TEXT_HTML);
    }
}
