package io.skysail.server.app.bookmarks.it;

import io.skysail.api.links.LinkRelation;
import io.skysail.client.testsupport.Browser;
import io.skysail.client.testsupport.Client;
import io.skysail.server.app.bookmarks.Bookmark;
import io.skysail.server.app.bookmarks.BookmarksApplication;
import lombok.extern.slf4j.Slf4j;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.representation.Representation;

@Slf4j
public class BookmarksBrowser extends Browser<BookmarksBrowser, Bookmark> {

    public BookmarksBrowser(MediaType mediaType) {
        super(BookmarksApplication.APP_NAME, mediaType);
    }

    public Reference createBookmark(Bookmark bookmark) {
        navigateToPostBookmarkList(client);
        client.post(createForm(bookmark));
        return client.getLocation();
    }

    public Representation getBookmarks() {
        log.info("{}retrieving TodoLists", Client.TESTTAG);
        getBookmarks(client);
        return client.getCurrentRepresentation();
    }

    public void deleteBookmark(String id) {
        log.info("{}deleting Bookmark #{}", Client.TESTTAG, id);
        findAndDelete(id);
    }

   

    private void getBookmarks(Client<?> client) {
        client.gotoRoot();
        client.followLinkTitle(BookmarksApplication.APP_NAME);
    }

    private void navigateToPostBookmarkList(Client<?> client) {
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