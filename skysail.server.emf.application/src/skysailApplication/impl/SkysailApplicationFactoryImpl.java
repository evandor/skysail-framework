/**
 */
package skysailApplication.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import skysailApplication.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class SkysailApplicationFactoryImpl extends EFactoryImpl implements SkysailApplicationFactory {
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static SkysailApplicationFactory init() {
        try {
            SkysailApplicationFactory theSkysailApplicationFactory = (SkysailApplicationFactory)EPackage.Registry.INSTANCE.getEFactory(SkysailApplicationPackage.eNS_URI);
            if (theSkysailApplicationFactory != null) {
                return theSkysailApplicationFactory;
            }
        }
        catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new SkysailApplicationFactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SkysailApplicationFactoryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EObject create(EClass eClass) {
        switch (eClass.getClassifierID()) {
            case SkysailApplicationPackage.APPLICATION: return createApplication();
            case SkysailApplicationPackage.ENTITY: return createEntity();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Application createApplication() {
        ApplicationImpl application = new ApplicationImpl();
        return application;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Entity createEntity() {
        EntityImpl entity = new EntityImpl();
        return entity;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SkysailApplicationPackage getSkysailApplicationPackage() {
        return (SkysailApplicationPackage)getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    @Deprecated
    public static SkysailApplicationPackage getPackage() {
        return SkysailApplicationPackage.eINSTANCE;
    }

} //SkysailApplicationFactoryImpl
