package io.skysail.app.propman;

import java.util.Date;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.*;
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
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Field
    private String name;
    public void setName(String value) {
        this.name = value;
    }

    public String getName() {
        return name;
    }


    @Field(type=InputType.READONLY)
    private Date created;
    public void setCreated(Date value) {
        this.created = value;
    }

    public Date getCreated() {
        return created;
    }


    @Reference(cls = Request.class)
    @PostView(visibility=Visibility.HIDE)
    @PutView(visibility=Visibility.HIDE)
    private List<Request> requests = new ArrayList<>();

    public void addRequest(Request entity) {
        requests.add(entity);
    }

    public List<Request> getRequests() {
        return requests;
    }



	/*
	Application:
	ApplicationModel(applicationName=PropMan, entityModels=[EntityModel(entityName=Campaign), EntityModel(entityName=Request)], packageName=io.skysail.app.propman, path=../, projectName=skysail.server.app.designer.propman)

	Entity:
	EntityModel(entityName=Campaign)
	entity.fields:
	FieldModel(name=name)
	*/
}