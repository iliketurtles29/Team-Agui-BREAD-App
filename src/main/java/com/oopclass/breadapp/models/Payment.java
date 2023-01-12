package com.oopclass.breadapp.models;
import java.time.LocalDate;
import javafx.scene.control.ComboBox;

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
@Table(name = "payment")
public class Payment {
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private long id;
	
	private Double amount;
	
	private String trainee;
	
	private LocalDate paydate;
    
    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
    
    public LocalDate getPaydate() {
		return paydate;
	}

	public void setPaydate(LocalDate paydate) {
		this.paydate = paydate;
	}
    
     public void setAmount(Double amount) {
		this.amount = amount;
	}

	public double getAmount() {
		return amount;
	}
    
    public String getTrainee() {
		return trainee;
	}

	public void setTrainee(String trainee) {
		this.trainee = trainee;
	}
    	@Override
	public String toString() {
		return "User [id=" + id + ", amount=" + amount + ", trainee=" + trainee + ", paydate=" + paydate + "]";
	}
}
