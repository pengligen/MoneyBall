package com.moneyball.math;

import java.util.ArrayList;
import java.util.Comparator;
import com.moneyball.crawler.CandleData;
import com.moneyball.crawler.TriCandleData;
import com.moneyball.executor.MoneyBall.TimeInterval;
import com.moneyball.notify.MoneyballLogger;

public class SmartAnalysis {
	
	public static float CLOSE_INCREASE_MAX_1H = 0.1f;
	public static float CLOSE_INCREASE_MAX_2H = 0.1f;
	public static float CLOSE_INCREASE_MAX_4H = 0.1f;
	public static float CLOSE_INCREASE_MAX_6H = 0.1f;
	public static float CLOSE_INCREASE_MAX_12H = 0.1f;
	public static float CLOSE_INCREASE_MAX_1D = 0.2f;
	
    public static float ONE_CANDLE_HIGH_INCREASE_1H = 0.05f;
    public static float ONE_CANDLE_HIGH_INCREASE_2H = 0.05f;
    public static float ONE_CANDLE_HIGH_INCREASE_4H = 0.05f;
    public static float ONE_CANDLE_HIGH_INCREASE_6H = 0.05f;
    public static float ONE_CANDLE_HIGH_INCREASE_12H = 0.1f;
    public static float ONE_CANDLE_HIGH_INCREASE_1D = 0.1f;
    
    public static float ONE_CANDLE_LOW_DECREASE_1H = 0.05f;
    public static float ONE_CANDLE_LOW_DECREASE_2H = 0.05f;
    public static float ONE_CANDLE_LOW_DECREASE_4H = 0.05f;
    public static float ONE_CANDLE_LOW_DECREASE_6H = 0.05f;
    public static float ONE_CANDLE_LOW_DECREASE_12H = 0.1f;
    public static float ONE_CANDLE_LOW_DECREASE_1D = 0.1f;
    
    public static float ONE_CANDLE_IRREGULAR = 0.75f;

	
	private static void sortCandleDataByTimeStamp(ArrayList<CandleData> data) {
		CandleData sampleData = data.get(0);
		// Test whether the data has the timeStamp
		if (sampleData.getTimeStampNumber() > 0) {
			data.sort(new Comparator<CandleData>() {
				@Override
				public int compare(CandleData o1, CandleData o2) {
					long current = o1.getTimeStampNumber();
					long compare = o2.getTimeStampNumber();
					if (current > compare) {
						return 1;
					} else if (current == compare) {
						return 0;
					} else {
						return -1;
					}
				}
			});
		}
	}
	
	public static ArrayList<TriCandleData> analyze1231H(ArrayList<CandleData> data) {
		
		ArrayList<TriCandleData> result = new ArrayList<TriCandleData>();
		if ((data == null) || (data.size() < 4)) {
			return result;
		}
		sortCandleDataByTimeStamp(data);
		int length = data.size();
		int end = length - 3;
		for (int i = 0 ; i <= end; i++) {
			TriCandleData triCandle = new TriCandleData(data.get(i), data.get(i+1), data.get(i+2));
			if (!analyze123VolumePriceLevel1(triCandle)) {
				MoneyballLogger.log("Volume price level1 judge failed");
				continue;
			}
			
			if (!analyze123ClosePrice(triCandle, 0.1F)) {
				MoneyballLogger.log("Close price judge failed");
				continue;
			}
			
			if (!analyze123MaxPrice(triCandle, 0.05F)) {
				MoneyballLogger.log("Max price judge failed");
				continue;
			}
			
			if (!analyze123MinPrice(triCandle, 0.05F)) {
				MoneyballLogger.log("Min price judge failed");
				continue;
			}
			MoneyballLogger.log(triCandle.getDescription());
			result.add(triCandle);
		}
		return result;
	}
	
