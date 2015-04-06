package io.skysail.server.restlet.filter;

import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.resources.EntityServerResource;
import io.skysail.server.restlet.resources.SkysailServerResource;

import java.text.ParseException;

import org.codehaus.jettison.json.JSONObject;
import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.Parameter;

import de.twenty11.skysail.server.core.restlet.ResponseWrapper;
import de.twenty11.skysail.server.services.EncryptorService;

public class OptionalEncryptionFilter<R extends SkysailServerResource<T>, T> extends AbstractResourceFilter<R, T> {

    private SkysailApplication application;

    public OptionalEncryptionFilter(SkysailApplication application) {
        this.application = application;
    }

    @Override
    protected FilterResult doHandle(R resource, ResponseWrapper<T> responseWrapper) {
        EncryptorService encryptorService = application.getEncryptorService();
        if (encryptorService == null) {
            return super.doHandle(resource, responseWrapper);
        }

        Response response = responseWrapper.getResponse();
        Form form = (Form) response.getRequest().getAttributes().get(EntityServerResource.SKYSAIL_SERVER_RESTLET_FORM);

        if (encryptorService != null) {
            encryptFields(resource, form, encryptorService);
        }

        Object data;
        try {
            data = getDataFromRequest(response.getRequest(), resource);
            if (data instanceof JSONObject) {
                responseWrapper.setJson((JSONObject) data);
            } else {
                responseWrapper.setEntity((T) data);
            }
        } catch (ParseException e) {
            throw new RuntimeException("could not parse form", e);
        }
        return super.doHandle(resource, responseWrapper);
    }

    private void encryptFields(R resource, Form form, EncryptorService encryptorService) {
        if (form == null) {
            return;
        }
        for (int i = 0; i < form.size(); i++) {
            Parameter parameter = form.get(i);
            String originalValue = parameter.getValue();

            if (application != null && resource instanceof EntityServerResource) {
                Class<? extends Object> cls = ((EntityServerResource<T>) resource)
                        .getEntity().getClass();
                cls = resource.getParameterType();
                String encryptionParameter = application.getEncryptionParameter(cls, parameter.getName());
                if (encryptionParameter != null && encryptionParameter.trim().length() > 0) {
                    String password = form.getFirstValue(encryptionParameter);
                    if (password != null && password.trim().length() > 0) {
                        parameter.setValue(encryptorService.encryptText(originalValue, password));
                    }
                }
            }
        }
    }

   
}
