package com.moneyball.crawler;

import java.util.Date;

public class TriCandleData {
	
	public CandleData mCandle1;
	public CandleData mCandle2;
	public CandleData mCandle3;
	
	public TriCandleData(CandleData candle1, CandleData candle2, CandleData candle3) {
		mCandle1 = candle1;
		mCandle2 = candle2;
		mCandle3 = candle3;
	}
	
	public String getDescription() {
		Date date1 = new Date(mCandle1.getTimeStampNumber());
		Date date2 = new Date(mCandle2.getTimeStampNumber());
		Date date3 = new Date(mCandle3.getTimeStampNumber());
		
		double volume1 = mCandle1.getVolume();
		double volume2 = mCandle2.getVolume();
		double volume3 = mCandle3.getVolume();
		
		double open1 = mCandle1.getOpen();
		double open2 = mCandle2.getOpen();
		double open3 = mCandle3.getOpen();
		
		double close1 = mCandle1.getClose();
		double close2 = mCandle2.getClose();
		double close3 = mCandle3.getClose();
		
		StringBuilder result = new StringBuilder();
		result.append("******************" + "\n");
		result.append("Date 1 : " + date1 + "  Volume1 : " + volume1 + " Open1 : " + open1 + " Close1 : " + close1 + "\n");
		result.append("Date 2 : " + date2 + "  Volume2 : " + volume2 + " Open2 : " + open2 + " Close2 : " + close2 + "\n");
		result.append("Date 3 : " + date3 + "  Volume3 : " + volume3 + " Open3 : " + open3 + " Close3 : " + close3 + "\n");
		result.append("******************" + "\n");
		return result.toString();
	}
	
	public CandleData getCandle1() {
		return mCandle1;
	}
	
	public CandleData getCandle2() {
		return mCandle2;
	}
	
	public CandleData getCandle3() {
		return mCandle3;
	}

}
