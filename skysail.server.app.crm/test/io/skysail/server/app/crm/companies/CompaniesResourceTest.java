//package io.skysail.server.app.crm.companies;
//
//import static org.hamcrest.CoreMatchers.equalTo;
//import static org.hamcrest.CoreMatchers.is;
//import static org.junit.Assert.assertThat;
//import io.skysail.api.validation.DefaultValidationImpl;
//import io.skysail.server.app.crm.CrmRepository;
//import io.skysail.server.app.crm.companies.resources.CompaniesResource;
//import io.skysail.server.app.crm.test.CrmAppTest;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mockito;
//import org.mockito.runners.MockitoJUnitRunner;
//import org.restlet.data.Form;
//
//@RunWith(MockitoJUnitRunner.class)
//public class CompaniesResourceTest extends CrmAppTest {
//
//    @InjectMocks
//    private CompaniesResource resource;
//
//    @Before
//    public void setUp() throws Exception {
//        super.setUp();
//        CrmRepository crmRepository = new CrmRepository();
//        Mockito.when(app.getValidatorService()).thenReturn(new DefaultValidationImpl());
//
//        crmRepository.setDbService(dbService);
//        Mockito.when(app.getValidatorService()).thenReturn(new DefaultValidationImpl());
//        resource.init(null, request, response);
//        form = Mockito.mock(Form.class);
//
//    }
//
//    @Test
//    public void testName() throws Exception {
//
//    }
//
//    // @Test
//    // @Ignore
//    // public void html_request_retrieves_data_from_dbService() {
//    // dbService.persist(new CompanyWithId("admin", "7"));
//    // Variant variant = new Variant(MediaType.TEXT_HTML);
//    // List<String> entities = resource.getAsJson(variant);
//    // assertThat(entities.size(), is(1));
//    //
//    // assertThat(response.getStatus().getCode(), is(200));
//    //
//    // // assertValidationFailed(400, "Validation failed");
//    // // assertOneConstraintViolation((ConstraintViolationsResponse<?>)
//    // // result, "name", "may not be null");
//    // }
//
//    protected void assertValidationFailed(int statusCode, String xStatusReason) {
//        assertThat(response.getStatus().getCode(), is(statusCode));
//        assertThat(response.getHeaders().getFirst("X-Status-Reason").getValue(), is(equalTo(xStatusReason)));
//    }
//
//}