    public static ArrayList<TriCandleData> analyze123(ArrayList<CandleData> data, TimeInterval timeInterval, boolean strictMode) {
    	
    	// Assign criteria
        float closeIncreaseMax = 0;
        float OneCandleIncreaseMax = 0;
        float OneCandleDecreaseMax = 0;
        
        switch (timeInterval) {
        case H1 :
        	closeIncreaseMax = CLOSE_INCREASE_MAX_1H;
        	OneCandleIncreaseMax = ONE_CANDLE_HIGH_INCREASE_1H;
        	OneCandleDecreaseMax = ONE_CANDLE_LOW_DECREASE_1H;
        	break;
        case H4 :
        	closeIncreaseMax = CLOSE_INCREASE_MAX_4H;
        	OneCandleIncreaseMax = ONE_CANDLE_HIGH_INCREASE_4H;
        	OneCandleDecreaseMax = ONE_CANDLE_LOW_DECREASE_4H;
        	break;
        case H6 :
        	closeIncreaseMax = CLOSE_INCREASE_MAX_6H;
        	OneCandleIncreaseMax = ONE_CANDLE_HIGH_INCREASE_6H;
        	OneCandleDecreaseMax = ONE_CANDLE_LOW_DECREASE_6H;
        	break;
        case H12 :
        	closeIncreaseMax = CLOSE_INCREASE_MAX_12H;
        	OneCandleIncreaseMax = ONE_CANDLE_HIGH_INCREASE_12H;
        	OneCandleDecreaseMax = ONE_CANDLE_LOW_DECREASE_12H;
        	break;
        case D1 :
        	closeIncreaseMax = CLOSE_INCREASE_MAX_1D;
        	OneCandleIncreaseMax = ONE_CANDLE_HIGH_INCREASE_1D;
        	OneCandleDecreaseMax = ONE_CANDLE_LOW_DECREASE_1D;
        	break;
        default :
            break;		
        }
        
        String assignedCriteria = " close increase max " + closeIncreaseMax + " one candle increase max " + OneCandleIncreaseMax + " one candle decrease max " + OneCandleDecreaseMax; 
        MoneyballLogger.log(assignedCriteria);
    	
		ArrayList<TriCandleData> result = new ArrayList<TriCandleData>();
		if ((data == null) || (data.size() < 4)) {
			return result;
		}
		sortCandleDataByTimeStamp(data);
		int length = data.size();
		int end = length - 3;
		for (int i = 0 ; i <= end; i++) {
			TriCandleData triCandle = new TriCandleData(data.get(i), data.get(i+1), data.get(i+2));
			if (!analyze123VolumePriceLevel1(triCandle)) {
				MoneyballLogger.log("Volume price level1 judge failed");
				continue;
			}
			
			if (!analyze123ClosePrice(triCandle, closeIncreaseMax)) {
				MoneyballLogger.log("Close price judge failed");
				continue;
			}
			
			if (!analyze123MaxPrice(triCandle, OneCandleIncreaseMax)) {
				MoneyballLogger.log("Max price judge failed");
				continue;
			}
			
			if (!analyze123MinPrice(triCandle, OneCandleDecreaseMax)) {
				MoneyballLogger.log("Min price judge failed");
				continue;
			}
			
			if (strictMode) {
				if (!check123CandleNormal(triCandle)) {
					MoneyballLogger.log("Candle irregular failed");
					continue;
				}
			}
			MoneyballLogger.log(triCandle.getDescription());
			result.add(triCandle);
		}
		return result;
	}
	
	
	public static ArrayList<TriCandleData> analyze1231D(ArrayList<CandleData> data) {
		if ((data == null) || (data.size() < 4)) {
			return null;
		}
		
		ArrayList<TriCandleData> result = new ArrayList<TriCandleData>();
		
		sortCandleDataByTimeStamp(data);
		
		int length = data.size();
		int end = length - 3;
		for (int i = 0 ; i <= end; i++) {
			TriCandleData triCandle = new TriCandleData(data.get(i), data.get(i+1), data.get(i+2));
			if (!analyze123VolumePriceLevel1(triCandle)) {
				MoneyballLogger.log("Volume price level1 judge failed");
				continue;
			}
			
			if (!analyze123ClosePrice(triCandle, 0.2F)) {
				MoneyballLogger.log("Close price judge failed");
				continue;
			}
			
			if (!analyze123MaxPrice(triCandle, 0.1F)) {
				MoneyballLogger.log("Max price judge failed");
				continue;
			}
			
			if (!analyze123MinPrice(triCandle, 0.1F)) {
				MoneyballLogger.log("Min price judge failed");
				continue;
			}
			MoneyballLogger.log(triCandle.getDescription());
			result.add(triCandle);
		}
		
		return result;
	}
	
