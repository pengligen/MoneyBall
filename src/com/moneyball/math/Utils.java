package com.moneyball.math;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.moneyball.crawler.CandleData;
import com.moneyball.crawler.TriCandleData;
import com.moneyball.crawler.WhaleShark;
import com.moneyball.executor.MoneyBall.TimeInterval;

public class Utils {
	
	public static String BASE_CURRENCY = "baseCurrency";
	public static String OPEN_PRICE = "open";
	public static String CLOSE_PRICE = "close";
	public static String MIN_PRICE = "min";
	public static String MAX_PRICE = "max";
	public static String VOLUME_IN_BTC = "volumeQuote";
	public static String TIMESTAMP = "timestamp";
	public static long H1_MILLI = 3600000;
	public static long H2_MILLI = 2 * H1_MILLI;
	public static long H4_MILLI = 4 * H1_MILLI;
	public static long H6_MILLI = 6 * H1_MILLI;
	public static long H12_MILLI = 12 * H1_MILLI;
	public static long D1_MILLI = 24 * H1_MILLI;
	public static long D3_MILLI = 3 * D1_MILLI;
	public static long D5_MILLI = 5 * D1_MILLI;
	public static long D10_MILLI = 10 * D1_MILLI;
	
	
//	public static ArrayList<CandleData> parseResponse(String content) {
//		ArrayList<CandleData> result = new ArrayList<CandleData>();
//		if (!isEmpty(content)) {
//			try {
//			    JSONArray array = new JSONArray(content);
//			    for (int i = 0; i < array.length(); i++) {
//			        JSONArray iter = array.getJSONArray(i);
//			        result.add(new CandleData(iter.optInt(0), iter.getDouble(1), iter.optDouble(2), iter.optDouble(3), iter.optDouble(4), iter.optDouble(5)));
//			    }
//			} catch (Exception e) {
//			}
//		}
//		return result;		
//	}
	
	public static boolean isEmpty(String s) {
		return (s == null) || (s.length() == 0);
	}
	
	public static ArrayList<JSONObject> getAllJSONObjects(String content) {
		ArrayList<JSONObject> result = new ArrayList<JSONObject>();
		if (!isEmpty(content)) {
			try {
			    JSONArray array = new JSONArray(content);
			    for (int i = 0; i < array.length(); i++) {
			        JSONObject iter = array.getJSONObject(i);
			        result.add(iter);
			    }
			} catch (Exception e) {
			}
		}
		return result;		
	}
	
	public static String parseBaseCurrencyHITBIT(JSONObject jsonObject) {
		String symbol = "";
		if (jsonObject != null) {
			symbol = jsonObject.optString(BASE_CURRENCY);
		}
		return symbol;
	}
	
	public static CandleData parseCandaleDataHITBTC(JSONObject jsonObject) {
		String timeStamp = jsonObject.optString(TIMESTAMP);
		double openPrice = jsonObject.optDouble(OPEN_PRICE);
		double closePrice = jsonObject.optDouble(CLOSE_PRICE);
		double minPrice = jsonObject.optDouble(MIN_PRICE);
		double maxPrice = jsonObject.optDouble(MAX_PRICE);
		double volume = jsonObject.optDouble(VOLUME_IN_BTC);
		return new CandleData(timeStamp, minPrice, maxPrice, openPrice, closePrice, volume);
	}
	
	public static CandleData parseCandaleDataBitfinex(JSONObject jsonObject) {
		String timeStamp = jsonObject.optString(TIMESTAMP);
		double openPrice = jsonObject.optDouble(OPEN_PRICE);
		double closePrice = jsonObject.optDouble(CLOSE_PRICE);
		double minPrice = jsonObject.optDouble(MIN_PRICE);
		double maxPrice = jsonObject.optDouble(MAX_PRICE);
		double volume = jsonObject.optDouble(VOLUME_IN_BTC);
		return new CandleData(timeStamp, minPrice, maxPrice, openPrice, closePrice, volume);
	}
	
	public static boolean isTimeLatest(TriCandleData triCandle, long timeInterval) {
		long timeToNow = System.currentTimeMillis() - triCandle.getCandle1().getTimeStampNumber();
		if (timeToNow < timeInterval) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isTimeLatest(long currentTimeStamp, long timeInterval) {
		long timeToNow = System.currentTimeMillis() - currentTimeStamp;
		if (timeToNow < timeInterval) {
			return true;
		} else {
			return false;
		}
	}
	
	public static void sortWhaleSharkByScore(List<WhaleShark> sharks) {
		if ((sharks != null) && (sharks.size() > 0)) {
			sharks.sort(new Comparator<WhaleShark>() {
				@Override
				public int compare(WhaleShark o1, WhaleShark o2) {
					if (o1.getScore() > o2.getScore()) {
						return -1;
					} else if (o1.getScore() == o2.getScore()) {
						return 0;
					} else {
						return 1;
					}
				}
			});
		}
	}
	
	public static void sortWhaleSharkByTimeStamp(List<WhaleShark> sharks) {
		if ((sharks != null) && (sharks.size() > 0)) {
			sharks.sort(new Comparator<WhaleShark>() {
				@Override
				public int compare(WhaleShark o1, WhaleShark o2) {
					if (o1.getLatestTimeStamp() > o2.getLatestTimeStamp()) {
						return -1;
					} else if (o1.getLatestTimeStamp() == o2.getLatestTimeStamp()) {
						return 0;
					} else {
						return 1;
					}
				}
			});
		}
	}
	
	public static int getScore(TimeInterval timeInterval) {
		switch (timeInterval) {
		case D1:
			return 6;
		case H12:
			return 5;
		case H6:
			return 4;
		case H4:
			return 3;
		case H2:
			return 2;
		case H1:
			return 1;
		default :
			return 0;
		}
	}
			
		
//			data.sort(new Comparator<CandleData>() {
//				@Override
//				public int compare(CandleData o1, CandleData o2) {
//					long current = o1.getTimeStampNumber();
//					long compare = o2.getTimeStampNumber();
//					if (current > compare) {
//						return 1;
//					} else if (current == compare) {
//						return 0;
//					} else {
//						return -1;
//					}
//				}
//			});
//		}
//	}
	
    
}
