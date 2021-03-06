package io.skysail.server.app.bookmarks.it;

import org.restlet.data.*;
import org.restlet.representation.Representation;

import io.skysail.api.links.LinkRelation;
import io.skysail.client.testsupport.*;
import io.skysail.server.app.bookmarks.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BookmarksBrowser extends ApplicationBrowser<BookmarksBrowser, Bookmark> {

    public BookmarksBrowser(MediaType mediaType, int port) {
        super(BookmarksApplication.APP_NAME, mediaType, port);
    }

    @Override
    protected Form createForm(Bookmark bookmark) {
        Form form = new Form();
        form.add("url", bookmark.getUrl().toExternalForm());
        form.add("name", bookmark.getName());
        return form;
    }

    public Reference createBookmark(Bookmark bookmark) {
        navigateToPostBookmarkList(client);
        client.post(createForm(bookmark));
        return client.getLocation();
    }

    public Representation getBookmarks() {
        log.info("{}retrieving TodoLists", ApplicationClient.TESTTAG);
        getBookmarks(client);
        return client.getCurrentRepresentation();
    }

    public void deleteBookmark(String id) {
        log.info("{}deleting Bookmark #{}", ApplicationClient.TESTTAG, id);
        findAndDelete(id);
    }

    private void getBookmarks(ApplicationClient<?> client) {
        client.gotoRoot();
        client.followLinkTitle(BookmarksApplication.APP_NAME);
    }

    private void navigateToPostBookmarkList(ApplicationClient<?> client) {
        client.gotoRoot().followLinkTitle(BookmarksApplication.APP_NAME).followLinkRelation(LinkRelation.CREATE_FORM);
    }

}
