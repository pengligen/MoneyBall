package com.moneyball.crawler;

public class CandleData implements Comparable<CandleData> {
	
	protected String mTimeStamp;
	protected long mTimeStampNumber;
	protected Double mLow;
	protected Double mHigh;
	protected Double mOpen;
	protected Double mClose;
	protected Double mVolume;
	
	public CandleData(String timeStamp, double low, double high, double open, double close, double volume) {
		mTimeStamp = timeStamp;
		mLow = low;
		mHigh = high;
		mOpen = open;
		mClose = close;
		mVolume = volume;
	}
	
	public CandleData(long timeStampNumber, String timeStamp, double low, double high, double open, double close, double volume) {
		mTimeStampNumber = timeStampNumber;
		mTimeStamp = timeStamp;
		mLow = low;
		mHigh = high;
		mOpen = open;
		mClose = close;
		mVolume = volume;
	}
	
	public double getVolume() {
		return mVolume;
	}
	
	public double getOpen() {
		return mOpen;
	}
	
	public double getClose() {
		return mClose;
	}
	
	public String getTimeStamp() {
		return mTimeStamp;
	}
	
	public double getMinPrice() {
		return mLow;
	}
	
	public double getMaxPrice() {
		return mHigh;
	}
	
	public long getTimeStampNumber() {
		return mTimeStampNumber;
	}

	@Override
	public int compareTo(CandleData candle) {
		long current = this.getTimeStampNumber();
		long compare = candle.getTimeStampNumber();
		if (current > compare) {
			return 1;
		} else if (current == compare) {
			return 0;
		} else {
			return -1;
		}
	}

}
