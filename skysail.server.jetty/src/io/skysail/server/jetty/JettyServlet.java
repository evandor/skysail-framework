package io.skysail.server.jetty;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.*;

public class JettyServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 9113393446781098730L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.getWriter().println("Hello from jetty!");
    };
}