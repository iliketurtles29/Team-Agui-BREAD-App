package com.oopclass.breadapp.models;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author alvin
 */
@Entity
@Table(name = "instructors")
public class Instructor {
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private long id;
	
	private String firstName;
	
	private String lastName;
	
	private LocalDate dob;
	
	private String gender;
    
    private long number;
    
    private String address;
    
    private String gymClass;

	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}
	
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
    
	public void setAddress(String address) {
		this.address = address;
	}
    
    public String getAddress() {
		return address;
	}
    
    public void setGymClass(String gymClass) {
		this.gymClass = gymClass;
	}
    
	public String getGymClass() {
		return gymClass;
	}

	public long getNumber() {
		return number;
	}

	public void setNumber(long number) {
		this.number = number;
    }

	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", dob=" + dob +  ", address=" + address + ", number=" + number + ", gymClass="+ gymClass +"]";
	}

}
