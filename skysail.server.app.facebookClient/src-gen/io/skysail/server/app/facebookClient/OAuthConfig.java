package io.skysail.server.app.facebookClient;

import javax.persistence.Id;

import java.util.ArrayList;
import java.util.List;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.*;
import io.skysail.server.forms.*;

import org.apache.commons.lang3.StringUtils;

public class OAuthConfig implements Identifiable {

    @Id
    private String id;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    // --- fields ---

    @Field(inputType = InputType.TEXT, htmlPolicy = HtmlPolicy.NO_HTML)
    private String cliendId;

    public void setCliendId(String value) {
        this.cliendId = value;
    }

    public String getCliendId() {
        return this.cliendId;
    }

    @Field(inputType = InputType.TEXT, htmlPolicy = HtmlPolicy.NO_HTML)
    private String clientSecret;

    public void setClientSecret(String value) {
        this.clientSecret = value;
    }

    public String getClientSecret() {
        return this.clientSecret;
    }

    @Field(inputType = InputType.TEXT, htmlPolicy = HtmlPolicy.NO_HTML)
    private String redirectUrl;

    public void setRedirectUrl(String value) {
        this.redirectUrl = value;
    }

    public String getRedirectUrl() {
        return this.redirectUrl;
    }


    // --- relations ---



}