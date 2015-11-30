package io.skysail.server.codegen.model;

import io.skysail.server.domain.core.ApplicationModel;
import lombok.Getter;

public class JavaApplication extends ApplicationModel implements JavaModel {

    @Getter
    private String packageName;

    public JavaApplication(String id) {
        super(id.substring(id.lastIndexOf(".")+1));
        this.packageName = "pkgName";//getPackageFromName(id);
    }

}
