package io.skysail.server.testsupport;

import org.junit.rules.ExternalResource;

import aQute.bnd.build.*;
import aQute.lib.io.IO;
public class SkysailServerRule extends ExternalResource {

    private Run run;
    private ProjectLauncher bndLauncher;
    private Thread launcherThread;
    private String bndrunFile;

    public SkysailServerRule(String bndrunFile) {
        this.bndrunFile = bndrunFile;
    }

    @SuppressWarnings("static-access")
    @Override
    protected void before() throws Throwable {
        run = Workspace.getRun(IO.getFile(bndrunFile));
        bndLauncher = run.getProjectLauncher();
        bndLauncher.prepare();

        launcherThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    bndLauncher.launch();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "Test Launcher");

        launcherThread.start();
        Thread.currentThread().sleep(30000);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void after() {
        //driver.quit();
        launcherThread.stop();
        run.close();
    }
}
