package io.skysail.server.app.designer.codegen;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.Field;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.repo.DesignerRepository;

import java.util.Set;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties("handler")
public class AppDesignerMoneyBank  implements Serializable, Identifiable {

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
    
        public void setName(String value) {
        this.name = value;
    }

    public String getName() {
        return name;
    }

    
}
