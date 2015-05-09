package io.skysail.client.testsupport;

import io.skysail.api.links.*;
import io.skysail.server.restlet.resources.SkysailServerResource;

import java.net.*;
import java.util.*;
import java.util.stream.Collectors;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import org.restlet.Response;
import org.restlet.data.*;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.util.Series;

@Slf4j
public class ApplicationClient<T> {

    public static final String TESTTAG = " > TEST: ";

    private String baseUrl;
    private String credentials;
    private String url;
    private ClientResource cr;
    @Getter
    private Representation currentRepresentation;
    private MediaType mediaType = MediaType.TEXT_HTML;
    private String appName;

    public ApplicationClient(@NonNull String baseUrl, @NonNull String appName, @NonNull MediaType mediaType) {
        this.baseUrl = baseUrl;
        this.appName = appName;
        this.mediaType = mediaType;
    }

    public ApplicationClient<T> setUrl(String url) {
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

    public ApplicationClient<T> gotoRoot() {
        url = "/";
        get();
        return this;
    }
    
    public ApplicationClient<T> gotoAppRoot() {
        gotoRoot().followLinkTitle(appName);
        return this;
    }


    public Representation post(Object entity) {
        log.info("{}issuing POST on '{}', providing credentials {}", TESTTAG, url, credentials);
        url = (url.contains("?") ? url + "&" : url + "?") + SkysailServerResource.NO_REDIRECTS ;
        cr = new ClientResource(url);
        cr.setFollowingRedirects(false);
        cr.getCookies().add("Credentials", credentials);
        return cr.post(entity, mediaType);
    }

    public Response getResponse() {
        return cr.getResponse();
    }

    public ApplicationClient<T> loginAs(@NonNull String username, @NonNull String password) {
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

    public ApplicationClient<T> followLinkTitle(String linkTitle) {
        return follow(new LinkTitlePredicate(linkTitle, cr.getResponse().getHeaders()));
    }

    public ApplicationClient<T> followLinkTitleAndRefId(String linkTitle, String refId) {
        Link example = new Link.Builder("").title(linkTitle).refId(refId).build();
        return follow(new LinkByExamplePredicate(example, cr.getResponse().getHeaders()));
    }

    public ApplicationClient<T> followLinkRelation(LinkRelation linkRelation) {
        return follow(new LinkRelationPredicate(linkRelation, cr.getResponse().getHeaders()));
    }

    public ApplicationClient<T> followLink(Method method) {
        return followLink(method, null);
    }

    public ApplicationClient<T> followLink(Method method, T entity) {
        return follow(new LinkMethodPredicate(method, cr.getResponse().getHeaders()), method, entity);
    }

    private ApplicationClient<T> follow(LinkPredicate predicate, Method method, T entity) {
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
        cr = new ClientResource(url);
        cr.getCookies().add("Credentials", credentials);

        if (method != null) {
            if (!(theLink.getVerbs().contains(method))) {
                throw new IllegalStateException("method " + method + " not eligible for link " + theLink);
            }
            if (Method.DELETE.equals(method)) {
                log.info("{}issuing DELETE on '{}', providing credentials {}", TESTTAG, url, credentials);
                currentRepresentation = cr.delete(mediaType);
            } else if (Method.POST.equals(method)) {
                log.info("{}issuing POST on '{}' with entity '{}', providing credentials {}", TESTTAG, url, entity, credentials);
                currentRepresentation = cr.post(entity, mediaType);
            } else if (Method.PUT.equals(method)) {
                log.info("{}issuing PUT on '{}' with entity '{}', providing credentials {}", TESTTAG, url, entity, credentials);
                currentRepresentation = cr.put(entity, mediaType);    
            } else {
                throw new UnsupportedOperationException();
            }
        } else {
            currentRepresentation = cr.get(mediaType);
        }
        return this;
    }

    private ApplicationClient<T> follow(LinkPredicate predicate) {
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
