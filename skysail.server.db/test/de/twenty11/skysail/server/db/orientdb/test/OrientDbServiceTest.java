package de.twenty11.skysail.server.db.orientdb.test;


public class OrientDbServiceTest {

	private void importInitialData() {

        // // CREATE A NEW PROXIED OBJECT AND FILL IT
        // SkysailUser admin = db.newInstance(SkysailUser.class);
        // admin.setUsername("admin");
        // admin.setPassword("$2a$12$52R8v2QH3vQRz8NcdtOm5.HhE5tFPZ0T/.MpfUa9rBzOugK.btAHS");
        //
        // db.save(admin);

//        ODatabaseDocumentTx importDb = new ODatabaseDocumentTx(getDbUrl());
//        importDb.open("admin", "admin");
//        try {
//            OCommandOutputListener listener = new OCommandOutputListener() {
//                @Override
//                public void onMessage(String iText) {
//                    System.out.print(iText);
//                }
//            };
//
//            try {
//                ComponentContext componentContext;
//				URL resource = componentContext.getBundleContext().getBundle().getResource("resources/import.json");
//                ODatabaseImport importer = new ODatabaseImport(importDb, resource.openStream(), listener);
//                importer.importDatabase();
//                importer.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } finally {
//            db.close();
//        }
    }

	private String getDbUrl() {
	    // TODO Auto-generated method stub
	    return null;
    }
}
