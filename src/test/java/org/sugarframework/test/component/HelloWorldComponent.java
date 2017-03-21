package org.sugarframework.test.component;

import java.lang.reflect.Field;

import org.sugarframework.SugarComponent;
import org.sugarframework.component.AbstractSugarComponent;
import org.sugarframework.component.Style;
import org.sugarframework.util.ComponentUtil;

@SugarComponent(HelloWorld.class)
public class HelloWorldComponent extends AbstractSugarComponent<HelloWorld, String, Field> {

    @Override
    public String render(HelloWorld hello, String value, Field member) {
    	
    	String heading = ev( hello.heading() );
	
    	return ComponentUtil.embedInPanel(heading, value, Style.STYLE_DANGER);
    }

}