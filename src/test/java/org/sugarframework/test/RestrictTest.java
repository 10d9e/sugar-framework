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

@View(value="Restrict", url="restrict", icon="glyphicon glyphicon-fire")
public class RestrictTest {
	
	@RestrictRole("admin")
	@Order(0) @HelloWorld(heading="ADMIN ONLY") private String value = "Yo mang, what's up?";

	@Order(13) @Paragraph("#{components.paragraph}") Void p;

	@RestrictPermission("winnebago:drive:eagle5")
	@Order(26) @Paragraph(value = "winnebago:drive:eagle5",
			style = ParagraphStyle.PARAGRAPH_LEAD, textStyle = TextStyle.DANGER) Void p2;

	
	@RestrictRole("admin")
	@Action
	public void secureOperation(String string, int age, short number){
		
	}
	
	@RestrictPermission("winnebago:drive:eagle5")
	@Action
	public void driveWinnebago(){
		
	}
	
}
