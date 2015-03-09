package io.skysail.server.app.crm.emails;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailContact {

    private String email;

    private EmailRelation relation;
}
