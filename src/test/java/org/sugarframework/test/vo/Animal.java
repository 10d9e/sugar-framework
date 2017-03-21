package org.sugarframework.test.vo;

import java.util.Date;
import java.util.Random;

import org.sugarframework.Hidden;
import org.sugarframework.Label;

public class Animal {

	@Hidden
	private int id;

	@Label("#{animalvo.weight.label}")
	private int weight;

	@Label("Name")
	private String name;

	@Label("Family")
	private String family;

	@Label("Birthdate")
	private Date birthdate;

	@Label("A Long Number")
	private Long longNumber;

	@Label("Antlers?")
	private Boolean hasAntlers = Boolean.FALSE;

	public Animal() {
		super();
		this.setId(new Random().nextInt());
	}

	public Animal(int weight, String name, String family, Date birthdate, Long longNumber, Boolean hasAntlers) {
		super();
		this.setId(new Random().nextInt());
		this.weight = weight;
		this.name = name;
		this.family = family;
		this.birthdate = birthdate;
		this.longNumber = longNumber;
		this.hasAntlers = hasAntlers;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFamily() {
		return family;
	}

	public void setFamily(String family) {
		this.family = family;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public Long getLongNumber() {
		return longNumber;
	}

	public void setLongNumber(Long longNumber) {
		this.longNumber = longNumber;
	}

	public Boolean getHasAntlers() {
		return hasAntlers;
	}

	public void setHasAntlers(Boolean hasAntlers) {
		this.hasAntlers = hasAntlers;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return "AnimalVO [weight=" + weight + ", name=" + name + ", family=" + family + ", birthdate=" + birthdate
				+ ", longNumber=" + longNumber + ", hasAntlers=" + hasAntlers + "]";
	}

}
