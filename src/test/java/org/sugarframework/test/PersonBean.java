package org.sugarframework.test;

import java.util.Date;

public class PersonBean {

	private int age;
	
	private String firstname;
	
	private String lastname;
	
	private Date birthday;
	
	private Boolean female;
	
	public PersonBean(){
		
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@Override
	public String toString() {
		return "PersonBean [age=" + age + ", firstname=" + firstname + ", lastname=" + lastname + ", birthday="
				+ birthday + "]";
	}

	public Boolean getFemale() {
		return female;
	}

	public void setFemale(Boolean female) {
		this.female = female;
	}
	
}
