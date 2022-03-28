package com.example.demo.service;

import com.example.demo.entity.Currency;

public interface CurrencyService {
	public void getCurrentPrice();
	public void getCurrencyPrice();
	public Currency getCurrency(String id);
	public Currency saveCurrency(String id, String name);
	public Currency updateCurrency(String id, String name);
	public Currency deleteCurrency(String id);
}
