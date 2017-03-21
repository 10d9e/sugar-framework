# Sugar Framework
Sugar Framework is a View Inversion framework for the Java Programming Language. It abstracts away the complexity of web request/response lifecycle and web container management with simple annotations.

# What is View Inversion?
In the same way that modern inversion of control containers like Spring and Guice have successfully inverted data and service resource dependencies, View Inversion delegates view generation and data binding via inversion annotations.

# Show me the code
```java
/** Data Inversion with JPA/Hibernate */
@Entity
public class Employee {
  private String name;
}

/** Control/Resource Inversion with Spring/JEE */
@Service("Employee Service")
public class EmployeeService {
  @Inject
  private Employee employee;
}

/** View Inversion with Sugar */
@View(value="Home", title="My Home Page", url="home.html")
public class EmployeeView {
  @Inject
  private EmployeeService service;
  
  // Inverted fields bind Collection data to view components like Tables, Lists, etc 
  @Table("Animals at the Zoo")
  private Collection<Animal> animals = new ArrayList<Animal>();
  
  // Inverted methods are bound to View actions
  @Action("Do Something")
  public void executeSomething(@Label("First Name") String name){
      // do something here...
  }
}
```

# Setup
1. ./gradlew clean eclipse
2. Try out the test application : 'org.sugarframework.test.TestContext.java'
3. Navigate to 'http://localhost:8080/sparkles/index'
