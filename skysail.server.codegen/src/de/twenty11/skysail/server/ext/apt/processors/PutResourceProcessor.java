//package de.twenty11.skysail.server.ext.apt.processors;
//
//import java.io.IOException;
//import java.io.Writer;
//import java.util.Set;
//
//import javax.annotation.processing.RoundEnvironment;
//import javax.annotation.processing.SupportedAnnotationTypes;
//import javax.annotation.processing.SupportedSourceVersion;
//import javax.lang.model.element.Element;
//import javax.lang.model.element.ElementKind;
//import javax.lang.model.element.TypeElement;
//import javax.tools.JavaFileObject;
//
//import org.stringtemplate.v4.ST;
//import org.stringtemplate.v4.STGroup;
//import org.stringtemplate.v4.STGroupFile;
//
//import de.twenty11.skysail.server.ext.apt.Processors;
//import de.twenty11.skysail.server.ext.apt.annotations.GeneratePutResource;
//import de.twenty11.skysail.server.ext.apt.annotations.SkysailApplication;
//
//@SupportedAnnotationTypes("de.twenty11.skysail.server.ext.apt.annotations.GeneratePutResource")
//@SupportedSourceVersion(javax.lang.model.SourceVersion.RELEASE_8)
//public class PutResourceProcessor extends Processors {
//
//	@Override
//	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
//		if (annotations.isEmpty()) {
//			return true;
//		}
//
//		Element skysailApplication = getElement(roundEnv, SkysailApplication.class);
//		if (skysailApplication == null) {
//			return true;
//		}
//
//		String applicationName = skysailApplication.getSimpleName().toString();
//		String applicationPkg = skysailApplication.getEnclosingElement().toString();
//
//		for (Element element : roundEnv.getElementsAnnotatedWith(GeneratePutResource.class)) {
//			if (element.getKind() == ElementKind.CLASS) {
//				createPutResource(element, applicationName, applicationPkg);
//			}
//		}
//
//		return true;
//	}
//
//	private void createPutResource(Element e, String applicationName, String applicationPkg) {
//		JavaFileObject jfo;
//		try {
//			jfo = processingEnv.getFiler().createSourceFile(
//			        e.getEnclosingElement().toString() + ".Put" + e.getSimpleName() + "Resource");
//			processingEnv.getMessager().printMessage(javax.tools.Diagnostic.Kind.NOTE,
//			        "creating source file: " + jfo.toUri());
//
//			Writer writer = jfo.openWriter();
//
//			STGroup group = new STGroupFile("putResource/PutResource.stg", '$', '$');
//			ST st = group.getInstanceOf("put");
//			st.add("name", e.getSimpleName());
//			st.add("package", e.getEnclosingElement().toString());
//			st.add("appName", applicationName);
//			st.add("appPkg", applicationPkg);
//			String result = st.render();
//
//			// vt.merge(vc, writer);
//			writer.append(result);
//
//			writer.close();
//		} catch (IOException ex) {
//			ex.printStackTrace();
//		}
//	}
//
//	@Override
//	public boolean doProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) throws Exception {
//		return false;
//	}
//
// }
