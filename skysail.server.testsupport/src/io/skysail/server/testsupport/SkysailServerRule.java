package io.skysail.server.testsupport;

import org.junit.rules.ExternalResource;

import aQute.bnd.build.*;
import aQute.lib.io.IO;
public class SkysailServerRule extends ExternalResource {


    private Workspace ws;
    private Project project;
    private Run run;
    private ProjectLauncher bndLauncher;
    private Thread launcherThread;

    @Override
    protected void before() throws Throwable {
        ws = new Workspace(IO.getFile("../"));
        project = ws.getProject("skysail.product.todos");
        run = Workspace.getRun(IO.getFile("todos.test.bndrun"));
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
        Thread.currentThread().sleep(35000);
    }

    @Override
    protected void after() {
        //driver.quit();
        launcherThread.stop();
        run.close();
        project.close();
        ws.close();
    }
}
