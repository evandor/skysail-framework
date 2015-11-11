package io.skysail.server.testsupport.categories;

/**
 * marker interface for junit categories.
 *
 * Usually, tests annotated with this interface are selenium-based and act on the
 * resource classes directly. An in-memory database might be involved, but there is
 * no web driver.
 */
public interface MediumTests {

}
