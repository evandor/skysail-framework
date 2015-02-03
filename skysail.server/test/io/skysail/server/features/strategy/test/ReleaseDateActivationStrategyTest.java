//package io.skysail.server.features.strategy.test;
//
//import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.Matchers.equalTo;
//import static org.junit.Assert.assertThat;
//import io.skysail.server.features.strategy.ReleaseDateActivationStrategy;
//
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import de.twenty11.skysail.api.features.FeatureState;
//import de.twenty11.skysail.server.core.restlet.SecurityFeatures;
//import de.twenty11.skysail.server.um.domain.SkysailUser;
//
//public class ReleaseDateActivationStrategyTest {
//    private FeatureState state;
//    private Map<String, String> config;
//    private SkysailUser user = new SkysailUser("username");
//    private ReleaseDateActivationStrategy strategy;
//
//    @Before
//    public void setUp() {
//        config = new HashMap<String, String>();
//        strategy = new ReleaseDateActivationStrategy();
//    }
//
//    @Test
//    public void provides_its_name() throws Exception {
//        assertThat(strategy.getName(), is(equalTo("Release date")));
//    }
//
//    @Test
//    public void is_not_active_if_no_releaseDate_is_provided() {
//        state = new FeatureState(SecurityFeatures.ALLOW_ORIGIN_FEATURE);
//        assertThat(strategy.isActive(state, "user"), is(false));
//    }
//
//    @Test
//    public void is_not_active_for_futureReleaseDate() {
//        config.put(ReleaseDateActivationStrategy.PARAM_DATE, "3000-11-20");
//        config.put(ReleaseDateActivationStrategy.PARAM_TIME, "13:14:15");
//        state = new FeatureState(SecurityFeatures.ALLOW_ORIGIN_FEATURE, config, true);
//        assertThat(strategy.isActive(state, "user"), is(false));
//    }
//
//    @Test
//    public void is_active_for_passed_releaseDate() {
//        config.put(ReleaseDateActivationStrategy.PARAM_DATE, "2000-01-20");
//        config.put(ReleaseDateActivationStrategy.PARAM_TIME, "13:14:55");
//        state = new FeatureState(SecurityFeatures.ALLOW_ORIGIN_FEATURE, config, true);
//        assertThat(strategy.isActive(state, "user"), is(true));
//    }
//
//    @Test
//    public void accepts_missing_time() {
//        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//        config.put(ReleaseDateActivationStrategy.PARAM_DATE, today);
//        state = new FeatureState(SecurityFeatures.ALLOW_ORIGIN_FEATURE, config, true);
//        assertThat(strategy.isActive(state, "user"), is(true));
//    }
//
//    @Test
//    public void is_not_active_for_unparseableDate() {
//        config.put(ReleaseDateActivationStrategy.PARAM_DATE, "30002110");
//        config.put(ReleaseDateActivationStrategy.PARAM_TIME, "13:14:15");
//        state = new FeatureState(SecurityFeatures.ALLOW_ORIGIN_FEATURE, config, true);
//        assertThat(strategy.isActive(state, "user"), is(false));
//    }
//
// }
