package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Currency;
import com.example.demo.model.CurrencyPrice;
import com.example.demo.service.CurrencyServiceImpl;

@RestController
public class CurrencyController {
    @Autowired 
    private CurrencyServiceImpl currencyService;
    
    @GetMapping("/")
	public String hello(){
		return "Hello!";
	}
    
    @GetMapping("/currentPrice")
	public Map<String, Object> getCurrentPrice() {
    	try {
    		return new JSONObject(currencyService.getCurrentPrice()).toMap();
    	} catch(Exception e) {
    		HashMap<String, Object> response = new HashMap<String, Object>();
    		response.put("error", "Internal Error");
    		return response;
    	}
	}
    
    @GetMapping("/currencyPrice")
	public Object getCurrencyPrice() {
    	try {
    		return currencyService.getCurrencyPrice();
    	} catch(Exception e) {
    		e.printStackTrace();
    		
    		HashMap<String, Object> response = new HashMap<String, Object>();
    		response.put("error", "Internal Error");
    		return response;
    	}
	}

	@PostMapping("/currency")
	public Currency saveCurrency(@RequestBody Currency currency){
		return currencyService.saveCurrency(currency);
	}
	
	// Read operation
    @GetMapping("/currency/{id}")
    public Currency getCurrency(@PathVariable String id)
    {
        return currencyService.getCurrency(id);
    }
 
    // Update operation
    @PutMapping("/currency/{id}")
    public Currency updateCurrency(@RequestBody Currency currency, @PathVariable("id") String currencyId)
    {
        return currencyService.updateCurrency(currencyId, currency);
    }
 
    // Delete operation
    @DeleteMapping("/currency/{id}")
    public String deleteCurrencyById(@PathVariable("id") String currencyId)
    {
    	currencyService.deleteCurrency(currencyId);
        return "Deleted Successfully";
    }
}
