package io.skysail.server.db;



/**
 * the orientdb class name used is derived from the java class name
 * by substituting dots with underscores.
 *
 */
public class DbClassName {

    public static String of(Class<?> cls) {
        return cls.getName().replace(".", "_");
    }

}
