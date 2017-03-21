package org.sugarframework.test;

import org.sugarframework.Order;
import org.sugarframework.View;
import org.sugarframework.component.ParagraphStyle;
import org.sugarframework.component.TextStyle;
import org.sugarframework.component.common.Action;
import org.sugarframework.component.common.Paragraph;
import org.sugarframework.security.RestrictPermission;
import org.sugarframework.security.RestrictRole;
import org.sugarframework.test.component.HelloWorld;

@RestrictRole("admin")
@View(value="RestrictView", url="restrictview", icon="glyphicon glyphicon-fire")
public class RestrictViewTest {
	
	@Order(1) @RestrictRole("admin")
	@HelloWorld(heading="ADMIN ONLY") private String value = "Yo mang, what's up?";

	@Order(0) @Paragraph("#{components.paragraph}") Void p;

	@Order(2) @RestrictPermission("winnebago:drive:eagle5")
	@Paragraph(value = "winnebago:drive:eagle5",
			style = ParagraphStyle.PARAGRAPH_LEAD, textStyle = TextStyle.DANGER) Void p2;

	
	@Order(4) @RestrictRole("admin")
	@Action
	public void secureOperation(String string, int age, short number){
		
	}
	
	@Order(3) @RestrictPermission("winnebago:drive:eagle5")
	@Action
	public void driveWinnebago(){
		
	}
	
}
