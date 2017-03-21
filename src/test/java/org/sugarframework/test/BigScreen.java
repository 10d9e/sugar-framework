package org.sugarframework.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.sugarframework.Choice;
import org.sugarframework.DefaultValue;
import org.sugarframework.ExceptionHandler;
import org.sugarframework.Label;
import org.sugarframework.MessageOnSuccess;
import org.sugarframework.Order;
import org.sugarframework.TextArea;
import org.sugarframework.Validate;
import org.sugarframework.Validator;
import org.sugarframework.View;
import org.sugarframework.component.common.Action;
import org.sugarframework.component.common.UnorderedList;

@Order(3)
@View(value = "Big", title = "My Test Screen", url = "big.html", icon="glyphicon glyphicon-leaf")
public class BigScreen {
    
    @Order(1) @UnorderedList 
    private List<String> items = new ArrayList<String>();
    {
		items.add("Cras justo odio");
		items.add("Dapibus ac facilisis in");
		items.add("Morbi leo risus");
		items.add("Porta ac consectetur ac");
		items.add("Vestibulum at eros");
    }
	
	@Order(0) @Action("Test Large Form")
	@MessageOnSuccess("Added a big record!")
	public void testBig( 
			@Label("Favorite Drink") 
			@DefaultValue("wine") 
			@Validate("checkDrink") 
			String favoriteDrink, 
			
			@Label("Integer One Label") 
			@DefaultValue("42") 
			int integerOne,
			
			@Label("A Double Value") 
			@DefaultValue("32423.943") 
			Double doubleVal,
			
			@Label("My Validated Number") 
			@Validate("isLessThanFive")
			int myNumber,
			
			@Label("Date of Birth") 
			Date birthday,
			
			@Label("Potty Trained?") 
			Boolean bValue, 
			
			@Label("Choose a breed") 
			@Choice({"Hound", "Terrior", "Pit Bull"}) 
			String dogType,
			
			@Label("Description")
			@DefaultValue("Yo doge, I heard you like doge, so here's some doge")
			@TextArea
			String description,
			
			@Label("Dog Owner Information") 
			PersonBean person){
		
			System.out.println(person);
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
		System.out.println("An error occured on the Big Screen page - " + e.getMessage() );
	}
}
