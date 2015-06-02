/**
 */
package skysailApplication;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see skysailApplication.SkysailApplicationFactory
 * @model kind="package"
 * @generated
 */
public interface SkysailApplicationPackage extends EPackage {
    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "skysailApplication";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http://io/skysail/server/emf/skysailapplication";

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "io.skysail.server.emf.skysailapplication";

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    SkysailApplicationPackage eINSTANCE = skysailApplication.impl.SkysailApplicationPackageImpl.init();

    /**
     * The meta object id for the '{@link skysailApplication.impl.ApplicationImpl <em>Application</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see skysailApplication.impl.ApplicationImpl
     * @see skysailApplication.impl.SkysailApplicationPackageImpl#getApplication()
     * @generated
     */
    int APPLICATION = 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION__NAME = 0;

    /**
     * The feature id for the '<em><b>Entities</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION__ENTITIES = 1;

    /**
     * The number of structural features of the '<em>Application</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Application</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int APPLICATION_OPERATION_COUNT = 0;


    /**
     * The meta object id for the '{@link skysailApplication.impl.EntityImpl <em>Entity</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see skysailApplication.impl.EntityImpl
     * @see skysailApplication.impl.SkysailApplicationPackageImpl#getEntity()
     * @generated
     */
    int ENTITY = 1;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ENTITY__NAME = 0;

    /**
     * The number of structural features of the '<em>Entity</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ENTITY_FEATURE_COUNT = 1;

    /**
     * The number of operations of the '<em>Entity</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ENTITY_OPERATION_COUNT = 0;


    /**
     * Returns the meta object for class '{@link skysailApplication.Application <em>Application</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Application</em>'.
     * @see skysailApplication.Application
     * @generated
     */
    EClass getApplication();

    /**
     * Returns the meta object for the attribute '{@link skysailApplication.Application#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see skysailApplication.Application#getName()
     * @see #getApplication()
     * @generated
     */
    EAttribute getApplication_Name();

    /**
     * Returns the meta object for the containment reference list '{@link skysailApplication.Application#getEntities <em>Entities</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Entities</em>'.
     * @see skysailApplication.Application#getEntities()
     * @see #getApplication()
     * @generated
     */
    EReference getApplication_Entities();

    /**
     * Returns the meta object for class '{@link skysailApplication.Entity <em>Entity</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Entity</em>'.
     * @see skysailApplication.Entity
     * @generated
     */
    EClass getEntity();

    /**
     * Returns the meta object for the attribute '{@link skysailApplication.Entity#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see skysailApplication.Entity#getName()
     * @see #getEntity()
     * @generated
     */
    EAttribute getEntity_Name();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    SkysailApplicationFactory getSkysailApplicationFactory();

    /**
     * <!-- begin-user-doc -->
     * Defines literals for the meta objects that represent
     * <ul>
     *   <li>each class,</li>
     *   <li>each feature of each class,</li>
     *   <li>each operation of each class,</li>
     *   <li>each enum,</li>
     *   <li>and each data type</li>
     * </ul>
     * <!-- end-user-doc -->
     * @generated
     */
    interface Literals {
        /**
         * The meta object literal for the '{@link skysailApplication.impl.ApplicationImpl <em>Application</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see skysailApplication.impl.ApplicationImpl
         * @see skysailApplication.impl.SkysailApplicationPackageImpl#getApplication()
         * @generated
         */
        EClass APPLICATION = eINSTANCE.getApplication();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute APPLICATION__NAME = eINSTANCE.getApplication_Name();

        /**
         * The meta object literal for the '<em><b>Entities</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference APPLICATION__ENTITIES = eINSTANCE.getApplication_Entities();

        /**
         * The meta object literal for the '{@link skysailApplication.impl.EntityImpl <em>Entity</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see skysailApplication.impl.EntityImpl
         * @see skysailApplication.impl.SkysailApplicationPackageImpl#getEntity()
         * @generated
         */
        EClass ENTITY = eINSTANCE.getEntity();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ENTITY__NAME = eINSTANCE.getEntity_Name();

    }

} //SkysailApplicationPackage
