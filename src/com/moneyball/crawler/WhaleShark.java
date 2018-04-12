package com.moneyball.crawler;

import java.util.Date;
import java.util.Hashtable;
import com.moneyball.executor.MoneyBall.TimeInterval;
import com.moneyball.math.Utils;

public class WhaleShark implements Comparable<WhaleShark> {
	
	public boolean oneHourCandleGood = false;
	public boolean oneDayCandleGood = false;
	public boolean twoHoursCandleGood = false;
	public boolean fourHoursCandleGood = false;
	public boolean sixHoursCandleGood = false;
	public boolean twelveHoursCandleGood = false;
	
	public boolean oneDayFresh = false;
	public boolean threeDayFresh = false;
	public boolean tenDayFresh = false;
	
	public long mlatestTimeStamp = 0;
	
    public int mScore = 0;
	
	public String mSymbol;
    public Hashtable<TimeInterval, Barracuda> mFishes = new Hashtable<TimeInterval, Barracuda>();
    
    public WhaleShark(String symbol) {
    	mSymbol = symbol;
    }
    
    public long getLatestTimeStamp() {
    	return mlatestTimeStamp;
    }
    
    public void addBarracudaForTimeInterval(Barracuda fish, TimeInterval timeInterval) {
    	if ((fish != null) && (fish.getTriCandleSet().size() > 0)) {
    		mScore = mScore + Utils.getScore(fish.getTimeInterval());
    		mFishes.put(timeInterval, fish);
    		if (fish.getLatestTimeStamp() > mlatestTimeStamp) {
    			mlatestTimeStamp = fish.getLatestTimeStamp();
    		}
    	}
    }
    
    public String getDescription() {
    	StringBuilder result = new StringBuilder();
    	result.append("$$$$" +  " Whaleshark " + mSymbol + "$$$$START" + "\n");
    	result.append("Summary START :" + "\n" + summary() + "Summary END:" + "\n");
		for (TimeInterval iter : mFishes.keySet()) {
			Barracuda fish = mFishes.get(iter);
			result.append(iter.toString() + ":::" + fish.getDescription() + "\n");
		}
		result.append("$$$$" +  " Whaleshark " + mSymbol + "$$$$END" + "\n");
		return result.toString();
    }
    
    public String summary() {
    	StringBuilder result = new StringBuilder();
    	for (TimeInterval iter : mFishes.keySet()) {
			result.append(iter.toString() + " latest 123 timestamp : " + new Date(mFishes.get(iter).getLatestTimeStamp()));
			result.append(" 123 appear times : " + mFishes.get(iter).getTriCandleSet().size() + "\n");
			result.append(" Score " + mScore + "\n");
		}
    	return result.toString();
    }
    
    public boolean validate() {
    	return true;
    }
    
    public boolean isReallyFresh() {
    	long timeInterval = System.currentTimeMillis() - mlatestTimeStamp;
    	if (timeInterval < Utils.D1_MILLI) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public boolean hasDailyFish() {
    	Barracuda fish = mFishes.get(TimeInterval.D1);
    	if ((fish != null) && (fish.getTriCandleSet().size() > 0) && (Utils.isTimeLatest(fish.getLatestTimeStamp(), Utils.D1_MILLI))) {
    		return true;
    	}
    	return false;
    }
    
    public int getScore() {
    	return mScore;
    }
    
    public String getSymbol() {
    	return mSymbol;
    }
    

	@Override
	public int compareTo(WhaleShark o) {
		if (o.getScore() > this.getScore()) {
			return -1;
		} else if ((o.getScore() == this.getScore())) {
			return 0;
		} else {
			return 1;
		}
	}

}
