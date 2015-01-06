package de.twenty11.skysail.server.core.restlet;

import org.restlet.data.Method;
import org.restlet.resource.Get;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.api.responses.LinkHeaderRelation;
import de.twenty11.skysail.api.structures.graph.Graph;
import de.twenty11.skysail.server.app.SkysailApplication;
import de.twenty11.skysail.server.core.restlet.filter.AbstractResourceFilter;

public abstract class GraphResource extends SkysailServerResource<Graph> {

    private static final Logger logger = LoggerFactory.getLogger(GraphResource.class);
    
    public abstract Graph getData();
    
    @Override
    public LinkHeaderRelation getLinkRelation() {
        return LinkHeaderRelation.ALTERNATE;
    }
    
    @Get("html|json|graph")
    public Graph getEntity() {
        logger.info("Request entry point: {} @Get('html|json|graph')", this.getClass().getSimpleName());
        RequestHandler<Graph> requestHandler = new RequestHandler<Graph>((SkysailApplication) getApplication());
        AbstractResourceFilter<GraphResource, Graph> chain = requestHandler.createForGraph(Method.GET);
        ResponseWrapper<Graph> wrapper = chain.handle(this, getResponse());
        return wrapper.getEntity();
    }

}
