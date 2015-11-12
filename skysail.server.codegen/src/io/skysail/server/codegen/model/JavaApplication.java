package io.skysail.server.codegen.model;

import io.skysail.server.domain.core.Application;
import lombok.Getter;

public class JavaApplication extends Application implements JavaModel {

    @Getter
    private String packageName;

    public JavaApplication(String id) {
        super(id.substring(id.lastIndexOf(".")+1));
        this.packageName = getPackageFromName(id);
    }

    @Override
    public String getTypeName() {
        return "typeName";
    }

}
