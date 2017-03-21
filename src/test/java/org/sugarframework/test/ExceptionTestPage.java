package org.sugarframework.test;

import org.sugarframework.ExceptionHandler;
import org.sugarframework.MessageOnException;
import org.sugarframework.MessageOnSuccess;
import org.sugarframework.Order;
import org.sugarframework.View;
import org.sugarframework.component.common.Action;
import org.sugarframework.security.RestrictRole;

@RestrictRole("admin")
@View(value = "Exceptions", url = "exceptions", icon="glyphicon glyphicon-gift")
public class ExceptionTestPage {

    @Order(1) @Action(value = "Test Exception")
    @MessageOnException("There was an error executing [testMessageOnException]")
    public void testMessageOnException() {
    	throw new RuntimeException("Kablowee!");
    }

    @Order(0) @Action("Test Message On Success")
    @MessageOnSuccess("Mission Complete, Jim")
    public void testMessageSuccess() {

    }
    
    @ExceptionHandler
    public void handleException(Exception e) {
	System.out.println("An error occured on the Big Screen page - " + e.getMessage());
    }
}
