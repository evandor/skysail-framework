package io.skysail.app.propman;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.Field;
import io.skysail.api.forms.Reference;
import io.skysail.server.forms.PostView;
import io.skysail.server.forms.PutView;
import io.skysail.server.forms.Visibility;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties("handler")
public class Request implements Serializable, Identifiable {

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

    @Field
    private String requestname;
    public void setRequestname(String value) {
        this.requestname = value;
    }

    public String getRequestname() {
        return requestname;
    }



	/*
	Application:
	ApplicationModel(applicationName=PropMan, entities=[EntityModel(entityName=Campaign, fields=[FieldModel(name=name)], references=[ReferenceModel(name=Request)], className=io.skysail.app.propman.Campaign, rootEntity=true), EntityModel(entityName=Request, fields=[FieldModel(name=requestname)], references=[], className=null, rootEntity=false)], packageName=io.skysail.app.propman, path=../, projectName=skysail.server.app.designer.propman)

	Entity:
	EntityModel(entityName=Request, fields=[FieldModel(name=requestname)], references=[], className=null, rootEntity=false)
	entity.fields:
	FieldModel(name=requestname)
	*/
}