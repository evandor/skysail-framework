package io.skysail.server.db.it;

import io.skysail.api.repos.DbRepository;
import io.skysail.server.app.SkysailApplication;

import org.osgi.service.component.annotations.Component;

import de.twenty11.skysail.server.app.ApplicationProvider;

@Component(immediate = true)
public class SkysailItApplication extends SkysailApplication implements ApplicationProvider {

    private static final String APP_NAME = "TestApp";

    private DbRepository myRepository;

    public SkysailItApplication() {
        super(APP_NAME);
   }

   @Override
   protected void attach() {
      super.attach();
//      router.attach(new RouteBuilder("/clips", ClipsResource.class));
//      router.attach(new RouteBuilder("/clips/", PostClipResource.class));
//      router.attach(new RouteBuilder("/clips/{id}", ClipResource.class));
//      router.attach(new RouteBuilder("/clips/{id}/", PutClipResource.class));
   }



   public TestRepository getRepository() {
        return (TestRepository) myRepository;
   }
}

