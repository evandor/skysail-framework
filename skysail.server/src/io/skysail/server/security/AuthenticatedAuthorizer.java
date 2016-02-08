package io.skysail.server.security;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.security.Authorizer;

public class AuthenticatedAuthorizer extends Authorizer {

	@Override
	protected boolean authorize(Request request, Response response) {
		Subject subject = SecurityUtils.getSubject();
		return subject.isAuthenticated();
	}

}
