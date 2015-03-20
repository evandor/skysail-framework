//package io.skysail.server.converter.renderer;
//
//import java.lang.reflect.Field;
//import java.util.Locale;
//import java.util.Optional;
//
//import org.restlet.resource.Resource;
//import org.stringtemplate.v4.AttributeRenderer;
//
//import io.skysail.api.responses.ConstraintViolationsResponse;
//import de.twenty11.skysail.server.core.FormField;
//
//public class FormFieldRenderer implements AttributeRenderer {
//
//    private Resource resource;
//    private Object source;
//
//    public FormFieldRenderer(Resource resource, Object source) {
//        this.resource = resource;
//        this.source = source;
//    }
//
//    @Override
//    public String toString(Object o, String formatString, Locale locale) {
//        FormField formField = (FormField) o;
//        Field field = formField.getFieldAnnotation();
//        io.skysail.api.forms.Field fieldAnnotation = field
//                .getAnnotation(io.skysail.api.forms.Field.class);
//
//        if ("javascript".equals(formatString)) {
//            return javascriptHtml(formField, field);
//        }
//        StringBuilder sb = new StringBuilder();
//
//        switch (fieldAnnotation.type()) {
//        case TAGS:
//            sb = new StringBuilder("<div name='test' id='s2id_").//
//                    append(formField.getFieldAnnotation().getName()).//
//                    append("' class='select2-container select2-container-multi' style='width:420px'/>");
//            sb.append("");
//            break;
//        case PASSWORD:
//            sb = new StringBuilder("<input type='password' class='form-control' name='").//
//                    append(formField.getFieldAnnotation().getName()).//
//                    append("' placeholder='' value='").//
//                    append(formField.getValue() != null ? formField.getValue() : "").append("'>");
//            break;
//        default:
//            if (source instanceof ConstraintViolationsResponse) {
//                return pre(formField, true) + inputWithError(formField, (ConstraintViolationsResponse<?>) source)
//                        + post();
//            }
//            sb = new StringBuilder("<input type='text' class='form-control' name='").//
//                    append(formField.getFieldAnnotation().getName()).//
//                    append("' placeholder='' value='").//
//                    append(formField.getValue() != null ? formField.getValue() : "").append("'>");
//            break;
//        }
//        return pre(formField, false) + sb.toString() + post();
//    }
//
//    private String pre(FormField formField, boolean hasError) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("<div class='form-group").append(hasError ? " has-error has-feedback" : "").append("'>");
//        String fieldName = formField.getName();
//        sb.append("<label for='").append(fieldName).append("' class='col-sm-2 control-label'>").append(fieldName)
//                .append("</label>");
//        sb.append("<div class='col-sm-10'>");
//        return sb.toString();
//    }
//
//    private String post() {
//        StringBuilder sb = new StringBuilder();
//        sb.append("</div>");
//        sb.append("</div>");
//        return sb.toString();
//    }
//
//    private String inputWithError(FormField formField, ConstraintViolationsResponse<?> source) {
//        String fieldName = formField.getName();
//        Optional<String> validationMessage = source.getViolations().stream()
//                .filter(v -> v.getPropertyPath().equals(fieldName)).map(v -> v.getMessage()).findFirst();
//        StringBuilder sb = new StringBuilder("<input type='text' class='form-control' name='")
//                .//
//                append(fieldName)
//                .//
//                append("' value='")
//                .//
//                append(formField.getValue() != null ? formField.getValue() : "")
//                .append("' aria-describedby='")
//                .append(fieldName)
//                .append("Status'>")
//                .//
//                append("<span class='glyphicon glyphicon-remove form-control-feedback' aria-hidden='true'></span>")
//                .//
//                append("<span id='")
//                .append(fieldName)
//                .append("Status' class='sr-only'>(error)</span>")
//                .//
//                append("<small style='' class='help-block' data-bv-validator='notEmpty' data-bv-for='title' data-bv-result='INVALID'>")
//                .append(validationMessage.orElse("")).append("</small>");
//        return sb.toString();
//    }
//
//    private String javascriptHtml(FormField formField, Field field) {
//        return "$(document).ready(function() { $('#s2id_" + formField.getName()
//                + "').select2({tags:['red', 'green', 'blue']}); });";
//    }
//
// }
