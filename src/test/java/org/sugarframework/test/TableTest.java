package org.sugarframework.test;

import java.util.Collection;

import org.sugarframework.BindReturnData;
import org.sugarframework.Initialize;
import org.sugarframework.Label;
import org.sugarframework.Live;
import org.sugarframework.Order;
import org.sugarframework.View;
import org.sugarframework.component.common.Action;
import org.sugarframework.component.common.Table;
import org.sugarframework.component.common.UnorderedList;
import org.sugarframework.data.Dao;
import org.sugarframework.test.data.AnimalDao;
import org.sugarframework.test.vo.Animal;

@View(value = "Table", url = "table", icon = "glyphicon glyphicon-tasks")
public final class TableTest {
        
    @Dao("data1")
    private AnimalDao dao;
    
    @Initialize
    public void setup(){
    	System.out.println(" *** setup database ***");
    	dao.create();
    }
    
    @Order(2) 
    @Live 
    @Table
    @Label("Animals at the Zoo") 
    @BindReturnData
    public Collection<Animal> getAnimals(){
    	return dao.all();
    }
    
    @Order(3) 
    @Live 
    @UnorderedList 
    @BindReturnData
    public Collection<String> getAnimalNames(){
    	return dao.names();
    }
    
    @Order(0) 
    @Action("Add New Animal")
    public void addTwoNewAnimals(@Label("Animal") Animal animal,
    		@Label("Animal 2") Animal animal2){
    	dao.add(animal);
    	System.out.println("Added " + animal);
    	dao.add(animal2);
    	System.out.println("Added " + animal2);
    }
   

}