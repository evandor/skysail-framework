package io.skysail.server.app;

import io.skysail.domain.Identifiable;
import io.skysail.domain.core.ApplicationModel;
import io.skysail.domain.core.repos.DbRepository;
import io.skysail.domain.core.repos.Repository;
import lombok.ToString;

@ToString
// FIXME needed?
public class RepositoryHolder implements Repository {

    private String id;
    private Class<? extends Identifiable> identifiableClass;
    private DbRepository repository;

    @Deprecated
    public RepositoryHolder(Class<? extends Identifiable> identifiableClass) {
        this.id = identifiableClass.getSimpleName() + "Repository";
        this.identifiableClass = identifiableClass;
    }

    public RepositoryHolder(String key, DbRepository value) {
        this.id = key;
        this.repository = value;
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
    public Object save(Identifiable identifiable, ApplicationModel appModel) {
        return null;
    }

    public Object update(Identifiable entity, ApplicationModel model) {
        return null;
    }

    @Override
    public void delete(Identifiable identifiable) {
    }

}
