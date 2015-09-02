package io.skysail.server.app.quartz.groups;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.Field;

import java.io.Serializable;

import javax.persistence.Id;
import javax.validation.constraints.Size;

import lombok.*;

@Getter
@Setter
public class Group implements Serializable, Identifiable {

    private static final long serialVersionUID = 5444440718293296363L;

    @Id
	private String id;

	@Field
	@Size(min = 1)
	private String name;


}
