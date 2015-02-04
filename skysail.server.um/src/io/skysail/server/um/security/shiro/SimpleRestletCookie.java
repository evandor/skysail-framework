package io.skysail.server.um.security.shiro;

import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;

public class SimpleRestletCookie extends SimpleCookie {

    public SimpleRestletCookie(Cookie template) {
        super(template);
    }

}
