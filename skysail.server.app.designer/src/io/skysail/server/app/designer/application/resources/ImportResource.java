package io.skysail.server.app.designer.application.resources;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import io.skysail.server.app.designer.application.*;
import io.skysail.server.app.designer.exceptions.MappingException;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class ImportResource extends PostEntityServerResource<ImportDefinition> {

    private static ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    
    @Override
    public ImportDefinition createEntityTemplate() {
        return new ImportDefinition();
    }
    
    @Override
    public void addEntity(ImportDefinition entity) {
        String content = entity.getYamlImport().replace("&#34;", "\"");
        try {
            DbApplication dbApplication = mapper.readValue(content, DbApplication.class);
            DesignerRepository.add(dbApplication, "entities");
        } catch (IOException e) {
            throw new MappingException(e.getMessage(), e);
        }
    }
    
    @Override
    public String redirectTo() {
        return super.redirectTo(ApplicationsResource.class);
    }

}
