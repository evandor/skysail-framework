package io.skysail.server.codegen.model.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Instances of this class connect two {@link Entity}s with a link "from -> to".
 * 
 * The semantic is that the from entity "has a member which is a collection of 'to'-Entities". 
 *
 */
@EqualsAndHashCode(exclude = { "from", "to" })
@Getter
public class Reference {

	private Entity from;
	private String fromName;
	private Entity to;
	private String toName;

	public Reference(Entity from, Entity to) {
		this.from = from;
		this.fromName = from.toString();
		this.to = to;
		this.toName = to.toString();
	}

	@Override
	public String toString() {
		return fromName + " > " + toName;
	}
}
