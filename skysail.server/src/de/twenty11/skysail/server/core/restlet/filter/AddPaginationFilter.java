//package de.twenty11.skysail.server.core.restlet.filter;
//
//import org.restlet.Response;
//import org.restlet.engine.header.Header;
//import org.restlet.util.Series;
//
//import de.twenty11.skysail.server.core.restlet.ResponseWrapper;
//import de.twenty11.skysail.server.core.restlet.SkysailServerResource;
//
//public class AddPaginationFilter<R extends SkysailServerResource<T>, T> extends ResponseHeadersManipulatingFilter<R, T> {
//
//	@Override
//	protected void afterHandle(R resource, Response response, ResponseWrapper<T> responseWrapper) {
//		if (resource instanceof SkysailServerResource) {
//			Series<Header> responseHeaders = createHttpHeadersIfNotExistent(resource.getResponse());
//			responseHeaders.add(new Header("X-Pagination-Pages", "4"));
//            responseHeaders.add(new Header("X-Pagination-Page", "1"));
//		}
//	}
//}