	// Check whether the volume and price is getting bigger for 3 candles
	public static boolean analyze123VolumePriceLevel1(TriCandleData triCandle) {
		double volume1 = triCandle.getCandle1().getVolume();
		double volume2 = triCandle.getCandle2().getVolume();
		double volume3 = triCandle.getCandle3().getVolume();
		
		double open1 = triCandle.getCandle1().getOpen();
		double open2 = triCandle.getCandle2().getOpen();
		double open3 = triCandle.getCandle3().getOpen();
		
		double close1 = triCandle.getCandle1().getClose();
		double close2 = triCandle.getCandle2().getClose();
		double close3 = triCandle.getCandle3().getClose();
		
		if ((open1 >= close1) || (open2 >= close2) || (open3 >= close3)) {
			return false;
		}
		
		if ((volume2 <= volume1) || (volume3 <= volume2)) {
			return false;
		}
		
		if ((volume1 <= 5) || (volume2 <= 5) || (volume3 <= 5)) {
			return false;
		}
		
		if ((open2 <= open1) || (open3 <= open2)) {
			return false;
		}
		return true;
	}
	
	public static boolean check123CandleNormal(TriCandleData triCandle) {
		
		double open1 = triCandle.getCandle1().getOpen();
		double open2 = triCandle.getCandle2().getOpen();
		double open3 = triCandle.getCandle3().getOpen();
		
		double close1 = triCandle.getCandle1().getClose();
		double close2 = triCandle.getCandle2().getClose();
		double close3 = triCandle.getCandle3().getClose();
		
		double low1 = triCandle.getCandle1().getMinPrice();
		double low2 = triCandle.getCandle2().getMinPrice();
		double low3 = triCandle.getCandle3().getMinPrice();
		
		double high1 = triCandle.getCandle1().getMaxPrice();
		double high2 = triCandle.getCandle2().getMaxPrice();
		double high3 = triCandle.getCandle3().getMaxPrice();
		
		if ((((high1 - low1) / (close1 - open1)) - 1) > ONE_CANDLE_IRREGULAR) {
			return false;
		}
		
		if ((((high2 - low2) / (close2 - open2)) - 1) > ONE_CANDLE_IRREGULAR) {
			return false;
		}
		
		if ((((high3 - low3) / (close3 - open3)) - 1) > ONE_CANDLE_IRREGULAR) {
			return false;
		}
		
		return true;
	}
	
	// Check close price of each candle, the change in percentage could not be exceeding the increasePercentageMax
	public static boolean analyze123ClosePrice(TriCandleData triCandle, float increasePercentageMax) {
		double close1 = triCandle.getCandle1().getClose();
		double close2 = triCandle.getCandle2().getClose();
		double close3 = triCandle.getCandle3().getClose();
		
		double increasePercentage1 = close2/close1 - 1;
		double increasePercentage2 = close3/close2 - 1;
		
		if ((increasePercentage1 <= increasePercentageMax) && (increasePercentage2 <= increasePercentageMax)) {
			return true;
		} else {
			return false;
		}
	}
	
	// The high price should not be surpassing the close price too much
	public static boolean analyze123MaxPrice(TriCandleData triCandle, float increasePercentageMax) {
		double high1 = triCandle.getCandle1().getMaxPrice();
		double high2 = triCandle.getCandle2().getMaxPrice();
		double high3 = triCandle.getCandle3().getMaxPrice();
		
		double close1 = triCandle.getCandle1().getClose();
		double close2 = triCandle.getCandle2().getClose();
		double close3 = triCandle.getCandle3().getClose();
		
		double percentage1 = high1/close1 - 1;
		double percentage2 = high2/close2 - 1;
		double percentage3 = high3/close3 - 1;
		
		if ((percentage1 <= increasePercentageMax) && (percentage2 <= increasePercentageMax) && (percentage3 <= increasePercentageMax)) {
			return true;
		} else {
			return false;
		}
	}
	
	// The low price should not be too far away from the open price too much
	public static boolean analyze123MinPrice(TriCandleData triCandle, float increasePercentageMax) {
		double low1 = triCandle.getCandle1().getMinPrice();
		double low2 = triCandle.getCandle2().getMinPrice();
		double low3 = triCandle.getCandle3().getMinPrice();
		
		double open1 = triCandle.getCandle1().getOpen();
		double open2 = triCandle.getCandle2().getOpen();
		double open3 = triCandle.getCandle3().getOpen();
		
		double percentage1 = open1/low1 - 1;
		double percentage2 = open2/low2 - 1;
		double percentage3 = open3/low3 - 1;
		
		if ((percentage1 <= increasePercentageMax) && (percentage2 <= increasePercentageMax) && (percentage3 <= increasePercentageMax)) {
			return true;
		} else {
			return false;
		}
	}	
	
	

}
