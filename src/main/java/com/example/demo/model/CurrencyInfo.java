package com.example.demo.model;

import java.util.Map;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
public class CurrencyInfo {
	private String name;
	private String code;
	private float rate;
	
	public CurrencyInfo(String name, String code, float rate) {
		this.setName(name);
		this.setCode(code);
		this.setRate(rate);
	}
}