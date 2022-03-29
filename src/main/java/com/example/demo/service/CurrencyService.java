package com.example.demo.service;

import java.util.Map;

import org.json.JSONObject;

import com.example.demo.entity.Currency;

public interface CurrencyService {
	public String getCurrentPrice() throws Exception;

	public Map getCurrencyPrice() throws Exception;

	public Currency getCurrency(String id);

	public Currency saveCurrency(Currency currency);

	public Currency updateCurrency(String id, Currency currency);

	public void deleteCurrency(String id);
}
