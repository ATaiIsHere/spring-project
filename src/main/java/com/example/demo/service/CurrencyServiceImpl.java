package com.example.demo.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Currency;
import com.example.demo.model.CurrencyInfo;
import com.example.demo.model.CurrencyPrice;
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
	public CurrencyPrice getCurrencyPrice() throws Exception {
		String currentPriceStr = getCurrentPrice();

		// parse data
		CurrentPrice currentPrice = new Gson().fromJson(currentPriceStr, CurrentPrice.class);

		// code:name mapping
		Map<String, String> currencyNameMap = new HashMap<String, String>();
		currencyRepository.findAllById(currentPrice.getBpi().keySet())
				.forEach((currency) -> currencyNameMap.put(currency.getCurrencyId(), currency.getName()));

		// date time format transform
		String isoString = currentPrice.getTime().getUpdatedISO();
		LocalDateTime datetime = LocalDateTime.parse(isoString, DateTimeFormatter.ISO_DATE_TIME);
		String updatedAt = datetime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));

		// currency data transform
		Map<String, CurrencyInfo> currencyInfoMap = currentPrice
				.getBpi().entrySet().stream()
				.map(entry -> new CurrencyInfo(
						currencyNameMap.get(entry.getKey()),
						entry.getKey(), 
						entry.getValue().getRateFloat()
					)
				)
				.collect(Collectors.toMap(CurrencyInfo::getCode, info -> info));

		// combine data
		CurrencyPrice response = CurrencyPrice.builder().updatedAt(updatedAt).currency(currencyInfoMap).build();

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
