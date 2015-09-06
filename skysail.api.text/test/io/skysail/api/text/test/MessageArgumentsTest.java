package io.skysail.api.text.test;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.api.text.MessageArguments;

import java.util.*;

import org.junit.*;
import org.mockito.Mockito;
import org.restlet.resource.Resource;

public class MessageArgumentsTest {

    private MessageArguments messageArguments;
    private Resource aResource;
    private String resourceMessageIdentifier;

    @Before
    public void setUp() throws Exception {
        aResource = Mockito.mock(Resource.class);
        resourceMessageIdentifier = aResource.getClass().getName() + ".message";
        messageArguments = new MessageArguments(aResource.getClass()) //
                .add("A", 7) //
                .add("B", "X") //
                .setNewIdentifier("testname") //
                .add("1", "b");
    }

    @Test
    public void messageArguments_has_two_identifiers_with_proper_names() {
        Set<String> identifier = messageArguments.getIdentifier();

        assertThat(identifier.size(),is(2));
        assertThat(identifier, hasItem("testname"));
        assertThat(identifier, hasItem(resourceMessageIdentifier));
    }

    @Test
    public void resourceMessage_has_two_items_with_proper_content() {
        Collection<Object> msgArguments = messageArguments.get(resourceMessageIdentifier);
        assertThat(msgArguments.size(),is(2));
        assertThat(msgArguments, hasItem(7));
        assertThat(msgArguments, hasItem("X"));
    }

}
