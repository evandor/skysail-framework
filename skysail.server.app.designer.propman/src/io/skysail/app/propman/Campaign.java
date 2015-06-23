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
public class Campaign implements Serializable, Identifiable {

	@Id
    private String id;

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {
    }

    @Field
    private String name;
    public void setName(String value) {
        this.name = value;
    }

    public String getName() {
        return name;
    }


    @Reference(cls = Request.class)
    @PostView(visibility=Visibility.HIDE)
    @PutView(visibility=Visibility.HIDE)
    private List<Request> requests = new ArrayList<>();

    public void addRequest(Request entity) {
        requests.add(entity);
    }



	/*
	Application:
	ApplicationModel(applicationName=PropMan, entities=[EntityModel(entityName=Campaign, fields=[FieldModel(name=name)], references=[ReferenceModel(name=Request)], className=null), EntityModel(entityName=Request, fields=[FieldModel(name=requestname)], references=[], className=null)], packageName=io.skysail.app.propman, path=../, projectName=skysail.server.app.designer.propman)

	Entity:
	EntityModel(entityName=Campaign, fields=[FieldModel(name=name)], references=[ReferenceModel(name=Request)], className=null)
	entity.fields:
	FieldModel(name=name)
	*/
}