package io.skysail.server.db.it;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.db.it.clip.*;
import io.skysail.server.db.it.clip.resources.*;

import java.util.*;
import java.util.stream.Collectors;

import org.junit.*;

public class ClipDbTests extends DbIntegrationTests {

    private PostClipResource postClipResource;
    private ClipResource clipResource;
    private ClipsResource clipsResource;
    private PutClipResource putClipResource;

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
    }

    @Test
    public void creates_a_new_simple_entity() {
        Clip simpleEntity = createSimpleEntity();
        SkysailResponse<Clip> post = postClipResource.post(simpleEntity, HTML_VARIANT);
        assertThat(post.getEntity(),is(simpleEntity));
        assertThat(post.getEntity().getCreated(),is(notNullValue()));
        assertThat(post.getEntity().getModified(),is(nullValue()));
    }

    @Test
    public void retrieves_a_clip_by_id() {
        Clip clip = createSimpleEntity();
        String newClipId = postClipResource.post(clip, HTML_VARIANT).getEntity().getId();
        request.addAttribute("id", newClipId);
        Clip clipFromDb = clipResource.getEntity();
        assertThat(clipFromDb.getTitle(),is(clip.getTitle()));
    }

    @Test
    public void retrieves_the_list_of_clips() {
        Clip newClip = createSimpleEntity();
        postClipResource.post(newClip, HTML_VARIANT);
        postClipResource.post(newClip, HTML_VARIANT);
        List<Clip> clips = clipsResource.getEntities(HTML_VARIANT).getEntity();
        assertThat(clips.stream().filter(clip -> clip.getTitle().equals(newClip.getTitle())).collect(Collectors.toList()).size(),is(2));
    }

    @Test
    public void clip_can_be_updated() {
        Clip clip = createSimpleEntity();
        String clipId = postClipResource.post(clip, HTML_VARIANT).getEntity().getId();
        request.addAttribute("id", clipId);
        clip.setTitle("changed");
        putClipResource.putEntity(clip, HTML_VARIANT).getEntity();

        Clip clipFromDb = clipResource.getEntity();
        assertThat(clipFromDb.getModified(),is(notNullValue()));
        assertThat(clipFromDb.getTitle(),is("changed"));
    }

    private Clip createSimpleEntity() {
        String name = Long.toString(new Date().getTime());
        Clip simpleEntity = new Clip(name);
        return simpleEntity;
    }

}
