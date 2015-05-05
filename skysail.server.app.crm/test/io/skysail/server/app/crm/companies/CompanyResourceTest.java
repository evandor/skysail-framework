//package io.skysail.server.app.crm.companies;
//
//import io.skysail.api.validation.DefaultValidationImpl;
//import io.skysail.server.app.crm.CrmRepository;
//import io.skysail.server.app.crm.companies.resources.CompanyResource;
//import io.skysail.server.app.crm.test.CrmAppTest;
//
//import org.junit.Before;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mockito;
//import org.mockito.runners.MockitoJUnitRunner;
//import org.restlet.data.Form;
//
//@RunWith(MockitoJUnitRunner.class)
//public class CompanyResourceTest extends CrmAppTest {
//
//    @InjectMocks
//    private CompanyResource resource;
//
//    @Before
//    public void setUp() throws Exception {
//        super.setUp();
//        CrmRepository crmRepository = new CrmRepository();
//        Mockito.when(app.getValidatorService()).thenReturn(new DefaultValidationImpl());
//
//        crmRepository.setDbService(dbService);
//        Mockito.when(app.getValidatorService()).thenReturn(new DefaultValidationImpl());
//        Mockito.when(app.getRepository()).thenReturn(crmRepository);
//        resource.init(null, request, response);
//        form = Mockito.mock(Form.class);
//    }
//
////    @Test
////    public void test() {
////        dbService.persist(new Company("admin"));
////        Company companys = resource.getEntity();
////
////        assertThat(response.getStatus().getCode(), is(200));
////
////        // assertValidationFailed(400, "Validation failed");
////        // assertOneConstraintViolation((ConstraintViolationsResponse<?>)
////        // result, "name", "may not be null");
////    }
//
//}
