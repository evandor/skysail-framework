package de.twenty11.skysail.server.ext.converter.st;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import org.apache.shiro.SecurityUtils;
import org.osgi.framework.Bundle;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.restlet.data.MediaType;
import org.restlet.engine.converter.ConverterHelper;
import org.restlet.engine.resource.VariantInfo;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.ST;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import de.twenty11.skysail.api.responses.LinkHeaderRelation;
import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.api.responses.LinkheaderRole;
import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.app.SkysailApplication;
import de.twenty11.skysail.server.app.SourceWrapper;
import de.twenty11.skysail.server.core.FormField;
import de.twenty11.skysail.server.core.restlet.EntityServerResource;
import de.twenty11.skysail.server.core.restlet.ListServerResource;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;
import de.twenty11.skysail.server.core.restlet.utils.CookiesUtils;
import de.twenty11.skysail.server.core.restlet.utils.StringParserUtils;
import de.twenty11.skysail.server.ext.converter.st.renderer.FormFieldRenderer;
import de.twenty11.skysail.server.ext.converter.st.wrapper.STFieldsWrapper;
import de.twenty11.skysail.server.ext.converter.st.wrapper.STListSourceWrapper;
import de.twenty11.skysail.server.ext.converter.st.wrapper.STResourceWrapper;
import de.twenty11.skysail.server.ext.converter.st.wrapper.STServicesWrapper;
import de.twenty11.skysail.server.ext.converter.st.wrapper.STSourceWrapper;
import de.twenty11.skysail.server.ext.converter.st.wrapper.STTargetWrapper;
import de.twenty11.skysail.server.services.MenuItemProvider;
import de.twenty11.skysail.server.services.OsgiConverterHelper;
import de.twenty11.skysail.server.services.SearchService;
import de.twenty11.skysail.server.services.UserManager;
import etm.core.configuration.EtmManager;
import etm.core.monitor.EtmMonitor;
import etm.core.monitor.EtmPoint;

/**
 * A component providing converting functionality via the StringTemplate
 * library.
 *
 */
@Component(immediate = true, properties = { "event.topics=GUI/alerts/*" })
public class StringTemplateConverter extends ConverterHelper implements OsgiConverterHelper, EventHandler {

	private static final String PRODUCT_NAME = "productName";

	private static final Logger logger = LoggerFactory.getLogger(StringTemplateConverter.class);

	protected static final EtmMonitor etmMonitor = EtmManager.getEtmMonitor();

	private static final float DEFAULT_MATCH_VALUE = 0.5f;
	private static Map<MediaType, Float> mediaTypesMatch = new HashMap<MediaType, Float>();

	private String templateNameFromCookie;

	private List<Event> events = new CopyOnWriteArrayList<>();

	private Map<String, List<String>> userMsgsList = new ConcurrentHashMap<>();

	private STGroupBundleDir importedGroupBundleDir;

	private STGroupBundleDir stGroup;

	private volatile Set<MenuItemProvider> menuProviders = new HashSet<>();

	private UserManager userManager;

	private SearchService searchService;

	static {
		mediaTypesMatch.put(MediaType.TEXT_HTML, 0.95F);
		mediaTypesMatch.put(SkysailApplication.SKYSAIL_TREE_FORM, 1.0F);
	}

	@Reference(multiple = true, optional = true, dynamic = true)
	public void addMenuProvider(MenuItemProvider provider) {
		if (provider == null) { // || provider.getMenuEntries() == null) {
			return;
		}
		menuProviders.add(provider);
	}

	public void removeMenuProvider(MenuItemProvider provider) {
		menuProviders.remove(provider);
	}

	@Reference(multiple = false, optional = false, dynamic = true)
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void unsetUserManager(@SuppressWarnings("unused") UserManager userManager) {
		this.userManager = null;
	}

	@Reference(multiple = false, optional = true, dynamic = true)
	public void setSearchService(SearchService searchService) {
		this.searchService = searchService;
	}

	public void unsetSearchService(@SuppressWarnings("unused") SearchService SearchService) {
		this.searchService = null;
	}

