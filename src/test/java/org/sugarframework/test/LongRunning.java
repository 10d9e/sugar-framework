package org.sugarframework.test;

import org.sugarframework.Label;
import org.sugarframework.Order;
import org.sugarframework.component.common.Action;

@Order(5)
//@Page(value="Long", title="Long Running", url="long.html", icon="glyphicon glyphicon-thumbs-up")
public class LongRunning {

	@Action("execute")
	public void executeLong(@Label("Name") String string) throws InterruptedException{
		
		System.out.println("Start");
		Thread.sleep(5000);
		System.out.println("Done");
		
	}
	
}
