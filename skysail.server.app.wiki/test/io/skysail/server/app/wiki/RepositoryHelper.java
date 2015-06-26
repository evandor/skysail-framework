package io.skysail.server.app.wiki;

import io.skysail.server.app.wiki.pages.Page;
import io.skysail.server.app.wiki.repository.WikiRepository;
import io.skysail.server.app.wiki.spaces.Space;

import java.math.BigInteger;
import java.security.SecureRandom;

import lombok.SneakyThrows;

public class RepositoryHelper {
    
    @SneakyThrows
    public static Space createTestSpace(String owner) {
        String spacename = "space_" + randomString();
        Space space = new Space(spacename);
        space.setOwner(owner);
        String id = WikiRepository.add(space).toString();
        space.setId(id);
        return space;
    }

    public static Page createTestPageIn(WikiRepository repo, Space space) {
        String pagename = "page_" + randomString();
        Page page = new Page(pagename);
        space.addPage(page);
        repo.update(space.getId(), space);
        return page;
    
    }

    protected static String randomString() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }
    
}
