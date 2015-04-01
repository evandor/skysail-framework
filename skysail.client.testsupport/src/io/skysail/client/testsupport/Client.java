package io.skysail.client.testsupport;

import io.skysail.api.links.Link;
import io.skysail.api.links.LinkRelation;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.Header;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.util.Series;

@Slf4j
public class Client<T> {

    public static final String TESTTAG = " > TEST: ";

    private String baseUrl;
    private String credentials;
    private String url;
    private ClientResource cr;
    @Getter
    private Representation currentRepresentation;
    private MediaType mediaType = MediaType.TEXT_HTML;

    public Client(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Client(String baseUrl, String credentials) {
        this.baseUrl = baseUrl;
        this.credentials = credentials;
    }

    public Client(String baseUrl, MediaType mediaType) {
        this.baseUrl = baseUrl;
        this.mediaType = mediaType;
    }

    public Client setUrl(String url) {
        log.info("{}setting browser client url to '{}'", TESTTAG, url);
        this.url = url;
        return this;
    }

    public Representation get() {
        String currentUrl = baseUrl + url;
        log.info("{}issuing GET on '{}', providing credentials {}", TESTTAG, currentUrl, credentials);
        cr = new ClientResource(currentUrl);
        cr.getCookies().add("Credentials", credentials);
        return cr.get(mediaType);
    }

    public Client gotoRoot() {
        url = "/";
        get();
        return this;
    }

    public Representation post(Object entity) {
        log.info("{}issuing POST on '{}', providing credentials {}", TESTTAG, url, credentials);
        cr = new ClientResource(url);
        cr.setFollowingRedirects(false);
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
        return follow(new LinkTitlePredicate(linkTitle, cr.getResponse().getHeaders()));
    }

    public Client followLinkTitleAndRefId(String linkTitle, String refId) {
        Link example = new Link.Builder("").title(linkTitle).refId(refId).build();
        return follow(new LinkByExamplePredicate(example, cr.getResponse().getHeaders()));
    }

    public Client<?> followLinkRelation(LinkRelation linkRelation) {
        return follow(new LinkRelationPredicate(linkRelation, cr.getResponse().getHeaders()));
    }

    public Client<?> followLink(Method method, T entity) {
        return follow(new LinkMethodPredicate(method, cr.getResponse().getHeaders()), method, entity);
    }

    private Client<?> follow(LinkPredicate predicate, Method method, T entity) {
        Series<Header> headers = cr.getResponse().getHeaders();
        String linkheader = headers.getFirstValue("Link");
        List<Link> links = Arrays.stream(linkheader.split(",")).map(l -> Link.valueOf(l)).collect(Collectors.toList());
        Link theLink = getTheOnlyLink(predicate, links);

        boolean isAbsolute = false;
        try {
            URI url2 = new URI(theLink.getUri());
            isAbsolute = url2.isAbsolute();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        url =  isAbsolute ? theLink.getUri() : baseUrl + theLink.getUri();
        log.info("{}url set to '{}'", TESTTAG, url);
        cr = new ClientResource(url);
        cr.getCookies().add("Credentials", credentials);

        if (method != null) {
            if (!(theLink.getVerbs().contains(method))) {
                throw new IllegalStateException("method " + method + " not eligible for link " + theLink);
            }
            if (Method.DELETE.equals(method)) {
                currentRepresentation = cr.delete(mediaType);
            } else if (Method.POST.equals(method)) {
                currentRepresentation = cr.post(entity, mediaType);
            } else if (Method.PUT.equals(method)) {
                currentRepresentation = cr.put(entity, mediaType);    
            } else {
                throw new UnsupportedOperationException();
            }
        } else {
            currentRepresentation = cr.get(mediaType);
        }
        return this;
    }

    private Client follow(LinkPredicate predicate) {
        return follow(predicate, null, null);
    }

    private Link getTheOnlyLink(LinkPredicate predicate, List<Link> links) {
        List<Link> filteredLinks = links.stream().filter(predicate).collect(Collectors.toList());
        if (filteredLinks.size() == 0) {
            throw new IllegalStateException("could not find link for predicate " + predicate);
        }
        if (filteredLinks.size() > 1) {
            throw new IllegalStateException("too many candidates found for predicate " + predicate);
        }
        Link theLink = filteredLinks.get(0);
        return theLink;
    }

    public Reference getLocation() {
        return cr.getLocationRef();
    }

}
