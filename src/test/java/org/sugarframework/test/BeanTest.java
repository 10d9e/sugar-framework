package org.sugarframework.test;

import java.util.Date;

import org.apache.log4j.Logger;
import org.sugarframework.DefaultValue;
import org.sugarframework.Label;
import org.sugarframework.Link;
import org.sugarframework.MessageOnSuccess;
import org.sugarframework.Order;
import org.sugarframework.Validate;
import org.sugarframework.Validator;
import org.sugarframework.View;
import org.sugarframework.component.common.Action;
import org.sugarframework.component.common.Descriptor;
import org.sugarframework.test.vo.Animal;

@Order(2)
@View(value = "Bean/Default", title="My Test Screen", url="beanscreen.html", icon="glyphicon glyphicon-fire")
public class BeanTest {
	
	static Logger log = Logger.getLogger(
			BeanTest.class.getName());
	
	private volatile String test = "vol";

	public static final String DEFAULT_ZOO_VALUE = "San Francisco Zoo";
	
	private String defaultLocation = "San Francisco";
	
	private int defaultInteger = 42;

	@Order(0) @Descriptor({ "Hello", "Just add some Sugar to your Java web app and enjoy." })
    Void d;
	
	@Link(pageName="Home", containerId="containerId", toolTip="My Tooltip")
	private String myTestLink = "Go Home";
	
	@Order(1) @Action
	@MessageOnSuccess("Mission Complete, Jim")
	public void testBean( 
			@Label("Zoo Name") @DefaultValue("${ me.DEFAULT_ZOO_VALUE }") String zooName,
			@Label("Zoo Location") @DefaultValue("${ me.defaultLocation }") String zooLocation,
			@Label("Int Test") @DefaultValue("${ me.calculateDefaultInteger() }") int intvalue,
			@Label("#{beantest.default.param.title}") @DefaultValue("#{beantest.default.param.value}") String anotherString,
			@Label("Opening Date") Date opening,
			@Label("Postal Code") @Validate("validatePostalCodeFormat") String postalCode,
			@Label("Animal Info") Animal animal){
		System.out.println(animal);
	}
	
	@Validator(value="Invalid Postal Code Format")
	public boolean validatePostalCodeFormat(String code){
	
		return code.matches("^[ABCEGHJKLMNPRSTVXY]{1}\\d{1}[A-Z]{1} *\\d{1}[A-Z]{1}\\d{1}$");
	}
	
	public int calculateDefaultInteger(){
		return 23423;
	}
	
}
