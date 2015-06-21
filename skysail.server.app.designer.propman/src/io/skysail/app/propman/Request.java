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
public class Request  implements Serializable, Identifiable {

    @Id
    private String id;
    
    public void setId(String id) {
        this.id = id;
    }

	public String getId() {
		return id;
	}
	    
    
    @Field
    private String requestname;
    
    
    
        public void setRequestname(String value) {
        this.requestname = value;
    }

    public String getRequestname() {
        return requestname;
    }


    
}
