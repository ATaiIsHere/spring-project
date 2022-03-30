package com.example.demo.model;

import java.util.Map;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class CurrentPrice {
	@SerializedName("time")
	private CurrentTime time;
	
	@SerializedName("bpi")
	Map<String, CurrentCurrency> bpi;
	
	@Data
	public class CurrentTime {
		@SerializedName("updatedISO")
		private String updatedISO;
	}
	
	@Data
	public class CurrentCurrency {
		@SerializedName("code")
		String code;
		@SerializedName("rate_float")
		float rateFloat;
	}
}