	@Override
	public List<Class<?>> getObjectClasses(Variant source) {
		throw new RuntimeException("getObjectClasses method is not implemented yet");
	}

	@Override
	public List<VariantInfo> getVariants(Class<?> source) {
		return Arrays.asList(new VariantInfo(SkysailApplication.SKYSAIL_TREE_FORM));
	}

	@Override
	public float score(Object source, Variant target, Resource resource) {
		for (MediaType mediaType : mediaTypesMatch.keySet()) {
			if (target.getMediaType().equals(mediaType)) {
				logger.info("converter '{}' matched '{}' with threshold {}", new Object[] {
				        this.getClass().getSimpleName(), mediaTypesMatch, mediaTypesMatch.get(mediaType) });
				return mediaTypesMatch.get(mediaType);
			}
		}
		return DEFAULT_MATCH_VALUE;
	}

	@Override
	public <T> float score(Representation source, Class<T> target, Resource resource) {
		return -1.0F;
	}

	@Override
	public <T> T toObject(Representation source, Class<T> target, Resource resource) {
		throw new RuntimeException("toObject method is not implemented yet");
	}

	/**
	 * source: List of entities variant: e.g. [text/html] resource: ? extends
	 * SkysailServerResource
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Representation toRepresentation(Object originalSource, Variant target, Resource resource) {

		EtmPoint point = etmMonitor.createPoint(this.getClass().getSimpleName() + ":toRepresentation");

		SkysailServerResource<?> skysailResource = (SkysailServerResource<?>) resource;

		SourceWrapper sourceWrapper = new SourceWrapper(originalSource, target, skysailResource);

		templateNameFromCookie = CookiesUtils.getTemplateFromCookie(resource.getRequest()); // hmm...

		stGroup = createStGroup(resource, target.getMediaType().getName());
		stGroup.registerRenderer(FormField.class, new FormFieldRenderer(resource, sourceWrapper.getConvertedSource()));

		String mainPage = CookiesUtils.getMainPageFromCookie(resource.getRequest());
		ST index;
		if (mainPage != null && mainPage.length() > 0) {
			index = stGroup.getInstanceOf(mainPage);

		} else {
			index = stGroup.getInstanceOf("index");
		}

		if (index == null) {
			throw new IllegalStateException("cannot get instance of stringtemplate 'index'");
		}

		if (skysailResource instanceof ListServerResource) {
			ListServerResource<?> listServerResource = (ListServerResource<?>) resource;
			Class<?> entityResource = listServerResource.getAssociatedEntityResource();
			if (entityResource != null && sourceWrapper.getConvertedSource() instanceof List) {
				try {
					EntityServerResource<?> newInstance = (EntityServerResource<?>) entityResource.newInstance();
					newInstance.init(resource.getContext(), resource.getRequest(), resource.getResponse());
					newInstance.release();
					List<Linkheader> linkheader = newInstance.getLinkheaderAuthorized();

					List<?> sourceAsList = (List<?>) sourceWrapper.getConvertedSource();
					for (Object object : sourceAsList) {
						if (!(object instanceof Map)) {
							continue;
						}
						StringBuilder sb = new StringBuilder();

						String id = guessId(object);
						linkheader.stream().filter(lh -> {
							return lh.getRole().equals(LinkheaderRole.DEFAULT);
						}).forEach(link -> addLinkHeader(link, newInstance, id, listServerResource, sb));
						((Map<String, Object>) object).put("_links", sb.toString());
					}

				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		} else if (skysailResource instanceof EntityServerResource) {
			EntityServerResource<?> entityServerResource = (EntityServerResource<?>) resource;
			Class<? extends ListServerResource<?>> associatedListServerResource = entityServerResource
			        .getAssociatedListServerResource();
			if (associatedListServerResource != null) {
				try {
					ListServerResource<?> newInstance = associatedListServerResource.newInstance();
					newInstance.init(resource.getContext(), resource.getRequest(), resource.getResponse());
					newInstance.release();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		addSubstitutions(sourceWrapper.getConvertedSource(), skysailResource, index, target);

		String inspect = CookiesUtils.getInpsectFromCookie(resource.getRequest());
		if (resource.getHostRef().getHostDomain().contains("localhost") && inspect != null
		        && inspect.equalsIgnoreCase("on")) {
			index.inspect();
		}
		String stringTemplateRenderedHtml = index.render();

		if (importedGroupBundleDir != null) {
			stGroup.addUsedTemplates(importedGroupBundleDir.getUsedTemplates());
		}
		String templatesHtml = isDebug() ? getTemplatesHtml() : "";
		StringRepresentation rep = new StringRepresentation(stringTemplateRenderedHtml.replace("%%templates%%",
		        templatesHtml));

		rep.setMediaType(MediaType.TEXT_HTML);

		point.collect();
		return rep;
	}

	private String guessId(Object object) {
		if (((Map<String, String>) object).get("id") != null) {
			Object value = ((Map<String, String>) object).get("id");
			return value.toString();
		} else if (((Map<String, String>) object).get("@rid") != null) {
			return ((Map<String, String>) object).get("@rid").replace("#", "");
		} else {
			return "";
		}
	}

	/**
	 * As the templates used for creating the output cannot be added to the
	 * output itself during creation time, they are added in an additional step
	 * here
	 */
	private String getTemplatesHtml() {
		return stGroup.getUsedTemplates().stream().map(template -> "<li>" + template + "</li>")
		        .collect(Collectors.joining("\n"));
	}

