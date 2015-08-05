package de.twenty11.skysail.server.mgt.test;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

import java.util.concurrent.Callable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.twenty11.skysail.server.mgt.worker.SkysailManagerThreadPool;

public class SkysailManagerThreadPoolTest {

    private SkysailManagerThreadPool stp;

    public class StringTask implements Callable<String> {

        private int i;

        public StringTask(int i) {
            this.i = i;
        }

        @Override
        public String call() throws Exception {
            return "hi " + i;
        }

    }

    @Before
    public void setUp() throws Exception {
        stp = new SkysailManagerThreadPool();
        stp.start();
    }

    @After
    public void tearDown() {
        stp.stop();
    }

    @Test
    public void supports_starting_ten_StringTasks() {
        for (int i = 0; i < 10; i++) {
            stp.submit(new StringTask(i));
        }

        String result = "";
        for (int i = 0; i < 10; i++) {
            result += stp.get();
        }
        assertThat(result, containsString("hi 4"));
        assertThat(result, containsString("hi 8"));
    }
}
