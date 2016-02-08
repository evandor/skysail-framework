package io.skysail.server.app.designer.codegen.writer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;

import io.skysail.server.app.designer.model.DesignerApplicationModel;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProjectFileWriter {

    public static void save(DesignerApplicationModel applicationModel, String buildPathFolder, String classNameToPath, byte[] entityCode) {
        String sourceFilename = getProjectPath(applicationModel) + "/"+buildPathFolder+"/"
                + classNameToPath;
        sourceFilename = sourceFilename.replace("//", "/");
        try {
            Path path = Paths.get(sourceFilename);
            new File(path.getParent().toString()).mkdirs();
            Files.write(path, entityCode);
        } catch (IOException e) {
            log.debug("could not write source code for compilation unit '{}' to '{}'", classNameToPath, sourceFilename);
        }
    }

    public static String getProjectPath(DesignerApplicationModel applicationModel) {
        return applicationModel.getPath() + "/" + applicationModel.getProjectName();
    }

    public static void mkdirs(@NonNull String path) {
        File file = Paths.get(path.replace("//", "/")).normalize().toFile();
        log.info("creating directories for path '{}' if they do not exist", file.getAbsolutePath());
        file.mkdirs();
    }

    public static void deleteDir(String path) {
        File file = new File(Paths.get(path.replace("//", "/")).normalize().toString());
        log.info("deleting directory '{}'", file.getAbsolutePath());
        try {
            FileUtils.deleteDirectory(file);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

}
