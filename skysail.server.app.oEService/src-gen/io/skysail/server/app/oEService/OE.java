package io.skysail.server.app.oEService;

import java.io.Serializable;
import javax.persistence.Id;

import java.util.*;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.*;
import io.skysail.server.forms.*;

import org.apache.commons.lang3.StringUtils;

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
    @ListView(link = OEsResource.class)
    private Date expires;

    public void setExpires(Date value) {
        this.expires = value;
    }

    public Date getExpires() {
        return this.expires;
    }


    // --- relations ---

    @Relation
    private List<io.skysail.server.app.oEService.OE> oes = new ArrayList<>();

    public void setOes(List<io.skysail.server.app.oEService.OE> value) {
        this.oes = value;
    }

    public List<io.skysail.server.app.oEService.OE> getOes() {
        return oes;
    }




}