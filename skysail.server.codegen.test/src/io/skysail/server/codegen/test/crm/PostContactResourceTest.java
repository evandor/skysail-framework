package io.skysail.server.codegen.test.crm;
//package io.skysail.server.ext.apt.test_.crm;
//
//import static org.hamcrest.Matchers.*;
//import static org.junit.Assert.assertThat;
//import io.skysail.api.validation.ValidatorService;
//import io.skysail.server.db.DbService;
//import io.skysail.server.ext.apt.test_.crm.contacts.*;
//
//import java.util.Arrays;
//import java.util.concurrent.*;
//
//import javax.validation.Validator;
//
//import org.apache.shiro.subject.Subject;
//import org.junit.*;
//import org.junit.rules.ExpectedException;
//import org.junit.runner.RunWith;
//import org.mockito.*;
//import org.mockito.runners.MockitoJUnitRunner;
//import org.restlet.*;
//import org.restlet.data.*;
//
//import de.twenty11.skysail.server.core.restlet.RouteBuilder;
//
//@RunWith(MockitoJUnitRunner.class)
//public class PostContactResourceTest extends io.skysail.server.ext.apt.test_.crm.ResourceTest {
//
//	@Rule
//	public ExpectedException thrown = ExpectedException.none();
//
//	@InjectMocks
//	private PostContactResource resource;
//
//	@Mock
//	private CrmGen clipboardApplication;
//
//	@Mock
//	private DbService dbService;
//
//	private ConcurrentMap<String, Object> attributes;
//
//	protected ContactsRepository contactsRepository;
//
//	private Request request;
//
//	private Response response;
//
//	@Before
//	public void setUp() throws Exception {
//		contactsRepository = ContactsRepository.getInstance();
//		contactsRepository.setDbService(dbService);
//		attributes = new ConcurrentHashMap<String, Object>();
//		Subject subjectUnderTest = Mockito.mock(Subject.class);
//		setSubject(subjectUnderTest);
//		request = Mockito.mock(Request.class);
//		Mockito.when(request.getClientInfo()).thenReturn(new ClientInfo());
//		Mockito.when(request.getAttributes()).thenReturn(attributes);
//		Reference targetRef = Mockito.mock(Reference.class);
//		Reference resourceRef = Mockito.mock(Reference.class);
//		Mockito.when(request.getResourceRef()).thenReturn(resourceRef);
//		Mockito.when(resourceRef.getTargetRef()).thenReturn(targetRef);
//		response = new Response(request);
//
//		ValidatorService validatorService = Mockito.mock(ValidatorService.class);
//		Validator validator = Mockito.mock(Validator.class);
//        Mockito.when(validatorService.getValidator()).thenReturn(validator);
//        Mockito.when(clipboardApplication.getValidatorService()).thenReturn(validatorService);
//
//        RouteBuilder routeBuilder = Mockito.mock(RouteBuilder.class);
//        Mockito.when(clipboardApplication.getRouteBuilders(Mockito.any())).thenReturn(Arrays.asList(routeBuilder));
//	}
//
//	@Test
//	public void creates_registration_template() throws Exception {
//		resource.init(null, null, null);
//		Contact template = resource.createEntityTemplate();
//		assertThat(template.getName(), is(nullValue()));
//	}
//
//	@Test
//	// TODO check, this seems wrong
//	public void returns_bad_request_for_empty_form() {
//		resource.init(null, request, response);
//		resource.post(new Form());
//		assertThat(response.getStatus().getCode(), is(303));
//	}
//
//	@Test
//	@Ignore
//	public void returns_bad_request_for_contact_with_too_short_name() {
//		resource.init(null, request, response);
//		Form form = new Form();
//		form.add("name", "A");
//		resource.post(form);
//		assertThat(response.getStatus().getCode(), is(400));
//	}
//
//}
