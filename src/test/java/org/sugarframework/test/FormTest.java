package org.sugarframework.test;


import java.util.Date;

import org.apache.log4j.Logger;
import org.sugarframework.DefaultValue;
import org.sugarframework.Label;
import org.sugarframework.Link;
import org.sugarframework.Order;
import org.sugarframework.Validate;
import org.sugarframework.View;
import org.sugarframework.component.common.Action;
import org.sugarframework.component.common.Descriptor;
import org.sugarframework.test.vo.Animal;

@View(value="form", url="form")
public class FormTest {
	
	static Logger log = Logger.getLogger(
			BeanTest.class.getName());
	
	private volatile String test = "vol";

	private static final String DEFAULT_ZOO_VALUE = "San Francisco Zoo";
	
	private String defaultLocation = "San Francisco";
	
	private int defaultInteger = 42;

	@Descriptor({ "Hello", "Just add some Sugar to your Java web app and enjoy." })
	@Order(1)
    Void d;

	@Link(pageName="Home", containerId="containerId", toolTip="My Tooltip") 
	private String myTestLink = "Go Home";
	
	@Action("Lets test") @Order(3)
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
	
	@Action("Lets test2") @Order(2)
	public void testBean2( 
			@Label("Zoo Name") @DefaultValue("${ me.DEFAULT_ZOO_VALUE }") String zooName,
			@Label("Zoo Location") @DefaultValue("${ me.defaultLocation }") String zooLocation,
			@Label("Int Test") @DefaultValue("${ me.calculateDefaultInteger() }") int intvalue,
			@Label("#{beantest.default.param.title}") @DefaultValue("#{beantest.default.param.value}") String anotherString,
			@Label("Opening Date") Date opening,
			@Label("Postal Code") @Validate("validatePostalCodeFormat") String postalCode,
			@Label("Animal Info") Animal animal){
		System.out.println(animal);
	}
	

	public int calculateDefaultInteger(){
		return 23423;
	}
}
