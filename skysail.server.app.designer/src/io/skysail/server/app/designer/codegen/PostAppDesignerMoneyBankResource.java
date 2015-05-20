package io.skysail.server.app.designer.codegen;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.repo.DesignerRepository;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;

public class PostAppDesignerMoneyBankResource extends PostEntityServerResource<AppDesignerMoneyBank> {

	private DesignerApplication app;

    public PostAppDesignerMoneyBankResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new AppDesignerMoneyBank");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (DesignerApplication) getApplication();
    }

	@Override
    public AppDesignerMoneyBank createEntityTemplate() {
        return new AppDesignerMoneyBank();
    }

    @Override
    public SkysailResponse<?> addEntity(AppDesignerMoneyBank entity) {
        Subject subject = SecurityUtils.getSubject();
        //entity.setOwner(subject.getPrincipal().toString());
        String id = DesignerRepository.add(entity).toString();
        entity.setId(id);
        return new SkysailResponse<String>();
    }
    
    //@Override
    //public String redirectTo() {
    //    return super.redirectTo(ApplicationsResource.class);
    //}
   
}
