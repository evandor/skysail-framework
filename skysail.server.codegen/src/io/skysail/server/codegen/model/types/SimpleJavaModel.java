package io.skysail.server.codegen.model.types;

public class SimpleJavaModel extends JavaModel {

	public SimpleJavaModel(Class<?> cls) {
		this.packageName = cls.getPackage().getName();
		this.typeName = cls.getSimpleName();
	}

	public SimpleJavaModel(Class<?> cls, String genericParameter) {
		this(cls);
		this.genericParameter = genericParameter;
	}

}
