package io.skysail.client.testsupport;

import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

public class Client {

    private String baseUrl;
    private String credentials;
    private String url;
    private ClientResource cr;

    public Client(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Client setUrl(String url) {
        this.url = url;
        return this;
    }

    public Representation get(MediaType mediaType) {
        cr = new ClientResource(baseUrl + url);
        cr.getCookies().add("Credentials", credentials);
        return cr.get(mediaType);
    }

    public Representation post(Object entity, MediaType mediaType) {
        cr = new ClientResource(baseUrl + url);
        cr.getCookies().add("Credentials", credentials);
        return cr.post(entity, mediaType);
    }

    public Response getResponse() {
        return cr.getResponse();
    }

    public Client loginAs(String username, String password) {
        cr = new ClientResource(baseUrl + "/_logout?targetUri=/");
        cr.get();
        cr = new ClientResource(baseUrl + "/_login");
        cr.setFollowingRedirects(true);
        Form form = new Form();
        form.add("username", username);
        form.add("password", password);
        cr.post(form, MediaType.TEXT_HTML);
        credentials = cr.getResponse().getCookieSettings().getFirstValue("Credentials");
        return this;
    }

}
