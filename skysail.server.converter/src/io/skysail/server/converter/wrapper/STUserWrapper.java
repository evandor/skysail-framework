package io.skysail.server.converter.wrapper;

import org.apache.shiro.subject.Subject;

public class STUserWrapper {

    private Subject subject;

    public STUserWrapper(Subject subject) {
        this.subject = subject;
    }

    public Object getPrincipal() {
        return subject.getPrincipal();
    }

    public Object getUsername() {
        return subject.getPrincipals().asList().get(1);
    }

}
