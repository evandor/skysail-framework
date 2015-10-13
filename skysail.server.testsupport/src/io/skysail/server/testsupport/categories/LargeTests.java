package io.skysail.server.testsupport.categories;

/**
 * marker interface for junit categories.
 *
 * Typically these tests start and configure an OSGi framework, the bundles in question are
 * installed and started and the annotated tests are run. Usually, the database will be
 * in-memory, but this can be configured accoring to your needs on a per-test-class base.
 */
public interface LargeTests {

}
