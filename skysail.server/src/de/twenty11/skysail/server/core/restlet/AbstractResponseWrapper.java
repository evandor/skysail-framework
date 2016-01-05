package de.twenty11.skysail.server.core.restlet;

import java.util.ArrayList;
import java.util.List;

import org.restlet.Response;

import io.skysail.api.responses.ConstraintViolationsResponse;
import io.skysail.domain.Identifiable;
import lombok.Getter;
import lombok.Setter;

public abstract class AbstractResponseWrapper<T extends Identifiable> implements Wrapper<T> {

    @Getter
    @Setter
    protected Response response;

    protected ConstraintViolationsResponse<T> constraintViolationsResponse;
    
    @Getter
    private List<String> errors = new ArrayList<>();

    @Getter
    private List<String> warnings = new ArrayList<>();

    @Getter
    private List<String> info = new ArrayList<>();

    @Override
    public void addError(String msg) {
        errors.add(msg);
    }

    @Override
    public void addInfo(String msg) {
        info.add(msg);
    }

    @Override
    public void addWarning(String msg) {
        warnings.add(msg);
    }

}
