package org.sugarframework.test;

import org.sugarframework.Context;
import org.sugarframework.DevelopmentMode;
import org.sugarframework.context.DefaultContextInitializer;

@Context(value = "Sparkles", style = "cosmo", urlContext = "sparkles", port = "8080", 
resourceDirectories = "resource", footerMessage = "© Magic Troll Nuts 2017")
@DevelopmentMode
public class TestContext {

	public static void main(String[] args) {
		DefaultContextInitializer.startContext(TestContext.class);
	}

}