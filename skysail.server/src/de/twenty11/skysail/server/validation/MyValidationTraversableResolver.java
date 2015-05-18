//package de.twenty11.skysail.server.validation;
//
//import java.lang.annotation.ElementType;
//
//import javax.validation.Path;
//
//import org.hibernate.validator.internal.engine.resolver.DefaultTraversableResolver;
//
///**
// *  http://stackoverflow.com/questions/25862444/how-to-fix-compatibility-issue-with-jsr-303-validation-and-orientdb 
// *
// */
//public class MyValidationTraversableResolver extends DefaultTraversableResolver {
//
//    public MyValidationTraversableResolver() {
//        // we simply override the constructor to disable jpa detection
//    }
//
//    @Override
//    public boolean isReachable(Object traversableObject, Path.Node traversableProperty, Class<?> rootBeanType,
//            Path pathToTraversableObject, ElementType elementType) {
//        return true;
//    }
//
//    @Override
//    public boolean isCascadable(Object traversableObject, Path.Node traversableProperty, Class<?> rootBeanType,
//            Path pathToTraversableObject, ElementType elementType) {
//        return true;
//    }
//}
