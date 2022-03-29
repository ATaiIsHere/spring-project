package com.example.demo.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Currency;
import com.example.demo.model.CurrentPrice;
import com.example.demo.repository.CurrencyRepository;
import com.google.gson.Gson;

@Service
public class CurrencyServiceImpl implements CurrencyService {
	@Autowired
	private CurrencyRepository currencyRepository;

	@Override
	public String getCurrentPrice() throws Exception {
		URL url = new URL("https://api.coindesk.com/v1/bpi/currentprice.json");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		int status = con.getResponseCode();
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer content = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			content.append(inputLine);
		}
		in.close();
		con.disconnect();
		
		return content.toString();
	}

	@Override
	public Map<String, Object> getCurrencyPrice() throws Exception {
		String currentPriceStr = getCurrentPrice();
		Map<String, Object> response = new HashMap<String, Object>();
		
		CurrentPrice currentPrice = new Gson().fromJson(currentPriceStr, CurrentPrice.class);
		
		
		System.out.print(currentPrice.toString());
		String isoString = currentPrice.getTime().getUpdatedISO();
		
		LocalDateTime datetime = LocalDateTime.parse(isoString, DateTimeFormatter.ISO_DATE_TIME);
		String updateTime = datetime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
		response.put("updateTime", updateTime);
		
		
		return response;
	}

	@Override
	public Currency getCurrency(String id) {
		return currencyRepository.findById(id).get();
	}

	@Override
	public Currency saveCurrency(Currency currency) {
		System.out.println(currency.toString());
		return currencyRepository.save(currency);
	}

	@Override
	public Currency updateCurrency(String id, Currency currency) {
		Currency oldCurrency = currencyRepository.findById(id).get();
		oldCurrency.setName(currency.getName());
		return currencyRepository.save(oldCurrency);
	};

	@Override
	public void deleteCurrency(String id) {
		currencyRepository.deleteById(id);
	};
}
