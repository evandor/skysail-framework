package io.skysail.server.ext.apollo;

import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Apollo MQ Config", pid="apolloMQ")
public @interface ApolloConfig {

    String getUser() default "admin";
    String getPassword() default "password";
    String getHost() default "localhost";
    int getPort() default 61613;    
    
}
