package io.skysail.server.app.oEService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Id;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.HtmlPolicy;
import io.skysail.domain.html.InputType;
import io.skysail.domain.html.Relation;
import io.skysail.server.forms.ListView;

@SuppressWarnings("serial")
public class OE implements Identifiable, Serializable {

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
    @ListView(link = OEsResource.class)
    private String name;

    public void setName(String value) {
        this.name = value;
    }

    public String getName() {
        return this.name;
    }

    @Field(inputType = InputType.DATE, htmlPolicy = HtmlPolicy.NO_HTML)
    private Date expires;

    public void setExpires(Date value) {
        this.expires = value;
    }

    public Date getExpires() {
        return this.expires;
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