package io.skysail.server.app.bookmarks.it;

import io.skysail.api.links.LinkRelation;
import io.skysail.client.testsupport.Client;
import io.skysail.server.app.bookmarks.Bookmark;
import io.skysail.server.app.bookmarks.app.BookmarksApplication;
import lombok.extern.slf4j.Slf4j;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.representation.Representation;

@Slf4j
public class Browser {

    private MediaType mediaType;
    private Client client;

    public Browser(String url) {
        this(url, MediaType.TEXT_HTML);
    }

    public Browser(String url, MediaType textHtml) {
        log.info("{}creating new browser client with url '{}' and mediaType '{}'", Client.TESTTAG, url,
                MediaType.TEXT_HTML);
        this.mediaType = mediaType;
        client = new Client(url, mediaType);
    }

    public Browser asUser(String username) {
        log.info("{}logging in as user '{}'", Client.TESTTAG, username);
        client.loginAs(username, "skysail");
        return this;
    }

    public Reference createBookmark(Bookmark bookmark) {
        navigateToPostBookmarkListAs(client);
        client.post(createForm(bookmark));
        return client.getLocation();

    }

    public Representation getTodoLists() {
        log.info("{}retrieving TodoLists", Client.TESTTAG);
        // getTodoLists(client);
        return client.getCurrentRepresentation();
    }

    private void navigateToPostBookmarkListAs(Client client) {
        client.gotoRoot()
            .followLinkTitle(BookmarksApplication.APP_NAME)
            .followLinkRelation(LinkRelation.CREATE_FORM);
    }
    
    private Form createForm(Bookmark bookmark) {
        Form form = new Form();
        form.add("url", bookmark.getUrl().toExternalForm());
        form.add("name", bookmark.getName());
        return form;
    }

}
