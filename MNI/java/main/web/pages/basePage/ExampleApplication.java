package pages.basePage;

import pages.homePage.HomePage;
import wicket.ISessionFactory;
import wicket.Session;
import wicket.protocol.http.WebApplication;
import wicket.spring.injection.annot.SpringComponentInjector;

public class ExampleApplication extends WebApplication {

	public void init() {
		addComponentInstantiationListener(new SpringComponentInjector(this));
	}
	
    /**
     * @see wicket.protocol.http.WebApplication#getSessionFactory()
     */
    public ISessionFactory getSessionFactory() {
    	
        return new ISessionFactory() {
        	
			public Session newSession() {
                return new ExampleSession(ExampleApplication.this);
            }
        };
    }

	/**
	 * @see wicket.Application#getHomePage()
	 */
	@SuppressWarnings("unchecked")
	public Class getHomePage() {
		return HomePage.class;
	}

}
