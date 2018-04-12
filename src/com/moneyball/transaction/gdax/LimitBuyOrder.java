package com.moneyball.transaction.gdax;

import java.time.Instant;

//import com.coinbase.exchange.api.exchange.HttpEntity;
//import com.coinbase.exchange.api.exchange.HttpHeaders;

public class LimitBuyOrder implements IOrder {

	@Override
	public boolean place(double limit, double quantity, int expirationDate) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean cancel() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
	
//	
//	@Override
//    public HttpEntity<String> securityHeaders(String endpoint, String method, String jsonBody) {
//        HttpHeaders headers = new HttpHeaders();
//
//        String timestamp = Instant.now().getEpochSecond() + "";
//        String resource = endpoint.replace(getBaseUrl(), "");
//
//        headers.add("accept", "application/json");
//        headers.add("content-type", "application/json");
//        headers.add("CB-ACCESS-KEY", publicKey);
//        headers.add("CB-ACCESS-SIGN", signature.generate(resource, method, jsonBody, timestamp));
//        headers.add("CB-ACCESS-TIMESTAMP", timestamp);
//        headers.add("CB-ACCESS-PASSPHRASE", passphrase);
//
//        curlRequest(method, jsonBody, headers, resource);
//
//        return new HttpEntity<>(jsonBody, headers);
//    }

	
}
