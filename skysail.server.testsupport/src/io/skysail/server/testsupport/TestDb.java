package io.skysail.server.testsupport;

import io.skysail.server.db.OrientGraphDbService;



public class TestDb extends OrientGraphDbService {

    @Override
    protected String getDbUrl() {
        return "memory:test";
    }

    @Override
    protected String getDbUsername() {
        return "admin";
    }

    @Override
    protected String getDbPassword() {
        return "admin";
    }
}
