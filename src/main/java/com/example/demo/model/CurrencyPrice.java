package com.example.demo.model;

import java.util.Map;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CurrencyPrice {
	private String updatedAt;
	private Map<String, CurrencyInfo> currency;
}
