package io.skysail.app.propman;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.*;

import java.util.Set;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties("handler")
public class Campaign  implements Serializable, Identifiable {

    @Id
    private String id;
    
    public void setId(String id) {
        this.id = id;
    }

	public String getId() {
		return id;
	}
	    
    
    @Field
    private String name;;

    @Field
    private String name;
    
    
    @Reference(cls = Request.class)
    private String Request;
    
        public void setName(String value) {
        this.name = value;
    }

    public String getName() {
        return name;
    }
;
    public void setName(String value) {
        this.name = value;
    }

    public String getName() {
        return name;
    }

    public void setRequest(String value) {
        this.Request = value;
    }

    public String getRequest() {
        return Request;
    }

    
}
