package io.skysail.server.db.it;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.server.db.it.clip.*;
import io.skysail.server.db.it.clip.resources.*;
import io.skysail.server.testsupport.categories.LargeTests;

import java.util.*;
import java.util.stream.Collectors;

import org.junit.*;
import org.junit.experimental.categories.Category;
import org.restlet.data.Form;
import org.restlet.representation.Variant;

@Category(LargeTests.class)
public class ClipDbTests extends DbIntegrationTests {

    private PostClipResource postClipResource;
    private ClipResource clipResource;
    private ClipsResource clipsResource;
    private PutClipResource putClipResource;

    private Clip aNewClip;
    private Form form;

    @BeforeClass
    public static void beforeClass() {
        skysailApplication = ClipApplication.getInstance();
    }

    @Before
    public void setUp() throws Exception {
        postClipResource = new PostClipResource();
        clipResource = new ClipResource();
        clipsResource = new ClipsResource();
        putClipResource = new PutClipResource();

        setupEntityResource(postClipResource);
        setupEntityResource(clipResource);
        setupEntityResource(clipsResource);
        setupEntityResource(putClipResource);

        request.clearAttributes();
        form = new Form();
        aNewClip = createClip();
    }

    @Test
    public void html_variant_creates_a_new_clip() {
        Clip clip = postClipResource.post(aNewClip, HTML_VARIANT).getEntity();
        assertNewClipWithTitle(clip, aNewClip.getTitle());
    }

    @Test
    public void json_variant_creates_a_new_clip() {
        form.add("title", aNewClip.getTitle());
        Clip clip = postClipResource.post(form, JSON_VARIANT).getEntity();
        assertNewClipWithTitle(clip, aNewClip.getTitle());
    }

    @Test
    public void html_variant_retrieves_a_clip_by_id() {
        Clip clip = postClipResource.post(aNewClip, HTML_VARIANT).getEntity();
        assertNewClipWithTitle(clip, aNewClip.getTitle());
    }

    @Test
    public void json_variant_retrieves_a_clip_by_id() {
        form.add("title", aNewClip.getTitle());
        String id = postClipResource.post(form, HTML_VARIANT).getEntity().getId();
        getByIdAndAssertTitle(id, aNewClip.getTitle());
    }

    @Test
    public void html_variant_retrieves_the_list_of_clips() {
        postClipResource.post(new Clip("html_variant_retrieves_the_list_of_clips"), HTML_VARIANT);
        postClipResource.post(new Clip("html_variant_retrieves_the_list_of_clips"), HTML_VARIANT);

        List<Clip> clips = clipsResource.getEntities(HTML_VARIANT).getEntity();

        assertThat(
                clips.stream().filter(clip -> clip.getTitle().equals("html_variant_retrieves_the_list_of_clips")).collect(Collectors.toList())
                        .size(), is(2));
    }

    @Test
    public void json_variant_retrieves_the_list_of_clips() {
        postClipResource.post(new Clip("json_variant_retrieves_the_list_of_clips"), HTML_VARIANT);
        postClipResource.post(new Clip("json_variant_retrieves_the_list_of_clips"), HTML_VARIANT);
        List<Clip> clips = clipsResource.getEntities(JSON_VARIANT).getEntity();
        assertThat(
                clips.stream().filter(clip -> clip.getTitle().equals("json_variant_retrieves_the_list_of_clips")).collect(Collectors.toList())
                        .size(), is(2));
    }

    @Test
    public void html_variant_clip_can_be_updated() {
        request.addAttribute("id", postClipResource.post(aNewClip, HTML_VARIANT).getEntity().getId());
        aNewClip.setTitle("changed");
        putClipResource.putEntity(aNewClip, HTML_VARIANT).getEntity();

        assertModifiedClipWithTitle(clipResource.getEntity(), "changed");
    }

    @Test
    public void json_variant_clip_can_be_updated() {
        request.addAttribute("id", postClipResource.post(aNewClip, HTML_VARIANT).getEntity().getId());
        aNewClip.setTitle("changed");
        putClipResource.putEntity(aNewClip, JSON_VARIANT).getEntity();

        assertModifiedClipWithTitle(clipResource.getEntity(), "changed");
    }

    @Test
    public void html_variant_clip_can_be_deleted_again() {
        String id = postClipResource.post(aNewClip, HTML_VARIANT).getEntity().getId();
        deleteClip(id, HTML_VARIANT);
        assertThat(clipResource.getEntity(), is(nullValue()));
    }

    @Test
    public void json_variant_clip_can_be_deleted_again() {
        String id = postClipResource.post(aNewClip, HTML_VARIANT).getEntity().getId();
        deleteClip(id, JSON_VARIANT);
        assertThat(clipResource.getEntity(), is(nullValue()));
    }

    private Clip createClip() {
        String name = Long.toString(new Date().getTime());
        Clip simpleEntity = new Clip(name);
        return simpleEntity;
    }

    private void assertNewClipWithTitle(Clip clip, String title) {
        assertThat(clip.getTitle(), is(aNewClip.getTitle()));
        assertThat(clip.getCreated(), is(notNullValue()));
        assertThat(clip.getModified(), is(nullValue()));
    }

    private void assertModifiedClipWithTitle(Clip clip, String title) {
        assertThat(clip.getTitle(), is(aNewClip.getTitle()));
        assertThat(clip.getCreated(), is(notNullValue()));
        assertThat(clip.getModified(), is(notNullValue()));
    }

    private void getByIdAndAssertTitle(String id, String title) {
        request.addAttribute("id", id);
        Clip clipFromDb = clipResource.getEntity();
        assertThat(clipFromDb.getTitle(), is(title));
    }

    private void deleteClip(String id, Variant variant) {
        request.addAttribute("id", id);
        clipResource.deleteEntity(variant);
    }



}
