package io.skysail.server.app.plugins.obr;

import org.apache.felix.bundlerepository.Repository;

public class ObrRepository {

    private String name;
    private String uri;

    public ObrRepository() {
        // TODO Auto-generated constructor stub
    }

    public ObrRepository(Repository repository) {
        name = repository.getName();
        uri = repository.getURI().toString();
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }

}
