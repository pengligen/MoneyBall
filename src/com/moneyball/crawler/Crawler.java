package com.moneyball.crawler;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.moneyball.network.HttpConnectionBitCoinPrice;

import java.text.SimpleDateFormat;
import redis.clients.jedis.Jedis;

public class Crawler {
	
	// 1491004800000 04/01/2017
	
	public long MILLI_SECONDS_IN_A_DAY = 1 * 1000 * 60 * 60 * 24;
	public long MILLI_SECONDS_IN_HALF_AN_HOUR = 1 * 1000 * 60 * 30;
	
	HttpConnectionBitCoinPrice httpConnectionBitCoinPrice;
	
	Jedis mJedis;
	
	public Crawler() {
		mJedis = new Jedis("127.0.0.1", 6379); 
		httpConnectionBitCoinPrice = new HttpConnectionBitCoinPrice();
	}
	
    public ArrayList<String> cutADay(long timeStampStartingPoint, long interval) {
    	ArrayList<String> result = new ArrayList<String>();
    	for (int i = 0; i < 50; i++) {
    		long currentIter = timeStampStartingPoint + i*interval;
    		result.add(getISO8601StringForDate(new Date(currentIter)));
    	}
    	System.out.println(result);
    	return result;
    }
    
    public static String getISO8601StringForDate(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		return dateFormat.format(date);
		
	}
    
    
    public void startCrawlForFourMonths(long timeStampStartingPoint) {
    	int counter = 0;
    	for (long iter = timeStampStartingPoint; iter < timeStampStartingPoint + 22 * 30 * MILLI_SECONDS_IN_A_DAY; iter = iter + MILLI_SECONDS_IN_A_DAY) {
    		counter++;
    		ArrayList<String> workForADay = cutADay(iter,  MILLI_SECONDS_IN_HALF_AN_HOUR);
        	for (int i = 0; i < workForADay.size() - 1; i++) {
        		String taskStart = workForADay.get(i);
        		String taskEnd = workForADay.get(i + 1);
        		ArrayList<CandleData> response = httpConnectionBitCoinPrice.getHistoricalCandle(taskStart, taskEnd, 10);
        		// Store response into database
        		
        		for (int j = 0; j < response.size(); j++) {
        			CandleData currentCandleData = response.get(j);
        			mJedis.lpush(currentCandleData.mTimeStamp.toString(), currentCandleData.mOpen.toString());
        			mJedis.lpush(currentCandleData.mTimeStamp.toString(), currentCandleData.mClose.toString());
        			mJedis.lpush(currentCandleData.mTimeStamp.toString(), currentCandleData.mHigh.toString());
        			mJedis.lpush(currentCandleData.mTimeStamp.toString(), currentCandleData.mLow.toString());
        			mJedis.lpush(currentCandleData.mTimeStamp.toString(), currentCandleData.mVolume.toString());
        			System.out.println("TimeStamp stored" + currentCandleData.mTimeStamp.toString());
        		}
        		System.out.println("Done for " + taskStart);
        	}
        	if (counter % 40 == 0) {
        		String dbResult = mJedis.bgsave();
        		System.out.println("DB result " + dbResult);
        	}
        }
    	
    	
//    	Thread thread = new Thread(new Runnable() {
//    	    @Override
//    	    public void run() {
//    	    	
//    	        for (long iter = timeStampStartingPoint; iter < timeStampStartingPoint + 4 * 30 * MILLI_SECONDS_IN_A_DAY; iter = iter + MILLI_SECONDS_IN_A_DAY) {
//    	        	ArrayList<String> workForADay = cutADay(timeStampStartingPoint,  MILLI_SECONDS_IN_HALF_AN_HOUR);
//    	        	for (int i = 0; i < workForADay.size() - 1; i++) {
//    	        		String taskStart = workForADay.get(i);
//    	        		String taskEnd = workForADay.get(i + 1);
//    	        		ArrayList<CandleData> response = httpConnectionBitCoinPrice.getHistoricalCandle(taskStart, taskEnd, 10);
//    	        		// Store response into database
//    	        		
//    	        		for (int j = 0; j < response.size(); j++) {
//    	        			CandleData currentCandleData = response.get(j);
//    	        			mJedis.lpush(currentCandleData.mTimeStamp.toString(), currentCandleData.mOpen.toString());
//    	        			mJedis.lpush(currentCandleData.mTimeStamp.toString(), currentCandleData.mClose.toString());
//    	        			mJedis.lpush(currentCandleData.mTimeStamp.toString(), currentCandleData.mHigh.toString());
//    	        			mJedis.lpush(currentCandleData.mTimeStamp.toString(), currentCandleData.mLow.toString());
//    	        			mJedis.lpush(currentCandleData.mTimeStamp.toString(), currentCandleData.mVolume.toString());
//    	        		}
//    	        		System.out.println("Task done for" + taskStart + " TimeStamp : ");
//    	        		String dbResult = mJedis.bgsave();
//    	        		System.out.println("DB result " + dbResult);
//    	        	}
//    	        }
//    	    }
//    	});   
//    	thread.start();
    }
    
    
    public void readDataOut() {
    	//mJedis.keys(pattern);
    }

}
