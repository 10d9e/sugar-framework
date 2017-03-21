package org.sugarframework.test;

import java.util.Date;

import org.sugarframework.Choice;
import org.sugarframework.Label;
import org.sugarframework.MessageOnSuccess;
import org.sugarframework.RedirectOnSuccess;
import org.sugarframework.View;
import org.sugarframework.component.common.Action;
import org.sugarframework.security.RestrictRole;
import org.sugarframework.Order;

@RestrictRole("admin")
@View(value = "Redirect Test", title="My Test Screen", url="screen3.html", icon="glyphicon glyphicon-globe")
public class RedirectTest {

	@Order(2) @Action
	@RedirectOnSuccess( "Home")
	public void redirectTest(String strParameter, int hello, double yo, boolean off){
		System.out.println ( String.format( "%s %s %s %s", strParameter, hello, yo, off ) );
	}
	
	@Order(4) @Action
	@RedirectOnSuccess( "Long Running Test")
	public void walkTheDog( @Label("Choose a breed") @Choice({"Hound", "Terrior", "Pit Bull"}) String dogType){
		System.out.println("You chose " + dogType);
	}
	
	@Order(3) @Action
	@MessageOnSuccess("Mission Complete, Jim")
	@RedirectOnSuccess( "Long Running Test")
	public void testRedirectAndMessage( @Label("Choose a breed") @Choice({"Hound", "Terrior", "Pit Bull"}) String dogType){
		System.out.println("You chose " + dogType);
	}
	
	@Order(1) @Action
	@RedirectOnSuccess( "Home")
	public void executeLongAndRedirect(@Label("Name") String string, @Label("Birthday") Date date) throws InterruptedException{
		
		System.out.println("Start");
		Thread.sleep(5000);
		System.out.println("Done");
		
	}
	
	@Order(0) @Action
	@MessageOnSuccess("Aaaaand, we're done.")
	public void executeLongTime() throws Exception{

		System.out.println("Start");
		Thread.sleep(5000);
		System.out.println("Done");
	}
}
