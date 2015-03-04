package io.skysail.server.app.crm.domain;

import io.skysail.server.app.crm.domain.companies.Company;
import lombok.Getter;

public class CompanyWithId extends Company {

    @Getter
    private String id;

    public CompanyWithId(String creator, String id) {
        super(creator);
        this.id = id;
    }

}
