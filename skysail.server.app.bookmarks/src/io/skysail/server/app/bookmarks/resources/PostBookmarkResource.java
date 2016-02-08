package io.skysail.server.app.bookmarks.resources;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.skysail.server.app.bookmarks.Bookmark;
import io.skysail.server.app.bookmarks.BookmarksApplication;
import io.skysail.server.app.bookmarks.repo.BookmarksRepository;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PostBookmarkResource extends PostEntityServerResource<Bookmark> {

    private BookmarksRepository repository;
    private BookmarksApplication app;

    @Override
    public void doInit() {
        app = (BookmarksApplication) getApplication();
        repository = (BookmarksRepository) app.getRepository(Bookmark.class);
    }

    @Override
    public Bookmark createEntityTemplate() {
        return new Bookmark();
    }

    @Override
    public void addEntity(Bookmark entity) {

        analyzeBookmarkUrl(entity);

        entity.setCreated(new Date());
        Subject subject = SecurityUtils.getSubject();
        entity.setOwner(subject.getPrincipal().toString());
        String id = repository.save(entity, app.getApplicationModel()).toString();
        entity.setId(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(BookmarksResource.class);
    }

    private void analyzeBookmarkUrl(Bookmark entity) {
        Document doc;
        try {
            doc = Jsoup.connect(entity.getUrl().toExternalForm()).get();
            if (StringUtils.isEmpty(entity.getName())) {
                entity.setName(doc.title());
            }
            Element e=doc.head().select("link[href~=.*\\.ico]").first();
            if (e != null) {
                entity.setFavicon(e.attr("href"));
            }
            entity.setMetaDescription(getMetaTag(doc, "description"));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    String getMetaTag(Document document, String attr) {
        Elements elements = document.select("meta[name=" + attr + "]");
        for (Element element : elements) {
            final String s = element.attr("content");
            if (s != null)
                return s;
        }
        elements = document.select("meta[property=" + attr + "]");
        for (Element element : elements) {
            final String s = element.attr("content");
            if (s != null)
                return s;
        }
        return null;
    }

}
