package pages.basePage;

import wicket.protocol.http.WebApplication;
import wicket.protocol.http.WebSession;

@SuppressWarnings("serial")
public class ExampleSession extends WebSession {

	
	protected ExampleSession(final WebApplication application) {
		super(application);
	}

}
