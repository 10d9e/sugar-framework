# Sugar Framework
Sugar Framework is a View Inversion framework for the Java Programming Language.

# What is View Inversion?
In the same way that numerous modern inversion of control containers like Spring and Guice have successfully inverted data and service resource dependencies, View Inversion delegates view generation and data binding to inversion annotations.

# Show me the code
```java
// Data Inversion with JPA
@Entity
public class Employee {
  private String name;
}

// Control/Resource Inversion with Spring/JEE
@Service
public class EmployeeService {
  @Inject
  private Employee employee;
}

// View Inversion with Sugar
@View(value="Home", title="My Home Page", url="home.html")
public class EmployeeView {
  @Inject
  private EmployeeService service;
  
  // Inverted Fields bind Collection data to view components like @Table 
  @Table
	@Label("Animals at the Zoo")
	private Collection<Animal> animals = new ArrayList<Animal>();
  
  // Inverted methods are bound to View actions
  @Action("Do Something")
	public void executeSomething(
			@Label("s1 Label") 
			@DefaultValue("butter") 
			String stringInput ){
      // do something here...
  }

}
```

# Setup
gradlew clean eclipse
