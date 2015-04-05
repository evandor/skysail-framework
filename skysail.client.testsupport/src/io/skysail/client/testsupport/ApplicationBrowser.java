package io.skysail.client.testsupport;

import lombok.extern.slf4j.Slf4j;

import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Status;

@Slf4j
public class ApplicationBrowser<T extends ApplicationBrowser<?, U>, U> {

    protected static final String HOST = "http://localhost";
    protected static final String PORT = "2014";

    protected MediaType mediaType;
    protected ApplicationClient<U> client;

    private String defaultUser = null;

    public ApplicationBrowser(String url) {
        this(url, MediaType.TEXT_HTML);
    }

    public ApplicationBrowser(String appName, MediaType mediaType) {
        this.mediaType = mediaType;
        String url = getBaseUrl();
        log.info("{}creating new browser client with url '{}' for Application '{}' and mediaType '{}'", ApplicationClient.TESTTAG,
                url, appName, MediaType.TEXT_HTML);
        client = new ApplicationClient<U>(url, appName, mediaType);
    }

    protected static String getBaseUrl() {
        return HOST + (PORT != null ? ":" + PORT : "");
    }

    protected void login() {
        log.info("{}logging in as user '{}'", ApplicationClient.TESTTAG, defaultUser);
        client.loginAs(defaultUser, "skysail");
    }
    
    @SuppressWarnings("unchecked")
    public T asUser(String username) {
        this.defaultUser = username;
        login();
        return (T) this;
    }

    public void setUser(String defaultUser) {
        this.defaultUser = defaultUser;
    }

    
    public Status getStatus() {
        return client.getResponse().getStatus();
    }
    
    protected void findAndDelete(String id) {
        client.gotoAppRoot()
            .followLinkTitleAndRefId("update", id)
            .followLink(Method.DELETE);
    }

}
