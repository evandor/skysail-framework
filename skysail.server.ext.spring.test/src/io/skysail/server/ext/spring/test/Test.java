package io.skysail.server.ext.spring.test;

public class Test {

    public Test() {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");

        // instantiate our spring dao object from the application context
        FileEventDao fileEventDao = (FileEventDao) ctx.getBean("fileEventDao");

        // create a FileEventType object from the application context
        FileEventType fileEventType = (FileEventType) ctx.getBean("fileEventType");

        // insert the file event with the spring dao
        fileEventDao.doInsert(fileEventType);
    }
}
