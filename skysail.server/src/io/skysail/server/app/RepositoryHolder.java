package io.skysail.server.app;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.repos.Repository;
import lombok.ToString;

@ToString
public class RepositoryHolder implements Repository {

    private String id;
    private Class<? extends Identifiable> identifiableClass;

    public RepositoryHolder(Class<? extends Identifiable> identifiableClass) {
        this.id = identifiableClass.getSimpleName() + "Repository";
        this.identifiableClass = identifiableClass;
    }

    @Override
    public Class<? extends Identifiable> getRootEntity() {
        return identifiableClass;
    }

    @Override
    public Identifiable findOne(String id) {
        return null;
    }

    @Override
    public Object save(Identifiable identifiable) {
        return null;
    }

    @Override
    public Object update(String id, Identifiable entity, String... edges) {
        return null;
    }

}
