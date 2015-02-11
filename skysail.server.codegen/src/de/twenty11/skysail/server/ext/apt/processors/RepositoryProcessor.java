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
//import de.twenty11.skysail.server.ext.apt.annotations.GenerateRepository;
//
//@SupportedAnnotationTypes("de.twenty11.skysail.server.ext.apt.annotations.GenerateRepository")
//@SupportedSourceVersion(javax.lang.model.SourceVersion.RELEASE_8)
//public class RepositoryProcessor extends Processors {
//
//	@Override
//	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
//		if (annotations.isEmpty()) {
//			return true;
//		}
//
//		for (Element e : roundEnv.getElementsAnnotatedWith(GenerateRepository.class)) {
//			if (e.getKind() == ElementKind.CLASS) {
//				//createRepository(e);
//			}
//		}
//
//		return true;
//	}
//
//	private void createRepository(Element e) {
//		JavaFileObject jfo;
//		try {
//			jfo = processingEnv.getFiler().createSourceFile(
//			        e.getEnclosingElement().toString() + "." + e.getSimpleName() + "sRepository");
//			processingEnv.getMessager().printMessage(javax.tools.Diagnostic.Kind.NOTE,
//			        "creating source file: " + jfo.toUri());
//
//			Writer writer = jfo.openWriter();
//
//			STGroup group = new STGroupFile("repository/Repository.stg", '$', '$');
//			ST st = group.getInstanceOf("repository");
//			st.add("name", e.getSimpleName());
//			st.add("package", e.getEnclosingElement().toString());
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
//}
