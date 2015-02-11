package de.twenty11.skysail.server.ext.apt.processors;

import io.skysail.server.ext.apt.model.entities.Entity;
import io.skysail.server.ext.apt.model.entities.EntityGraph;
import io.skysail.server.ext.apt.model.entities.Reference;
import io.skysail.server.ext.apt.model.types.RepositoryModel;
import io.skysail.server.ext.apt.model.types.TypeModel;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.JavaFileObject;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import de.twenty11.skysail.server.ext.apt.Processors;
import de.twenty11.skysail.server.ext.apt.STElement;
import de.twenty11.skysail.server.ext.apt.annotations.GenerateEntityResource;
import de.twenty11.skysail.server.ext.apt.annotations.GenerateListResource;
import de.twenty11.skysail.server.ext.apt.annotations.GeneratePostResource;
import de.twenty11.skysail.server.ext.apt.annotations.GeneratePutResource;
import de.twenty11.skysail.server.ext.apt.annotations.SkysailApplication;

@SupportedAnnotationTypes("de.twenty11.skysail.server.ext.apt.annotations.SkysailApplication")
@SupportedSourceVersion(javax.lang.model.SourceVersion.RELEASE_8)
public class ApplicationProcessor extends Processors {

    @Override
    public boolean doProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) throws Exception {
        for (Element skysailApplicationElement : roundEnv.getElementsAnnotatedWith(SkysailApplication.class)) {
            if (skysailApplicationElement.getKind() == ElementKind.CLASS) {
                graph = analyseEntities(roundEnv, skysailApplicationElement);
                typeModel = new TypeModel(graph, skysailApplicationElement);

                createRepositories(typeModel);
                createApplication(skysailApplicationElement, roundEnv);
                createRootResource(skysailApplicationElement, roundEnv);
                createEntityResources(skysailApplicationElement, roundEnv, graph);
                createListResources(skysailApplicationElement, roundEnv, graph);
                createPostResources(skysailApplicationElement, roundEnv, graph);
                createPutResources(skysailApplicationElement, roundEnv, graph);
            }
        }
        return true;
    }

    private void createRepositories(TypeModel model) {
        List<RepositoryModel> repos = model.getRepos();
        repos.stream().forEach(repo -> createRepo(repo));
    }

    private void createRepo(RepositoryModel repo) {
        JavaFileObject jfo;
        try {
            jfo = createSourceFile(repo.getPackageName() + "." + repo.getTypeName());

            Writer writer = jfo.openWriter();
            STGroup group = new STGroupFile("repository/Repository.stg", '$', '$');
            ST st = group.getInstanceOf("repository");
            st.add("repo", repo);
            st.add("linkedClasses", repo.getLinkedClasses());
            st.add("imports", repo.getImports());
            String result = st.render();
            writer.append(result);
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private EntityGraph analyseEntities(RoundEnvironment roundEnv, Element skysailApplicationElement) {
        Set<? extends Element> entities = getSubElements(roundEnv, GenerateEntityResource.class,
                skysailApplicationElement);

        Set<Entity> nodes = new HashSet<>();
        Set<Reference> edges = new HashSet<>();

        // create nodes
        for (Element entity : entities) {
            printMessage("adding entity: " + entity.toString());
            nodes.add(new Entity(entity));
        }

        // create edges
        for (Element entity : entities) {

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

    private void createApplication(Element applicationElement, RoundEnvironment roundEnv) throws Exception {

        JavaFileObject jfo = createSourceFile((getTypeName(applicationElement) + "Gen"));

        SkysailApplication annotation = applicationElement.getAnnotation(SkysailApplication.class);
        String annotations = "";
        if (!annotation.extend()) {
            annotations = "@Component(immediate = true)";
        }

        createApplicationSource(jfo, roundEnv, annotations);
    }

    private void createApplicationSource(JavaFileObject jfo, RoundEnvironment roundEnv, String annotations)
            throws IOException {
        Writer writer = jfo.openWriter();

        STGroup group = new STGroupFile("application/Application.stg", '$', '$');
        ST st = group.getInstanceOf("application");
        st.add("postResources", getElements2(roundEnv, GeneratePostResource.class, graph));
        st.add("putResources", getElements2(roundEnv, GeneratePutResource.class, graph));
        st.add("listResources", getElements2(roundEnv, GenerateListResource.class, graph));
        st.add("entityResources", getElements2(roundEnv, GenerateEntityResource.class, graph));
        st.add("annotations", annotations);
        st.add("applicationModel", typeModel.getApplication());
        st.add("rootResource", typeModel.getRootResource());
        String result = st.render();

        writer.append(result);
        writer.close();
    }

    private void createRootResource(Element element, RoundEnvironment roundEnv) throws Exception {
        JavaFileObject jfo = createSourceFile(element.getEnclosingElement().toString() + "." + "RootResource");

        Writer writer = jfo.openWriter();

        STGroup group = new STGroupFile("RootResource.stg", '$', '$');
        ST st = group.getInstanceOf("rootResource");
        st.add("name", element.getSimpleName() != null ? element.getSimpleName() : "unknown");
        st.add("package", element.getEnclosingElement().toString());
        st.add("entityResources", getElements(roundEnv, GenerateEntityResource.class));
        String result = st.render();

        writer.append(result);
        writer.close();
    }

    private void createEntityResources(Element skysailApplicationElement, RoundEnvironment roundEnv, EntityGraph graph)
            throws Exception {
        Set<? extends Element> entityElements = getSubElements(roundEnv, GenerateEntityResource.class,
                skysailApplicationElement);

        for (Element entityElement : entityElements) {
            // GenerateEntityResource annotation =
            // skysailApplicationElement.getAnnotation(GenerateEntityResource.class);
            String linkheader = "";// Arrays.stream(annotation.linkheader()).map(lh
                                   // -> lh + ".class")
            // .collect(Collectors.joining(","));

            Entity entity = toEntity(graph, entityElement);

            List<Reference> references = graph.getOutgoingEdges(entity);
            linkheader = calcLinkheader(references, entity);
            String imports = references.stream().map(r -> {
                return "import " + r.getTo().getPackageName() + ".*;\n";
            }).collect(Collectors.joining());

            // List<Reference> incomingEdges = graph.getIncomingEdges(entity);

            JavaFileObject jfo = createSourceFile(getTypeName(entityElement) + "Resource");
            Writer writer = jfo.openWriter();
            STGroup group = new STGroupFile("entityResource/EntityResource.stg", '$', '$');
            ST st = group.getInstanceOf("entity");
            st.add("element", new STElement(entityElement, graph, GenerateEntityResource.class));
            st.add("entityResources", getElements2(roundEnv, GenerateEntityResource.class, graph));
            st.add("linkheader", linkheader);
            st.add("imports", imports);
            writer.append(st.render());
            writer.close();
        }

    }

    private Entity toEntity(EntityGraph graph, Element entityElement) {
        return graph.getNode(entityElement.getEnclosingElement().toString(), entityElement.getSimpleName().toString());
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
        linkheader += "Put"+simpleName+"Resource.class";
        return linkheader;
    }

    private void createListResources(Element skysailApplicationElement, RoundEnvironment roundEnv, EntityGraph graph) {
        Set<? extends Element> entityElements = getSubElements(roundEnv, GenerateListResource.class,
                skysailApplicationElement);

        for (Element entityElement : entityElements) {
            GenerateListResource annotation = entityElement.getAnnotation(GenerateListResource.class);
            String linkheader = Arrays.stream(annotation.linkheader()).map(lh -> lh + ".class")
                    .collect(Collectors.joining(","));
            if (linkheader == null || linkheader.trim().length() == 0) {
                linkheader = "Post" + entityElement.getSimpleName() + "Resource.class";
            }

            try {
                JavaFileObject jfo = createSourceFile(getTypeName(entityElement) + "sResource");

                Writer writer = jfo.openWriter();

                STGroup group = new STGroupFile("listResource/ListResource.stg", '$', '$');
                ST st = group.getInstanceOf("list");
                st.add("name", entityElement.getSimpleName());
                st.add("package", entityElement.getEnclosingElement().toString());
                st.add("imports", getImports(entityElement, graph));
                st.add("linkheader", linkheader);
                st.add("getData", getData(entityElement, graph));
                String result = st.render();
                writer.append(result);

                writer.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
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

    private String getData(Element element, EntityGraph graph) {
        Entity entity = toEntity(graph, element);
        List<Reference> incomingEdges = graph.getIncomingEdges(entity);
        if (incomingEdges.size() == 0) {
            return "return " + entity.getSimpleName() + "sRepository.getInstance().get" + entity.getSimpleName()
                    + "s();";
        }
        Reference reference = incomingEdges.get(0);
        String simpleName = reference.getFrom().getSimpleName();
        return "return " + simpleName + "sRepository.getInstance().getById(id).getEntities();";
    }

    private void createPostResources(Element skysailApplicationElement, RoundEnvironment roundEnv, EntityGraph graph)
            throws IOException {

        Set<? extends Element> entityElements = getSubElements(roundEnv, GeneratePostResource.class,
                skysailApplicationElement);

        for (Element entityElement : entityElements) {
            JavaFileObject jfo = createSourceFile(entityElement.getEnclosingElement().toString() + ".Post"
                    + entityElement.getSimpleName() + "Resource");

            Writer writer = jfo.openWriter();

            STGroup group = new STGroupFile("postResource/PostResource.stg", '$', '$');
            ST st = group.getInstanceOf("post");
            st.add("name", entityElement.getSimpleName());
            st.add("package", entityElement.getEnclosingElement().toString());
            st.add("appName", typeModel.getApplication().getTypeName());
            st.add("appPkg", typeModel.getApplication().getPackageName());
            st.add("addEntity", addEntity(entityElement, graph));
            st.add("imports", getImports(entityElement, graph));
            String result = st.render();
            writer.append(result);

            writer.close();

        }

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

    private void createPutResources(Element skysailApplicationElement, RoundEnvironment roundEnv, EntityGraph graph)
            throws IOException {

        Set<? extends Element> entityElements = getSubElements(roundEnv, GeneratePutResource.class,
                skysailApplicationElement);

        for (Element entityElement : entityElements) {
            JavaFileObject jfo = createSourceFile(entityElement.getEnclosingElement().toString() + ".Put"
                    + entityElement.getSimpleName() + "Resource");

            Writer writer = jfo.openWriter();
            STGroup group = new STGroupFile("putResource/PutResource.stg", '$', '$');
            ST st = group.getInstanceOf("put");
            st.add("name", entityElement.getSimpleName());
            st.add("package", entityElement.getEnclosingElement().toString());
            st.add("appName", typeModel.getApplication().getTypeName());
            st.add("appPkg", typeModel.getApplication().getPackageName());
            String result = st.render();
            writer.append(result);
            writer.close();
        }

    }

}
