package fi.onion.util;

import org.springframework.context.ApplicationContext;

public class AppContext {

	private static ApplicationContext ctx;
	
	public static void setApplicationContext(ApplicationContext appContext) {
		ctx = appContext;
	}

	public static ApplicationContext getApplicationContext() {
		return ctx;
	}
}
