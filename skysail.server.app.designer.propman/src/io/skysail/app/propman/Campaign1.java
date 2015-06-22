package io.skysail.app.propman;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.*;
import io.skysail.server.forms.*;

import java.util.Set;

import java.io.Serializable;
import java.util.*;

import javax.persistence.Id;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties("handler")
public class Campaign1  implements Serializable, Identifiable {

    @Id
    private String id;
    
    public void setId(String id) {
        this.id = id;
    }

	public String getId() {
		return id;
	}
	    
    
    @Field
    private String name;
    
    
    @Reference(cls = Request.class)
    @PostView(visibility=Visibility.HIDE)
    @PutView(visibility=Visibility.HIDE)
    private List<Request> requests = new ArrayList<>();

    public void addRequest(Request entity) {
        requests.add(entity);
    }

    
        public void setName(String value) {
        this.name = value;
    }

    public String getName() {
        return name;
    }


    
}
