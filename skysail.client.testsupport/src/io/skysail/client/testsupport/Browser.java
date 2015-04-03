package io.skysail.client.testsupport;

import lombok.extern.slf4j.Slf4j;

import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Status;

@Slf4j
public class Browser<T extends Browser<?, U>, U> {

    protected static final String HOST = "http://localhost";
    protected static final String PORT = "2015";

    protected MediaType mediaType;
    protected Client<U> client;
    private String appName;

    public Browser(String url) {
        this(url, MediaType.TEXT_HTML);
    }

    public Browser(String appName, MediaType mediaType) {
        this.appName = appName;
        this.mediaType = mediaType;
        String url = getBaseUrl();
        log.info("{}creating new browser client with url '{}' for Application '{}' and mediaType '{}'", Client.TESTTAG,
                url, appName, MediaType.TEXT_HTML);
        client = new Client<U>(url, mediaType);
    }

    public Browser(MediaType mediaType) {
        this(getBaseUrl(), mediaType);
    }

    protected static String getBaseUrl() {
        return HOST + (PORT != null ? ":" + PORT : "");
    }

    @SuppressWarnings("unchecked")
    public T asUser(String username) {
        log.info("{}logging in as user '{}'", Client.TESTTAG, username);
        client.loginAs(username, "skysail");
        return (T) this;
    }

    public Status getStatus() {
        return client.getResponse().getStatus();
    }
    
    protected void findAndDelete(String id) {
        client.gotoRoot()
            .followLinkTitle(appName)
            .followLinkTitleAndRefId("update", id)
            .followLink(Method.DELETE);
    }

}
