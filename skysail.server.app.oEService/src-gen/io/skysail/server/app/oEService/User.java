package io.skysail.server.app.oEService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.HtmlPolicy;
import io.skysail.domain.html.InputType;
import io.skysail.domain.html.Relation;
import io.skysail.server.forms.ListView;

@SuppressWarnings("serial")
public class User implements Identifiable, Serializable {

    @Id
    private String id;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    // --- fields ---

    @Field(inputType = InputType.TEXT, htmlPolicy = HtmlPolicy.NO_HTML)
    @ListView(link = UsersOEsResource.class)
    private String name;

    public void setName(String value) {
        this.name = value;
    }

    public String getName() {
        return this.name;
    }


    // --- relations ---

    @Relation
    private List<io.skysail.server.app.oEService.OE> oEs = new ArrayList<>();

    public void setOEs(List<io.skysail.server.app.oEService.OE> value) {
        this.oEs = value;
    }

    public List<io.skysail.server.app.oEService.OE> getOEs() {
        return oEs;
    }




}