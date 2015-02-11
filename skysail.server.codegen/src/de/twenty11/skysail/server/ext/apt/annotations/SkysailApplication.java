package de.twenty11.skysail.server.ext.apt.annotations;


public @interface SkysailApplication {

	/**
	 * setting this to true allows you to extend the generated class to
	 * overwrite it. In order to be used, you need the following annotation and
	 * implementation list:
	 * 
	 * <code>
	 * @Component(immediate = true) 
	 * public class Mailer extends MailerApplication implements ApplicationProvider, MenuItemProvider {
	 * ...
	 * }
	 * </code>
	 * @return
	 */
	boolean extend() default false;

}
