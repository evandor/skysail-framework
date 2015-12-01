package io.skysail.server.app.designer.entities;

import java.util.regex.Pattern;

import javax.validation.*;

public class CheckValidJavaIdentifierValidator  implements ConstraintValidator<CheckValidJavaIdentifier, String> {

    private Pattern pattern = Pattern.compile("[a-zA-Z_$][a-zA-Z\\d_$]*");
    
    private CheckValidJavaIdentifier cvji;

    @Override
    public void initialize(CheckValidJavaIdentifier cvji) {
        this.cvji = cvji;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext ctx) {
        return false;
    }

}