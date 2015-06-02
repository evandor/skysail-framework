/**
 */
package skysailApplication;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see skysailApplication.SkysailApplicationPackage
 * @generated
 */
public interface SkysailApplicationFactory extends EFactory {
    /**
     * The singleton instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    SkysailApplicationFactory eINSTANCE = skysailApplication.impl.SkysailApplicationFactoryImpl.init();

    /**
     * Returns a new object of class '<em>Application</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Application</em>'.
     * @generated
     */
    Application createApplication();

    /**
     * Returns a new object of class '<em>Entity</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Entity</em>'.
     * @generated
     */
    Entity createEntity();

    /**
     * Returns the package supported by this factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the package supported by this factory.
     * @generated
     */
    SkysailApplicationPackage getSkysailApplicationPackage();

} //SkysailApplicationFactory