	private STGroupBundleDir createStGroup(Resource resource, String mediaType) {
		SkysailApplication currentApplication = (SkysailApplication) resource.getApplication();
		Bundle appBundle = currentApplication.getBundle();
		String resourcePath = ("/templates/" + mediaType).replace("/*", "");
		logger.info("reading templates from resource path '{}'", resourcePath);
		URL templatesResource = appBundle.getResource("/templates");// resourcePath);
		if (templatesResource != null) {
			STGroupBundleDir stGroup = new STGroupBundleDir(appBundle, resource, "/templates");// resourcePath);
			importTemplate("skysail.server.ext.converter.st", resource, appBundle, resourcePath, stGroup);
			importTemplate("skysail.server.documentation", resource, appBundle, resourcePath, stGroup);
			return stGroup;
		} else {
			Optional<Bundle> thisBundle = findBundle(appBundle, "skysail.server.ext.converter.st");
			return new STGroupBundleDir(thisBundle.get(), resource, resourcePath);
		}
	}

	private void importTemplate(String symbolicName, Resource resource, Bundle appBundle, String resourcePath,
	        STGroupBundleDir stGroup) {
		Optional<Bundle> theBundle = findBundle(appBundle, symbolicName);
		if (theBundle.isPresent()) {
			if (theBundle.get().getResource(resourcePath) != null) {
				importedGroupBundleDir = new STGroupBundleDir(theBundle.get(), resource, resourcePath);
				stGroup.importTemplates(importedGroupBundleDir);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void addSubstitutions(Object source, SkysailServerResource<?> resource, ST decl, Variant target) {

		decl.add("user", SecurityUtils.getSubject());
		decl.add("target", new STTargetWrapper(target));
		decl.add("converter", this);
		decl.add("services", new STServicesWrapper(menuProviders, searchService, resource));
		decl.add("resource", new STResourceWrapper(source, resource));

		List<FormField> fields = null;

		if (source instanceof List) {
			decl.add("source", new STListSourceWrapper((List<Object>) source));
			Object entity;
			try {
				entity = resource.getParameterType().newInstance();
				fields = getInheritedFields(resource.getParameterType()).stream()
				        //
				        .filter(f -> test(resource, f))
				        //
				        .sorted((f1, f2) -> sort(resource, f1, f2))
				        .map(f -> new FormField(f, userManager, source, entity))//
				        .collect(Collectors.toList());

				decl.add("fields", new STFieldsWrapper(fields));
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		} else {
			decl.add("source", new STSourceWrapper(source));
			if (source != null && (source instanceof SkysailResponse)) {
				Object entity = ((SkysailResponse<?>) source).getEntity();
				fields = getInheritedFields(entity.getClass()).stream()
				        //
				        .filter(f -> test(resource, f))
				        .map(f -> new FormField(f, userManager, source, ((SkysailResponse<?>) source).getEntity()))//
				        .collect(Collectors.toList());

				decl.add("fields", new STFieldsWrapper(fields));
			} else if (source != null && (source instanceof HashMap)) {

			} else {

			}
		}
		Map<String, String> messages = resource.getMessages(fields);
		messages.put(PRODUCT_NAME, getProductName());
		decl.add("messages", messages);

	}

	private int sort(SkysailServerResource<?> resource, Field f1, Field f2) {
		List<String> fieldNames = resource.getFields();
		return fieldNames.indexOf(f1.getName()) - fieldNames.indexOf(f2.getName());
	}

	private String getProductName() {
		return "???";
	}

	private Optional<Bundle> findBundle(Bundle bundle, String bundleName) {
		Bundle[] bundles = bundle.getBundleContext().getBundles();
		Optional<Bundle> thisBundle = Arrays.stream(bundles).filter(b -> b.getSymbolicName().equals(bundleName))
		        .findFirst();
		return thisBundle;
	}

	private boolean test(SkysailServerResource<?> resource, Field field) {
		List<String> fieldNames = resource.getFields();
		return (field.getAnnotation(de.twenty11.skysail.api.forms.Field.class) != null && fieldNames.contains(field
		        .getName()));
	}

	private List<java.lang.reflect.Field> getInheritedFields(Class<?> type) {
		List<java.lang.reflect.Field> result = new ArrayList<java.lang.reflect.Field>();

		Class<?> i = type;
		while (i != null && i != Object.class) {
			while (i != null && i != Object.class) {
				for (java.lang.reflect.Field field : i.getDeclaredFields()) {
					if (!field.isSynthetic()) {
						result.add(field);
					}
				}
				i = i.getSuperclass();
			}
		}

		return result;
	}

	public boolean isDebug() {
		return "debug".equalsIgnoreCase(templateNameFromCookie);
	}

	public boolean isEdit() {
		return "edit".equalsIgnoreCase(templateNameFromCookie);
	}

	@Override
	public void handleEvent(Event event) {
		events.add(event);
	}

	public List<Notification> getNotifications() {
		String currentUser = SecurityUtils.getSubject().getPrincipal().toString();
		if (currentUser == null) {
			return Collections.emptyList();
		}
		List<Notification> result = new ArrayList<>();
		List<String> oldMessages = getOldMessages(currentUser);
		events.stream().forEach(e -> {
			String msg = (String) e.getProperty("msg");
			String type = (String) e.getProperty("type");
			if (!oldMessages.contains(msg)) {
				oldMessages.add(msg);
				result.add(new Notification(msg, type));
			}
		});
		return result;
	}

	private List<String> getOldMessages(String currentUser) {
		List<String> oldMessages = userMsgsList.get(currentUser);
		if (oldMessages == null) {
			oldMessages = new ArrayList<String>();
			userMsgsList.put(currentUser, oldMessages);
		}
		return oldMessages;
	}

	private void addLinkHeader(Linkheader link, Resource entityResource, String id, ListServerResource<?> resource,
	        StringBuilder sb) {
		String path = link.getUri();
		String href = StringParserUtils.substitutePlaceholders(path, entityResource);

		// hmmm... last resort
		if (id != null && href.contains("{") && href.contains("}")) {
			// path = path.replace("{id}", id);
			href = href.replaceFirst(StringParserUtils.placeholderPattern.toString(), id);
		}

		sb.append("<a class='btn btn-mini' href='").append(href).append("'>").append(link.getTitle())
		        .append("</a>&nbsp;");

		resource.getLinkheader().add(
		        new Linkheader.Builder(href).relation(LinkHeaderRelation.ITEM)
		                .title("item " + id == null ? "unknown" : id).build());

	}

}
