package io.skysail.server.http;

import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Server Config", pid="server")
public @interface ServerConfig {

    int port() default 2015;
    
}
