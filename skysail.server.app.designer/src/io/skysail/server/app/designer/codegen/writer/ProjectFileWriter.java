package io.skysail.server.app.designer.codegen.writer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import io.skysail.server.app.designer.model.DesignerApplicationModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProjectFileWriter {

    public static void save(DesignerApplicationModel applicationModel, String buildPathFolder, String classNameToPath, byte[] entityCode) {
        String sourceFilename = applicationModel.getPath() + "/" + applicationModel.getProjectName() + "/"+buildPathFolder+"/"
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

}
