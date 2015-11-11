package io.skysail.server.codegen.model.entities;

import javax.lang.model.element.Element;

import lombok.*;

/**
 * Instances of this class identify a business entity.
 *
 * Typically, an annotation processor scans the classes in the given project for
 * specific annotations and creates new Entity instances by passing a
 * {@link Element} object to the constructor.
 *
 * The entities are related to other entities, which is modeled in the
 * {@link EntityGraph} class.
 */
@Getter
@EqualsAndHashCode(exclude = { "elementName" })
public class Entity {

	private @NonNull String elementName;
	private @NonNull String simpleName;
    private @NonNull String packageName;
   // private @NonNull Class<? extends SkysailApplication> application;

	public Entity(Element element) {
	 	this.elementName = element.toString();
		this.simpleName = element.getSimpleName().toString();
		packageName = element.getEnclosingElement().toString();
	}

	public Entity(@NonNull String packageName, @NonNull String simpleName) {
		this.simpleName = simpleName;
		this.packageName = packageName;
	}

	@Override
	public String toString() {
	    return new StringBuilder("Entity(").append(packageName).append(", ").append(simpleName).append(")").toString();
	}
}
