package io.skysail.server.app.bb.goals;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.bb.BBApplication;
import io.skysail.server.app.bb.DummyGoal;
import io.skysail.server.app.bb.goals.wc.WorkAndCareerGoalsResource;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public abstract class PostAreaGoalsResource extends PostEntityServerResource<DummyGoal> {

    @Override
    public SkysailResponse<DummyGoal> addEntity(DummyGoal entity) {
        String id = ((BBApplication)getApplication()).getRepository().save(entity).toString();
        Subject subject = SecurityUtils.getSubject();
        entity.setOwner(subject.getPrincipal().toString());
        entity.setId(id);
        return new SkysailResponse<>(entity);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(WorkAndCareerGoalsResource.class);
    }
}
