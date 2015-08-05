package de.twenty11.skysail.server.osgi.osgimonitor.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.util.Arrays;

import org.osgi.framework.*;
import org.restlet.data.*;
import org.restlet.representation.*;
import org.restlet.resource.*;

import de.twenty11.skysail.server.osgi.OsgiMonitorViewerApplication;
import de.twenty11.skysail.server.osgi.osgimonitor.domain.BundleDetails;

/**
 * Restlet Resource class for handling a Bundle.
 *
 */
public class BundleResource extends EntityServerResource<BundleDetails> {

    private String bundleId;
    private String action;

//    public BundleResource() {
//    	super("osgimonitor bundle resource");
//        setName("osgimonitor bundle resource");
//        setDescription("The resource containing bundle detail information");
//    }

    @Override
    protected void doInit() throws ResourceException {
        bundleId = (String) getRequest().getAttributes().get("bundleId");
        Form form = new Form(getRequest().getEntity());
        action = form.getFirstValue("action");
    }

    @Get("html|json")
    public SkysailResponse<BundleDetails> getBundle() {
        OsgiMonitorViewerApplication app = (OsgiMonitorViewerApplication) getApplication();
        BundleContext bundleContext = app.getBundleContext();
        Bundle bundle = bundleContext.getBundle(Long.parseLong(bundleId));
        BundleDetails details = new BundleDetails(bundle);
        // registerCommand("start", new StartCommand(bundle));
        // registerCommand("stop", new StopCommand(bundle));
        // registerCommand("update", new UpdateCommand(bundle));
        // registerLinkedPage(new LinkedPage() {
        // @Override
        // public boolean applicable() {
        // return true;
        // }
        //
        // @Override
        // public String getHref() {
        // return bundleId + "/headers";
        // }
        //
        // @Override
        // public String getLinkText() {
        // return "show Headers";
        // }
        // });
        return null;//getEntity(details);
    }

    @Get("putform")
    public Representation formWithPut() {
        if (Arrays.asList("start", "stop", "update").contains(action)) {
            String start = "<html><head><title>form to issue PUT request</title></head>\n<body>\n";
            String stop = "</body></html>";
            StringRepresentation stringRepresentation = new StringRepresentation(
                    start
                            + "<form action='?method=PUT' method='POST'><input type='text' name='media' value='json'><input type='text' name='testname' value='testvalue'><input type='submit'></form>"
                            + stop);
            stringRepresentation.setMediaType(MediaType.TEXT_HTML);
            return stringRepresentation;
        } else {
            return new StringRepresentation("only 'start','stop' and 'update' are allowed as action");
        }
    }

    /**
     * parameter needed for restlet!
     *
     * @param entity
     * @return
     */
    @Put("html|json")
    public SkysailResponse<BundleDetails> startOrStopBundle(Representation entity) {
        // Command command = getCommand(action);
        // if (command != null) {
        // try {
        // command.execute();
        // } catch (Exception e) {
        // return new FailureResponse<BundleDetails>(e);
        // }
        // }
        // setMessage("Success");
        // return getBundle();
        return null;
    }

	@Override
	public BundleDetails getEntity() {
		// TODO Auto-generated method stub
		return null;
	}

    @Override
    public SkysailResponse<?> eraseEntity() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

}
