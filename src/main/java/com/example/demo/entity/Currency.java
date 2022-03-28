package com.example.demo.entity;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Currency {
	@Id
	private String CurrencyId;
	private String name;
}
