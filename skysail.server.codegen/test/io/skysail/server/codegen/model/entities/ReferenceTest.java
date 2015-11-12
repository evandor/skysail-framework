package io.skysail.server.codegen.model.entities;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import io.skysail.server.codegen.model.entities.*;

import org.junit.Before;
import org.junit.Test;

public class ReferenceTest {

	private Reference reference;
	private AptEntity entity1;
	private AptEntity entity2;

	@Before
	public void setUp() {
		entity1 = new AptEntity("p1","e1");
		entity2 = new AptEntity("p1","e2");
		reference = new Reference(entity1, entity2);
	}

	@Test
	public void two_entities_are_equal_if_they_link_the_same_entities() throws Exception {
		Reference reference2 = new Reference(entity1, entity2);
		assertThat(reference, is(equalTo(reference2)));
		assertThat(reference.hashCode(), is(equalTo(reference2.hashCode())));
	}

	@Test
	public void two_entities_are_not_equal_if_one_entity_is_different() throws Exception {
		AptEntity entity3 = new AptEntity("p1", "e3");
		Reference reference2 = new Reference(entity1, entity3);
		assertThat(reference, is(not(equalTo(reference2))));
	}

	@Test
	public void toString_contains_both_entity1_and_entity2() throws Exception {
		Reference reference = new Reference(entity1, entity2);
		assertThat(reference.toString(), is(equalTo("AptEntity(p1, e1) > AptEntity(p1, e2)")));
	}

	@Test
	public void is_initialized_correctly() throws Exception {
		assertThat(reference.getFrom(), is(equalTo(entity1)));
		assertThat(reference.getTo(), is(equalTo(entity2)));
		assertThat(reference.getFromName(), is(equalTo("AptEntity(p1, e1)")));
		assertThat(reference.getToName(), is(equalTo("AptEntity(p1, e2)")));
	}
}
