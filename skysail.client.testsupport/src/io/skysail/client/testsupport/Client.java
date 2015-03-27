package io.skysail.client.testsupport;

import io.skysail.api.links.Link;
import io.skysail.api.links.LinkRelation;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.Header;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.util.Series;

public class Client {

    private String baseUrl;
    private String credentials;
    private String url;
    private ClientResource cr;
    
    private class LinkRelationPredicate<Link> implements Predicate {

        private String info;

        public LinkRelationPredicate(String info) {
            this.info = info;
        }
        
        @Override
        public boolean test(Object l) {
            return false;// l.getRel().equals(linkRelation);
        }
        
        @Override
        public String toString() {
            return super.toString();
        }
        
    }

    public Client(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Client(String baseUrl, String credentials) {
        this.baseUrl = baseUrl;
        this.credentials = credentials;
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
        cr = new ClientResource(url);
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
        cr = new ClientResource(baseUrl + "/");
        cr.getCookies().add("Credentials", credentials);
        cr.get(MediaType.TEXT_HTML);
        return this;
    }

    public Client followLinkTitle(String linkTitle) {
        Predicate<? super Link> predicate = l -> {
            return l.getTitle().equals(linkTitle);
        };
        return follow(predicate);
    }

    public Client followLinkRelation(LinkRelation linkRelation) {
        Predicate<? super Link> predicate = l -> {
            return l.getRel().equals(linkRelation);
        };
        
        return follow(new LinkRelationPredicate("hi"));
    }

    private Client follow(Predicate<? super Link> predicate) {
        Series<Header> headers = cr.getResponse().getHeaders();
        String linkheader = headers.getFirstValue("Link");
        List<Link> links = Arrays.stream(linkheader.split(",")).map(l -> Link.valueOf(l)).collect(Collectors.toList());
        Supplier<RuntimeException> exception = new Supplier<RuntimeException>() {

            @Override
            public RuntimeException get() {
                return new IllegalStateException("could not follow " + predicate.toString());
            }
        };
        Link theLink = links.stream().filter(predicate).findFirst().orElseThrow(exception);
        url = baseUrl + theLink.getUri();
        return this;
    }
}
