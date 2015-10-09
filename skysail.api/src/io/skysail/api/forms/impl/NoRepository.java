package io.skysail.api.forms.impl;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.repos.DbRepository;

public class NoRepository implements DbRepository {

    @Override
    public Object save(Identifiable identifiable) {
        return null;
    }

    @Override
    public Object update(String id, Object entity, String... edges) {
        return null;
    }

    @Override
    public Identifiable findOne(String id) {
        return null;
    }

}
