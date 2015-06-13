package io.skysail.server.http;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.*;

public class JettyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.getWriter().println("Hello from jetty!");
    };
}