package org.sugarframework.test;

import org.sugarframework.Order;
import org.sugarframework.View;
import org.sugarframework.component.common.BlockQuote;
import org.sugarframework.security.RestrictRole;

@RestrictRole("admin")
@View(value="#{int.page.title}", url="int")
public class Internationalization {

	@Order(1) @BlockQuote({ "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer posuere erat a ante.",
	"Someone famous in Source Title" })
	Void blockQuote;
	
	@Order(2) @BlockQuote({ "#{int.blockquote.title}", "#{int.blockquote.message}" })
	Void blockQuote2;
	
	@Order(0) @BlockQuote({ "#{int.blockquote.title}", "#{int.blockquote.message}" })
	Void blockQuote3;

	
	
}
