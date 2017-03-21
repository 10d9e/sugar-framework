package org.sugarframework.test;

import java.util.Date;

import org.sugarframework.Label;
import org.sugarframework.MessageOnSuccess;
import org.sugarframework.Order;
import org.sugarframework.View;
import org.sugarframework.component.common.Action;

@View(value = "Demonstration", url = "demo")
public class Demo {

	@Order(0) @Action
	@MessageOnSuccess("Nice job")
	public void executeMethod(@Label("First") String one,
			@Label("Second") Integer two, @Label("My Date") Date three, 
			@Label("May the fourth be with you") int four) {
		
		System.out.println(String.format("%s %s on %s", one, two, three));
	}

	@Order(1) @Action
	@MessageOnSuccess("Nice job doing something!")
	public void doSomething() {

	}

}