package org.sugarframework.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.sugarframework.DefaultValue;
import org.sugarframework.ExceptionHandler;
import org.sugarframework.Label;
import org.sugarframework.Order;
import org.sugarframework.Validate;
import org.sugarframework.ValidateNotEmpty;
import org.sugarframework.Validator;
import org.sugarframework.View;
import org.sugarframework.component.common.Action;
import org.sugarframework.component.common.Table;
import org.sugarframework.test.vo.Animal;

@Order(1)
@View(value="Home", title="My Home Page", url="home.html", icon="glyphicon glyphicon-bullhorn")
public class HomePage {

	private String value;
	
	@Order(2) @Table
	@Label("Animals at the Zoo")
	private Collection<Animal> animals = new ArrayList<Animal>();
	{
	    animals.add( new Animal(2123, "Cha cha", "Canine", new Date(), 67l, false) );
	}
		
	@Order(0) @Action("Do Something")
	public void doSomething(
			@Label("s1 Label") 
			@DefaultValue("default s1 String") 
			String s1, 
			
			@Label("Integer One Label") 
			@DefaultValue("42") 
			int integerOne,
			
			@Label("A Double Value") 
			@DefaultValue("32423.943") 
			@ValidateNotEmpty 
			Double doubleVal,
			
			@Label("My Validated Number") 
			@DefaultValue("345") 
			@Validate("isLessThanFive") 
			int myNumber
			
			) {
		
		//System.out.println(value);
		
		value = s1;
		
		System.out.printf("Do Something called %s %s %s\n", s1, integerOne, doubleVal);
		
	}
	
	@Order(1) @Action("Do Something Else")
	public void doSomethingElse(@Label("Hello Label") @Validate("isLessThanFive") int hello, 
			@Label("Favorite Drink") @Validate("checkDrink") String drink) {
		System.out.println("Do Something Else called");
	}
	
	@Validator(invalidMessage="Value is NOT less than five")
	public boolean isLessThanFive(int value){
		return value < 5;
	}
	
	@Validator(invalidMessage="Value is NOT beer")
	public boolean checkDrink(String value){
		return value.equals("beer");
	}
	

	@ExceptionHandler
	public void handleException(Exception e){
		System.out.println("An error occured on the HOME page - " + e.getMessage() );
	}

}
