package org.sugarframework.test;

import org.sugarframework.View;
import org.sugarframework.component.common.Html;

@View(value="NavTest")
public class NavTest {
	
/*
	@ComponentGroup ( style = ComponentGroupStyle.TABS,
						@Group("First label"),
						@Group("Second Label"),
						@Group("Third Label") ) Void componentGroup;

	
	@WithinGroup("First label") @Html("#{components.desc.h1.heading}") Void h1;

	@WithinGroup("Second Label") @Html("#{components.desc.h2.heading}") Void h2;
	*/

	@Html("#{components.desc.h3.heading}") Void h3;

	@Html("#{components.desc.h4.heading}") Void h4;

	@Html("#{components.desc.h5.heading}") Void h5;

	@Html("#{components.desc.h6.heading}") Void h6;

	
}
