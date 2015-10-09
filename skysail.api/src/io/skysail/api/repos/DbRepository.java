package io.skysail.api.repos;


/**
 * marker interface, typically defined like this:
 *
 * <pre><code>
 *    {@literal @}Component(immediate = true, property = "name=TheRepository")
 *    public class XYZRepository implements DbRepository {
 *
 *        {@ literal} Reference
 *        private DbService dbService;
 *
 *        (...)
 *    }
 * </code></pre>
 *
 * and used like this:
 *
 * <pre><code>
 *    {@literal @}Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY, target = "(name=TheRepository)")
 *    private Repository repository;
 * </code></pre>
 *
 */
public interface DbRepository extends Repository {

}
