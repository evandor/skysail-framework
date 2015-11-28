package io.skysail.server.um.security.shiro.util;

import org.restlet.*;

/**
 * A {@code RestletRequestPairSource} is a provider of a {@link Request Request}
 * and {@link Response Response} pair associated with a currently executing
 * request.
 *
 * @since 1.0
 */
public interface RestletRequestPairSource {

    Request getRestletRequest();

    Response getRestletResponse();

}
