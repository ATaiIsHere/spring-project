package com.example.demo.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Answers.CALLS_REAL_METHODS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.example.demo.entity.Currency;
import com.example.demo.repository.CurrencyRepository;
import com.example.demo.service.CurrencyServiceImpl;
import com.google.gson.Gson;

@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class TestCurrencyController {
	@Autowired
	private MockMvc mvc;
	
	@SpyBean
	private CurrencyServiceImpl currencyService;
	
	static private Currency currency = Currency.builder().currencyId("TWD").name("台幣").build();
	static private Currency newCurrency = Currency.builder().currencyId("TWD").name("新台幣").build();
	static private String currencyJson = new Gson().toJson(currency);
	static private String newCurrencyJson = new Gson().toJson(newCurrency);
	static private String currentPriceJson = "{ \"time\": { \"updated\": \"Mar 28, 2022 10:57:00 UTC\", \"updatedISO\": \"2022-03-28T10:57:00+00:00\", \"updateduk\": \"Mar 28, 2022 at 11:57 BST\" }, \"disclaimer\": \"This data was produced from the CoinDesk Bitcoin Price Index (USD). Non-USD currency data converted using hourly conversion rate from openexchangerates.org\", \"chartName\": \"Bitcoin\", \"bpi\": { \"USD\": { \"code\": \"USD\", \"symbol\": \"&#36;\", \"rate\": \"47,204.3083\", \"description\": \"United States Dollar\", \"rate_float\": 47204.3083 }, \"GBP\": { \"code\": \"GBP\", \"symbol\": \"&pound;\", \"rate\": \"35,947.5441\", \"description\": \"British Pound Sterling\", \"rate_float\": 35947.5441 }, \"EUR\": { \"code\": \"EUR\", \"symbol\": \"&euro;\", \"rate\": \"42,992.8816\", \"description\": \"Euro\", \"rate_float\": 42992.8816 } } }";

	@Test
	void test_hello_ok() throws Exception {
		mvc.perform(get("/"))
		.andExpect(status().isOk())
		.andExpect(content().string("Hello!"));
	}
	
	@Test
	void test_getCurrentPrice_ok() throws Exception {
		mvc.perform(get("/currentPrice"))
		.andExpect(status().isOk());
	}
	
	@Test
	void test_getCurrencyPrice_ok() throws Exception {
		when(currencyService.getCurrentPrice()).thenReturn(currentPriceJson);
		
		mvc.perform(get("/currencyPrice"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.updatedAt").value("2022/03/28 10:57:00"))
		.andExpect(jsonPath("$.currency.EUR.code").value("EUR"))
		.andExpect(jsonPath("$.currency.USD.code").value("USD"))
		.andExpect(jsonPath("$.currency.GBP.code").value("GBP"))
		.andDo(MockMvcResultHandlers.print());
		
	}
	
	@Test
	void test_saveCurrency_ok() throws Exception {
		mvc.perform(
			post("/currency")
			.contentType(MediaType.APPLICATION_JSON)
			.content(currencyJson)
		)
		.andExpect(status().isOk())
		.andExpect(content().json(currencyJson));
	}
	
	@Test
	void test_getCurrency_ok() throws Exception {
		test_saveCurrency_ok();
		mvc.perform(get("/currency/{id}", "TWD"))
		.andExpect(status().isOk())
		.andExpect(content().json(currencyJson));
	}
	
	@Test
	void test_updateCurrency_ok() throws Exception {
		test_saveCurrency_ok();
		mvc.perform(
			put("/currency/{id}", "TWD")
			.contentType(MediaType.APPLICATION_JSON)
			.content(newCurrencyJson)
		)
		.andExpect(status().isOk())
		.andExpect(content().json(newCurrencyJson))
		.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	void test_deleteCurrency_ok() throws Exception {
		mvc.perform(delete("/currency/{id}", "TWD"))
		.andExpect(status().isOk())
		.andExpect(content().string("Deleted Successfully"));
	}
}
