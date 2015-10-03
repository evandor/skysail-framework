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
    private String projectName;
    private String bndrunFile;

    public SkysailServerRule(String project, String bndrunFile) {
        this.projectName = project;
        this.bndrunFile = bndrunFile;
    }

    @Override
    protected void before() throws Throwable {
        ws = new Workspace(IO.getFile("../"));
        project = ws.getProject(projectName);
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
