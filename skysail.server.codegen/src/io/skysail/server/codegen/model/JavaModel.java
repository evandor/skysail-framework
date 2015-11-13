package io.skysail.server.codegen.model;


public interface JavaModel {

    String getPackageName();

    /*default String getPackageFromName(String name) {
        int indexOfLastDot = name.lastIndexOf(".");
        if (indexOfLastDot < 0) {
            return "";
        }
        return name.substring(0,indexOfLastDot);
    }

    default String toSimpleName(String fullQualifiedClassName) {
        int indexOfLastDot = fullQualifiedClassName.lastIndexOf(".");
        if (indexOfLastDot < 0) {
            return fullQualifiedClassName;
        }
        return fullQualifiedClassName.substring(indexOfLastDot+1);
    }*/

}
