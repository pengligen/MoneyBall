package com.moneyball.crawler;

import java.util.ArrayList;
import java.util.Date;

import com.moneyball.executor.MoneyBall.TimeInterval;
import com.moneyball.math.Utils;

public class Barracuda {
	
	public boolean oneHourCandleGood = false;
	public boolean oneDayCandleGood = false;
	public boolean twoHoursCandleGood = false;
	public boolean fourHoursCandleGood = false;
	public boolean sixHoursCandleGood = false;
	public boolean twelveHoursCandleGood = false;
	
	public boolean oneDayFresh = false;
	public boolean threeDayFresh = false;
	public boolean tenDayFresh = false;
	
	public String mSymbol;
	public ArrayList<TriCandleData> mQualifiedTriCandleSet = new ArrayList<TriCandleData>();
	public TimeInterval mTimeInterval;
	public long mLatestTimeStamp = 0;
	
	public Barracuda(String symbol, ArrayList<TriCandleData> triCandleSet, TimeInterval timeInterval) {
		mSymbol = symbol;
		mQualifiedTriCandleSet = triCandleSet;
		mTimeInterval = timeInterval;
		validateFreshNess();
		stampLatestTimeStamp();
	}
	
	public void stampLatestTimeStamp() {
		mLatestTimeStamp = mQualifiedTriCandleSet.get(mQualifiedTriCandleSet.size() - 1).getCandle3().getTimeStampNumber();
	}
	
	public Barracuda() {
	}
	
	public void setOneHourFlag() {
		oneHourCandleGood = true;
	}
	
	public void setOneDayFlag() {
		oneDayCandleGood = true;
	}
	
	public void setTwoHourFlag() {
		twoHoursCandleGood = true;
	}
	
	public void setFourHourFlag() {
		fourHoursCandleGood = true;
	}
	
	public void setSixHourFlag() {
		sixHoursCandleGood = true;
	}
	
	public void setTwelveHourFlag() {
		twelveHoursCandleGood = true;
	}
	
	public String getSymbol() {
		return mSymbol;
	}
	
	public boolean get1DayFresh() {
		return oneDayFresh;
	}
	
	public boolean get3DayFresh() {
		return threeDayFresh;
	}
	
	public boolean get10DayFresh() {
		return tenDayFresh;
	}
	
	public TimeInterval getTimeInterval() {
		return mTimeInterval;
	}
	
	public ArrayList<TriCandleData> getTriCandleSet() {
		return mQualifiedTriCandleSet;
	}
	
	public boolean isFreshFish(long freshness) {
		if ((mQualifiedTriCandleSet == null) || (mQualifiedTriCandleSet.size() == 0)) {
			return false;
		}
		
		for (int i = 0 ; i < mQualifiedTriCandleSet.size(); i++) {
			TriCandleData triCandle = mQualifiedTriCandleSet.get(i);
			if (Utils.isTimeLatest(triCandle, freshness)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isAOneDayFreshFish() {
		return isFreshFish(Utils.D1_MILLI);
	}
	
	public boolean isAThreeDayFreshFish() {
		return isFreshFish(Utils.D3_MILLI);
	}
	
	public boolean isATenDayFreshFish() {
		return isFreshFish(Utils.D10_MILLI);
	}
    
    public void validateFreshNess() {
    	if (isAOneDayFreshFish()) {
    		oneDayFresh = true;
    	}
    	
    	if (isAThreeDayFreshFish()) {
    		threeDayFresh = true;
    	}
    	
    	if (isATenDayFreshFish()) {
    		tenDayFresh = true;
    	}
    }
    
    public long getLatestTimeStamp() {
    	return mLatestTimeStamp;
    }
    
    public int get123Count() {
    	return mQualifiedTriCandleSet.size();
    }
    
    public String getDescription() {
		StringBuilder result = new StringBuilder();
		result.append("******************" + mSymbol + "******************" + "\n");
		for (int i = 0 ; i < mQualifiedTriCandleSet.size() ; i++) {
			result.append("123Candle " + i + "\n" + mQualifiedTriCandleSet.get(i).getDescription());
		}
		result.append("Latest timestamp : " + new Date(mLatestTimeStamp));
		result.append("******************" + "\n");
		return result.toString();
	}
	

}
