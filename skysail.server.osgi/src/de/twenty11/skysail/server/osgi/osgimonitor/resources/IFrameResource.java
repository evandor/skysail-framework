//package de.twenty11.skysail.server.ext.osgimonitor.resources;
//
//import org.restlet.resource.Get;
//
//import de.twenty11.skysail.common.Presentation;
//import de.twenty11.skysail.common.PresentationStyle;
//import de.twenty11.skysail.common.responses.SkysailResponse;
//import de.twenty11.skysail.common.responses.SuccessResponse;
//import de.twenty11.skysail.server.core.restlet.SkysailServerResource2;
//
///**
// * 
// */
//@Presentation(preferred = PresentationStyle.IFRAME)
//public class IFrameResource extends SkysailServerResource2 {
//
//    public IFrameResource() {
//        setName("iframe resource");
//    }
//
//    @Get("html")
//    public SkysailResponse<String> redirectToIFrame() {
//        return new SkysailResponse<String>("asGraph/d3Simple");
//    }
//
// }
