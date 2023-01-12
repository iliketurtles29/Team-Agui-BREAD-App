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
@Table(name = "report")
public class Report {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
    
	private long id;
	
	private String firstName;
	
	private String lastName;
	
	private String status;
    
    private long number;
    
    private String reason;
    
    private LocalDate creation;
    

	
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
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
    
	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getReason() {
		return reason;
	}
    
    public void setNumber(long number) {
		this.number = number;
	}

	public long getNumber() {
		return number;
	}
    
    public LocalDate getCreation() {
		return creation;
	}

	public void setCreation(LocalDate creation) {
		this.creation= creation;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName +  ", reason=" + reason + ", number=" + number + ", creation=" + creation + ", status=" + status + "]";
	}

	
}
