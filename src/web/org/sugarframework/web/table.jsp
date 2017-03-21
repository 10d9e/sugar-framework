<% 
// render the table fields if present
					    for(Field field : screenInstance.getClass().getDeclaredFields()){
							field.setAccessible(true);
						    if (field.isAnnotationPresent(Table.class)){
						    	if (field.get(screenInstance) instanceof Collection){
						    		Collection objectCollection = (Collection)field.get(screenInstance);
						    		
						    		if(objectCollection.size() < 1){
						    		    break;
						    		}
						    		// table header first
						    		Object firstObject = objectCollection.toArray()[0];
						    		if (firstObject != null){
							    		Class<?> beanClass = firstObject.getClass();
							    		BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
								    	String beanName = beanInfo.getBeanDescriptor().getDisplayName();
	
							    		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
							    		%><table id="example" class="table table-hover"> 
							    		   <thead>
											   <tr><%
							    		
							    		for(PropertyDescriptor pd : propertyDescriptors){
								    		Class<?> propertyType = pd.getPropertyType();										
											// remove access to the class property
											if ( propertyType.equals(Class.class)){
												continue;
											}
							    		    
								    		String displayName = pd.getDisplayName();
										    
								    		Field f = org.sugarframework.util.BeanUtil.find(beanClass, pd);
										    if (f != null){
										      	Label label = f.getAnnotation(Label.class);
										       	if (label != null){
										       	    displayName = label.value();
										       	}
										    }
									       	
									       	%><th><%=displayName %></th>
									       	
							    	   <%}%> 
							    	 	
							    	   		   </tr>
									      </thead> <%
						    		}
						    		
						    		%> <tbody> <%
						    		
						    		for(Object item : objectCollection){
						    		    
							    		Class<?> beanClass = firstObject.getClass();
							    		BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
								    	String beanName = beanInfo.getBeanDescriptor().getDisplayName();
	
							    		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
							    		
							    		%><tr><%
							    		for(PropertyDescriptor pd : propertyDescriptors){
								    		Class<?> propertyType = pd.getPropertyType();										
											// remove access to the class property
											if ( propertyType.equals(Class.class)){
												continue;
											}
											
											Field f = org.sugarframework.util.BeanUtil.find(beanClass, pd);
											f.setAccessible(true);
											Object value = f.get(item);
											if (value == null){
											    value = new String();
											}
							    		%>
											          <td><%=value %></td>
											       
										<%
							    		}
							    		%>
							    		
							    		</tr><%
						    		}
						    		
						    		%> </tbody>
									</table> <%
						    	}
						    }
						}
					    // end table
%> <br>    