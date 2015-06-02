/**
 */
package skysailApplication;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Application</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link skysailApplication.Application#getName <em>Name</em>}</li>
 *   <li>{@link skysailApplication.Application#getEntities <em>Entities</em>}</li>
 * </ul>
 * </p>
 *
 * @see skysailApplication.SkysailApplicationPackage#getApplication()
 * @model
 * @generated
 */
public interface Application extends EObject {
    /**
     * Returns the value of the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Name</em>' attribute.
     * @see #setName(String)
     * @see skysailApplication.SkysailApplicationPackage#getApplication_Name()
     * @model
     * @generated
     */
    String getName();

    /**
     * Sets the value of the '{@link skysailApplication.Application#getName <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Name</em>' attribute.
     * @see #getName()
     * @generated
     */
    void setName(String value);

    /**
     * Returns the value of the '<em><b>Entities</b></em>' containment reference list.
     * The list contents are of type {@link skysailApplication.Entity}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Entities</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Entities</em>' containment reference list.
     * @see skysailApplication.SkysailApplicationPackage#getApplication_Entities()
     * @model containment="true"
     * @generated
     */
    EList<Entity> getEntities();

} // Application
