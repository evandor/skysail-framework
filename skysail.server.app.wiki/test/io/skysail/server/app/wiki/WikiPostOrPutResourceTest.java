package io.skysail.server.app.wiki;

import io.skysail.server.app.wiki.repository.WikiRepository;
import io.skysail.server.testsupport.PostResourceTest;

import java.util.HashMap;

import org.apache.shiro.subject.SimplePrincipalMap;
import org.mockito.Mockito;

public abstract class WikiPostOrPutResourceTest extends PostResourceTest {

    protected WikiRepository repo;

    protected void initRepository() {
        repo = new WikiRepository();
        repo.setDbService(testDb);
        repo.activate();
        ((WikiApplication)application).setWikiRepository(repo);
        Mockito.when(((WikiApplication)application).getRepository()).thenReturn(repo);
    }

    protected void initUser(String username) {
        Mockito.when(subjectUnderTest.getPrincipal()).thenReturn(username);
        Mockito.when(subjectUnderTest.getPrincipals()).thenReturn(new SimplePrincipalMap(new HashMap<>()));
        setSubject(subjectUnderTest);
    }
}
