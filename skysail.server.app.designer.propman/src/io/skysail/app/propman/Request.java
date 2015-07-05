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
	ApplicationModel(applicationName=PropMan, entityModels=[EntityModel(entityName=Campaign), EntityModel(entityName=Request)], packageName=io.skysail.app.propman, path=../, projectName=skysail.server.app.designer.propman)

	Entity:
	EntityModel(entityName=Request)
	entity.fields:
	FieldModel(name=requestname)
	*/
}