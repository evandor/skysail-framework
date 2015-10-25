package io.skysail.server.codegen.apt.processors;

import io.skysail.server.codegen.apt.stringtemplate.MySTGroupFile;
import io.skysail.server.ext.apt.annotations.GenerateResources;
import io.skysail.server.ext.apt.model.entities.*;
import io.skysail.server.ext.apt.model.types.TypeModel2;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

import javax.annotation.processing.*;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.tools.JavaFileObject;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.stringtemplate.v4.*;

import de.twenty11.skysail.server.ext.apt.annotations.ResourceType;

@SupportedAnnotationTypes("io.skysail.server.ext.apt.annotations.GenerateResources")
@SupportedSourceVersion(javax.lang.model.SourceVersion.RELEASE_8)
@Slf4j
public class EntityProcessor extends Processors {

    private static final String GENERATED_ANNOTATION = "@Generated(\"io.skysail.server.codegen.apt.processors.EntityProcessor\")";

    @Override
    public boolean doProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) throws Exception {
        Set<? extends Element> generateResourceElements = roundEnv.getElementsAnnotatedWith(GenerateResources.class);
        graph = analyse(roundEnv, generateResourceElements);
        typeModel2 = new TypeModel2(graph);
        generateResourceElements.stream().forEach(entity -> {
            try {
                createRepository(entity);
                createEntityResource(roundEnv, graph, entity);
                createListResource(roundEnv, graph, entity);
                createPostResource(roundEnv, graph, entity);
                createPutResource(roundEnv, graph, entity);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
        return true;
    }

    private EntityGraph analyse(RoundEnvironment roundEnv, Set<? extends Element> generateResourceElements) {
        Set<Entity> nodes = new HashSet<>();
        Set<Reference> edges = new HashSet<>();

        printHeadline("Analysing project for code generation");

        // create nodes
        for (Element entityElement : generateResourceElements) {
            printMessage("adding entity: " + entityElement.toString());
            nodes.add(new Entity(entityElement));
        }

        printMessage("");

        for (Element entity : generateResourceElements) {
            for (Element member : entity.getEnclosedElements()) {
                if (!(member instanceof VariableElement)) {
                    continue;
                }
                TypeMirror typeMirror = member.asType();

                if (typeMirror.toString().startsWith("java.util.List")) {
                    String targetMemberName = typeMirror.toString().substring("java.util.List".length() + 1,
                            typeMirror.toString().length() - 1);

                    Entity source = nodes.stream().filter(e -> {
                        return e.getElementName().equals(entity.toString());
                    }).findFirst().orElseThrow(IllegalStateException::new);

                    Entity target = nodes.stream().filter(e -> {
                        return e.getElementName().equals(targetMemberName);
                    }).findFirst().orElseThrow(IllegalStateException::new);

                    Reference edge = new Reference(source, target);
                    edges.add(edge);
                    printMessage("added edge: " + edge);
                }
            }
        }

        return new EntityGraph(nodes, edges);

    }

    private void createRepository(Element entityElement) {
        JavaFileObject jfo;
        try {
            jfo = createSourceFile(getTypeName(entityElement) + "Repo");

            Writer writer = jfo.openWriter();
            STGroup group = new MySTGroupFile("repository/Repository.stg", '$', '$');

            ST st = group.getInstanceOf("repository");
            st.add("package", entityElement.getEnclosingElement().toString());
            st.add("name", entityElement.getSimpleName());
            st.add("repo", ""); // repo
            st.add("linkedClasses", "");// repo.getLinkedClasses());
            st.add("imports", "");// repo.getImports());
            String result = st.render();
            writer.append(result);
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void createEntityResource(RoundEnvironment roundEnv, EntityGraph graph, Element entityElement)
            throws Exception {

        GenerateResources annotation = entityElement.getAnnotation(GenerateResources.class);
        String application = annotation.application();

        String linkheader = "";// Arrays.stream(annotation.linkheader()).map(lh
                               // -> lh + ".class")

        Entity entity = toEntity(graph, entityElement);

        List<Reference> references = graph.getOutgoingEdges(entity);
        linkheader = calcLinkheader(references, entity);
        String imports = references.stream().map(r -> {
            return "import " + r.getTo().getPackageName() + ".*;\n";
        }).collect(Collectors.joining());

        JavaFileObject jfo = createSourceFile(getTypeName(entityElement) + "Resource");
        Writer writer = jfo.openWriter();

        URL url = getURL("entityResource/EntityResource.stg");
        printMessage("URL " + url.toString());

        STGroup group = new MySTGroupFile("entityResource/EntityResource.stg", '$', '$');
        ST st = group.getInstanceOf("entity");
        st.add("package", entityElement.getEnclosingElement().toString());
        st.add("appName", application);
        st.add("name", entityElement.getSimpleName());
        st.add("linkheader", linkheader);
        st.add("imports", imports);
        writer.append(st.render());
        writer.close();

    }

    private void createListResource(RoundEnvironment roundEnv, EntityGraph graph, Element entityElement) throws IOException {

        GenerateResources annotation = entityElement.getAnnotation(GenerateResources.class);
        String application = annotation.application();

        JavaFileObject jfo = createSourceFile(getTypeName(entityElement) + "sResource");

        Writer writer = jfo.openWriter();

        STGroup group = new MySTGroupFile("listResource/ListResource.stg", '$', '$');
        ST st = group.getInstanceOf("list");
        st.add("name", entityElement.getSimpleName());
        st.add("appName", application);
        st.add("package", entityElement.getEnclosingElement().toString());
        st.add("imports", getImports(entityElement, graph));
        st.add("linkedResources", getLinksForListResource(entityElement.getSimpleName(), graph));
        st.add("getData", "");
        String result = st.render();
        writer.append(result);

        writer.close();
    }

    private String getLinksForListResource(Name entityName, EntityGraph graph) {
        String collection = graph.getNodes().stream().map(n -> n.getSimpleName()).map(name -> name + "sResource.class").collect(Collectors.joining(", "));
        return "Post" + entityName + "Resource.class, " + collection;
    }

    private void createPostResource(RoundEnvironment roundEnv, EntityGraph graph, Element entityElement)
            throws IOException {

        GenerateResources annotation = entityElement.getAnnotation(GenerateResources.class);
        String application = annotation.application();
        if (Arrays.asList(annotation.exclude()).contains(ResourceType.POST)) {
            return;
        }

        String typeName = entityElement.getEnclosingElement().toString() + ".Post" + entityElement.getSimpleName()
                + "Resource";
        // if (skipGeneration(typeName)) {
        // return;
        // }
        JavaFileObject jfo = createSourceFile(typeName);

        Writer writer = jfo.openWriter();

        STGroup group = new MySTGroupFile("postResource/PostResource2.stg", '$', '$');
        ST st = group.getInstanceOf("post");
        st.add("name", entityElement.getSimpleName());
        st.add("package", entityElement.getEnclosingElement().toString());
        st.add("appName", application);
        st.add("appPkg", typeModel2.getApplication().getPackageName());
        st.add("addEntity", addEntity(entityElement, graph));
        st.add("imports", getImports(entityElement, graph));
        String result = st.render();
        writer.append(result);

        writer.close();

    }

    private void createPutResource(RoundEnvironment roundEnv, EntityGraph graph, Element entityElement)
            throws IOException {
        GenerateResources annotation = entityElement.getAnnotation(GenerateResources.class);
        String application = annotation.application();

        JavaFileObject jfo = createSourceFile(entityElement.getEnclosingElement().toString() + ".Put"
                + entityElement.getSimpleName() + "Resource");

        Writer writer = jfo.openWriter();
        STGroup group = new MySTGroupFile("putResource/PutResource.stg", '$', '$');
        ST st = group.getInstanceOf("put");
        st.add("name", entityElement.getSimpleName());
        st.add("package", entityElement.getEnclosingElement().toString());
        st.add("appName", application);
        st.add("appPkg", typeModel2.getApplication().getPackageName());
        String result = st.render();
        writer.append(result);
        writer.close();
    }

    private String addEntity(Element entityElement, EntityGraph graph) {
        Entity entity = toEntity(graph, entityElement);
        List<Reference> incomingEdges = graph.getIncomingEdges(entity);
        if (incomingEdges.size() == 0) {
            String simpleName = entityElement.getSimpleName().toString();
            return "entity = " + simpleName + "sRepository.getInstance().add(entity);";
        }
        Reference reference = incomingEdges.get(0);
        StringBuilder sb = new StringBuilder();
        sb.append("Application app = ApplicationsRepository.getInstance().getById(id);\n");
        sb.append("app.getEntities().add(entity);\n");
        sb.append("ApplicationsRepository.getInstance().update(app);\n");
        return sb.toString();
    }

    private String getImports(Element entityElement, EntityGraph graph) {
        StringBuilder sb = new StringBuilder("import ").append(entityElement.getEnclosingElement().toString()).append(
                ".*;\n");
        List<Reference> incomingEdges = graph.getIncomingEdges(toEntity(graph, entityElement));
        if (incomingEdges.size() == 0) {
            return sb.toString();
        }
        Reference reference = incomingEdges.get(0);
        sb.append("import ").append(reference.getFrom().getPackageName()).append(".*;\n");
        return sb.toString();
    }

    private String calcLinkheader(List<Reference> references, Entity entity) {
        String linkheader;
        linkheader = references.stream().map(r -> {
            String simpleName = r.getTo().getSimpleName();
            return "Post" + simpleName + "Resource.class, " + simpleName + "sResource.class";
        }).collect(Collectors.joining(", "));
        String simpleName = entity.getSimpleName();
        if (linkheader.trim().length() > 0) {
            linkheader += ", ";
        }
        linkheader += "Put" + simpleName + "Resource.class";
        return linkheader;
    }

    private Entity toEntity(EntityGraph graph, Element entityElement) {
        return graph.getNode(entityElement.getEnclosingElement().toString(), entityElement.getSimpleName().toString());
    }

    private URL getURL(String fileName) {
        URL url;
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        url = cl.getResource(fileName);
        if ( url==null ) {
            cl = this.getClass().getClassLoader();
            url = cl.getResource(fileName);
        }
        return url;
    }

    private boolean skipGeneration(String typename) {
        String filename = "C:\\git\\skysail-framework\\skysail.server.codegen.test\\src" + File.separatorChar
                + typename.replace('.', File.separatorChar) + ".java";
        Path path = Paths.get(filename);
        if (!(new File(filename).exists())) {
            printMessage("file '" + filename + "' not found, start generation...");
            return false;
        }
        try (Stream<String> lines = Files.lines(path)) {
            Optional<String> hasAnnotation = lines.filter(s -> s.contains(GENERATED_ANNOTATION)).findFirst();
            if (hasAnnotation.isPresent()) {
                printMessage("file " + filename
                        + " will be regenerated, as it contains the proper @Generate annotation.");
                return false;
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        printMessage("file " + filename + " will not be regenerated, as it is missing the proper @Generate annotation");
        return true;
    }

    private void printHeadline(String msg) {
        printMessage("");
        printMessage(msg);
        printMessage(StringUtils.repeat("-", msg.length()));
        printMessage("");
    }
}
