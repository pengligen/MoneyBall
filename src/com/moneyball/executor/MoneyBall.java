package com.moneyball.executor;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.security.InvalidKeyException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.management.RuntimeErrorException;

import com.moneyball.crawler.Barracuda;
import com.moneyball.crawler.CandleData;
import com.moneyball.crawler.Crawler;
import com.moneyball.crawler.TriCandleData;
import com.moneyball.crawler.WhaleShark;
import com.moneyball.math.SmartAnalysis;
import com.moneyball.math.Utils;
import com.moneyball.monitor.IMonitor.CoinType;
import com.moneyball.monitor.IMonitor.OnNewDataArrivedObserver;
import com.moneyball.monitor.MoneyBallMontior;
import com.moneyball.network.AllSymbols;
import com.moneyball.network.HttpConnectionBitCoinPrice;
import com.moneyball.network.SymbolBTC1DCandles;
import com.moneyball.network.SymbolBTC1HCandles;
import com.moneyball.network.SymbolCandles;
import com.moneyball.notify.Sms;
import com.moneyball.transaction.gdax.GdaxConstants;

import redis.clients.jedis.Jedis;

public class MoneyBall implements OnNewDataArrivedObserver {
	
	public static enum TimeInterval {
		H1,    // 1 point
		H2,    // 2 points
		H4,    // 3 points
		H6,    // 4 points
		H12,   // 5 points
		D1     // 6 points
	}
	
	
	public MoneyBallMontior mMonitor;
	
	public ArrayList<Double> mFiveMinBuffer;
	public ArrayList<Double> mThirtyMinBuffer;
	public ArrayList<Double> mOneHourMinBuffer;
	
	public static ArrayList<String> mHITBTCSymbols;
	
	public static ArrayList<String> mBitfinexSymbols;
	
	public static ArrayList<String> mBinanceSymbols;
	
	public static SymbolBTC1HCandles mCandleCrawler1H = new SymbolBTC1HCandles();
	
	public static SymbolBTC1DCandles mCandleCrawler1D = new SymbolBTC1DCandles();
	
	public static SymbolCandles mCandleCrawler = new SymbolCandles();
	
	
	
	public static boolean isDebug = true;
	
	public static Set resultSet = Collections.synchronizedSet(new HashSet());;

	public static void main(String[] args) {
		
		Integer haha = (Integer) null;
		long start = 1486643560000L;
		//goThroughAllSymbolsHITBTC();
		//goThroughAllSymbolsBITFINEX();
		//new Crawler().startCrawlForFourMonths(start);
		
		TimerTask repeatedTask = new TimerTask() {
	        public void run() {
	        	startBinanceAnalysis();
	        }
	    };
	    Timer timer = new Timer("Timer");
	    long period = Utils.H4_MILLI;
	    timer.scheduleAtFixedRate(repeatedTask, 0, period);
	
	}
	
	public static Hashtable<String, WhaleShark> startBinanceAnalysis() {
		
		
		ArrayList<String> phoneBooks = new ArrayList<String>();
		phoneBooks.add("88888888");
		phoneBooks.add("88888888");
		
		Hashtable<String, WhaleShark> binanceNet = new Hashtable<String, WhaleShark>();
		ArrayList<TimeInterval> allTimeGapsInterested = new ArrayList<TimeInterval>();
		allTimeGapsInterested.add(TimeInterval.H1);
		allTimeGapsInterested.add(TimeInterval.H12);
		allTimeGapsInterested.add(TimeInterval.D1);
		allTimeGapsInterested.add(TimeInterval.H6);
		allTimeGapsInterested.add(TimeInterval.H4);
		allTimeGapsInterested.add(TimeInterval.H2);
		for (TimeInterval iter : allTimeGapsInterested) {
			List<Barracuda> fishList = goThroughAllSymbolBinance(iter);
			for (Barracuda oneFish : fishList) {
				String fishName = oneFish.getSymbol();
				if (Utils.isEmpty(fishName)) {
					continue;
				}
				if (binanceNet.keySet().contains(fishName)) {
					WhaleShark existingWhaleShark = binanceNet.get(fishName);
					existingWhaleShark.addBarracudaForTimeInterval(oneFish, iter);
				} else {
					WhaleShark newWhaleShark = new WhaleShark(fishName);
					newWhaleShark.addBarracudaForTimeInterval(oneFish, iter);
					binanceNet.put(fishName, newWhaleShark);
				}
			}
		}
//		System.out.println("Begin shark report : total sharks : " + binanceNet.size() + " at time : " + new Date());
//		for (String fishName : binanceNet.keySet()) {
//			WhaleShark shark = binanceNet.get(fishName);
//			if (shark.validate()) {
//				System.out.println(shark.getDescription());
//			}
//		}
		
		List<WhaleShark> sharks = new ArrayList<WhaleShark>();
		for (String fishName : binanceNet.keySet()) {
		WhaleShark shark = binanceNet.get(fishName);
		sharks.add(shark);
	    }
		Utils.sortWhaleSharkByTimeStamp(sharks);   
		StringBuffer smsContent = new StringBuffer();
		StringBuffer importantFreshReport = new StringBuffer();
		System.out.println("IMPORTANT!!! Begin fresh shark report :" + " at time : " + new Date());
		importantFreshReport.append("IMPORTANT!!! Begin fresh shark report :" + " at time : " + new Date() + "with total sharks :" + binanceNet.size() + "\n");
		for (WhaleShark whaleShark : sharks) {
			importantFreshReport.append(whaleShark.getDescription() + "\n");
			if (Utils.isTimeLatest(whaleShark.getLatestTimeStamp(), Utils.H6_MILLI)) {
				if (smsContent.length() > 2500) {
					continue;
				} else {
				    smsContent.append("Symbol " + whaleShark.getSymbol() + " Date : " + new Date(whaleShark.getLatestTimeStamp()) + whaleShark.summary() + "\n");
				}
			}
		}
		
//		StringBuffer importantFreshReport = new StringBuffer();
//		System.out.println("IMPORTANT!!! Begin fresh shark report :" + " at time : " + new Date());
//		importantFreshReport.append("IMPORTANT!!! Begin fresh shark report :" + " at time : " + new Date() + "with total sharks :" + binanceNet.size() + "\n");
//		for (String fishName : binanceNet.keySet()) {
//			WhaleShark shark = binanceNet.get(fishName);
//			if (shark.hasDailyFish()) {
//				System.out.println(shark.getDescription());
//				importantFreshReport.append(shark.getDescription() + "\n");
//			}
//		}
		
	
		
		try (PrintStream out = new PrintStream(new FileOutputStream("FreshSharkReport" + new Date() +  ".txt"))) {
		    out.print(importantFreshReport.toString());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(importantFreshReport.toString());
		System.out.println("Short message : " + smsContent.toString());
		// Send short message
		for (String phone : phoneBooks) {
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Sms.sendSms(phone, smsContent.toString());
		}
		System.out.println("IMPORTANT!!! End fresh shark report :" + " at time : " + new Date());
		return binanceNet;
	}
	
	
	public static void goThroughAllSymbolsHITBTC() {
//		mHITBTCSymbols = new AllSymbols().getAllSymbolsHitbtc();
//		System.out.println(mHITBTCSymbols);
//		System.out.println("BEGIN EXAMING" + mHITBTCSymbols.size() + " COINS ");
//		for (int i = 0 ; i < mHITBTCSymbols.size(); i++) {
//		    String symbol = mHITBTCSymbols.get(i);
//		    System.out.println("CURRENT EXAMING" + i + symbol);
//		    Thread thread = new Thread() {
//		        public void run(){
//		        	ArrayList<CandleData> candleData = mCandleCrawler1H.getHistoricalCandleHITBTC(symbol);
//		        	if (SmartAnalysis.analyze123(candleData)) {
//		        		System.out.println("One fish caught " + symbol);
//		        	} else {
//		        		System.out.println("Examined finished. No luck !" + symbol);
//		        	}
//		        }
//		    };
//		    thread.start();
//		    try {
//				thread.sleep(5000);
//			} catch (InterruptedException e) {
//			}
//		}
	}
	
	public static boolean isTimeLatest(TriCandleData triCandle, long timeInterval) {
		long timeToNow = System.currentTimeMillis() - triCandle.getCandle1().getTimeStampNumber();
		if (timeToNow < timeInterval) {
			return true;
		} else {
			return false;
		}
	}
	
	public static void goThroughAllSymbolsBITFINEX(TimeInterval timeInterval) {
		mBitfinexSymbols = new AllSymbols().getAllSymbolsBitfinex();
		int totalSymbolSize = mBitfinexSymbols.size();
		System.out.println(mBitfinexSymbols);
		System.out.println("BEGIN EXAMING" + totalSymbolSize + " COINS " + "at" + new Date() + " For time range " + timeInterval.toString());
		for (int i = 0 ; i < totalSymbolSize; i++) {
		    String symbol = mBitfinexSymbols.get(i);
		    System.out.println("CURRENT EXAMING NO. " + i + " " +  symbol);
		    Thread thread = new Thread() {
		        public void run() {
		        	ArrayList<CandleData> candleData = new ArrayList<CandleData>();
		        	ArrayList<TriCandleData> allTriCandle = new ArrayList<TriCandleData>();
		        	if (timeInterval == TimeInterval.H1) {
		        	    candleData = mCandleCrawler1H.getHistoricalCandleBitfinex(symbol);
		        	    allTriCandle = SmartAnalysis.analyze1231H(candleData);
		        	} else if (timeInterval == TimeInterval.D1) {
		        		candleData = mCandleCrawler1D.getHistoricalCandleBitfinex(symbol);
		        		allTriCandle = SmartAnalysis.analyze1231D(candleData);
		        	}
		        	if ((allTriCandle != null) && (!allTriCandle.isEmpty())) {
		        		for (int i = 0 ; i < allTriCandle.size(); i++) {
		        			TriCandleData iter = allTriCandle.get(i);
		        			if ((System.currentTimeMillis() - iter.getCandle1().getTimeStampNumber()) < Utils.D10_MILLI) {
		        				System.out.println("One fish caught " + symbol);
		        				System.out.println(iter.getDescription());
		        				resultSet.add(symbol);
		        			}
		        		}
		        	} else {
		        		System.out.println("Examined finished. No luck !" + symbol);
		        	}
		        }
		    };
		    thread.start();
		    try {
				thread.sleep(5000);
			} catch (InterruptedException e) {
			}
		}
		System.out.println("*****************" + resultSet + "*********************" );
		System.out.println("***************** size : " + resultSet.size() + "*********************" );
	}
	
	public static List<Barracuda> goThroughAllSymbolBinance(TimeInterval timeInterval) {
		List<Barracuda> harvest = Collections.synchronizedList(new ArrayList<Barracuda>());
		StringBuffer summaryReport = new StringBuffer();
		summaryReport.append("$$$$$$$$$$$" + timeInterval + "$$$$$$$$$$$$$$" + "\n");
		if ((mBinanceSymbols == null) || (mBinanceSymbols.size() == 0)) {
		    mBinanceSymbols = new AllSymbols().getAllSymbolsBinance();
		}
		int totalSymbolSize = mBinanceSymbols.size();
		System.out.println(mBinanceSymbols);
		System.out.println("BEGIN EXAMING" + totalSymbolSize + " COINS " + "at" + new Date() + " For time range " + timeInterval.toString());
		for (int i = 0 ; i < totalSymbolSize; i++) {
		    String symbol = mBinanceSymbols.get(i);
		    System.out.println("CURRENT EXAMING NO." + i + " " +  symbol);
		    Thread thread = new Thread() {
		        public void run(){
		        	ArrayList<CandleData> candleData = new ArrayList<CandleData>();
		        	ArrayList<TriCandleData> allTriCandle = new ArrayList<TriCandleData>();
		        	/******IMPORTANT : Timely detection mode or back testing mode*****/
		        	candleData = mCandleCrawler.getHistoricalCandleBinance(symbol, timeInterval, System.currentTimeMillis());
		        	allTriCandle = SmartAnalysis.analyze123(candleData, timeInterval, false);
		        	
		        	Barracuda fishCaught = new Barracuda();
		        	if ((allTriCandle != null) && (allTriCandle.size() > 0)) {
		        		fishCaught = new Barracuda(symbol, allTriCandle, timeInterval);
		        		System.out.println("LIGEN CAUGHT A FISH!!!!! ");
		        	}
		        	
		        	if (timeInterval == TimeInterval.H1) {
//		        	    if (fishCaught.get3DayFresh()) {
		        	    	System.out.println("One fish caught 3 day fresh for " + timeInterval + fishCaught.getDescription());
		        	    	summaryReport.append(fishCaught.getDescription());
		        	    	resultSet.add(symbol);
		        	    	harvest.add(fishCaught);
//		        	    }
		        	} else {
//		        		if (fishCaught.get10DayFresh()) {
		        	    	System.out.println("One fish caught 10 days fresh for " + timeInterval + fishCaught.getDescription());
		        	    	summaryReport.append(fishCaught.getDescription());
		        	    	resultSet.add(symbol);
		        	    	harvest.add(fishCaught);
//		        	    }
		        	}
		        }
		    };
		    thread.start();
		    try {
				thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
		System.out.println("*****************" + resultSet + "*********************" );
		System.out.println("***************** size : " + resultSet.size() + "out of " + totalSymbolSize + "*********************" );
		System.out.println("Summary report : " + summaryReport.toString());
		return harvest;
	}
	
	
//	public static void goThroughAllSymbolsBITFINEX1H() {
//		mBitfinexSymbols = new AllSymbols().getAllSymbolsBitfinex();
//		System.out.println(mBitfinexSymbols);
//		System.out.println("BEGIN EXAMING" + mBitfinexSymbols.size() + " COINS " + "at" + new Date());
//		for (int i = 0 ; i < mBitfinexSymbols.size(); i++) {
//		    String symbol = mBitfinexSymbols.get(i);
//		    System.out.println("CURRENT EXAMING" + i + symbol);
//		    Thread thread = new Thread() {
//		        public void run(){
//		        	ArrayList<CandleData> candleData = mCandleCrawler1H.getHistoricalCandleBitfinex(symbol);
//		        	ArrayList<TriCandleData> allTriCandle = SmartAnalysis.analyze1231H(candleData);
//		        	if (!allTriCandle.isEmpty()) {
//		        		for (int i = 0 ; i < allTriCandle.size(); i++) {
//		        			TriCandleData iter = allTriCandle.get(i);
//		        			if ((System.currentTimeMillis() - iter.getCandle1().getTimeStampNumber()) <  48 * 3600000) {
//		        				System.out.println("One fish caught " + symbol);
//		        				System.out.println(iter.getDescription());
//		        			}
//		        		}
//		        	} else {
//		        		System.out.println("Examined finished. No luck !" + symbol);
//		        	}
//		        }
//		    };
//		    thread.start();
//		    try {
//				thread.sleep(5000);
//			} catch (InterruptedException e) {
//			}
//		}
//	}
	
//	public static void goThroughAllSymbolsBITFINEX1D() {
//		mBitfinexSymbols = new AllSymbols().getAllSymbolsBitfinex();
//		System.out.println(mBitfinexSymbols);
//		System.out.println("BEGIN EXAMING" + mBitfinexSymbols.size() + " COINS " + "at" + new Date());
//		for (int i = 0 ; i < mBitfinexSymbols.size(); i++) {
//		    String symbol = mBitfinexSymbols.get(i);
//		    System.out.println("CURRENT EXAMING" + i + symbol);
//		    Thread thread = new Thread() {
//		        public void run(){
//		        	ArrayList<CandleData> candleData = mCandleCrawler1D.getHistoricalCandleBitfinex(symbol);
//		        	if (SmartAnalysis.analyze123(candleData)) {
//		        		System.out.println("One fish caught " + symbol);
//		        	} else {
//		        		System.out.println("Examined finished. No luck !" + symbol);
//		        	}
//		        }
//		    };
//		    thread.start();
//		    try {
//				thread.sleep(5000);
//			} catch (InterruptedException e) {
//			}
//		}
//	}
	
	
	public static String generate(String requestPath, String method, String body, String secretKey) {
        try {
        	String timestamp = Instant.now().getEpochSecond() + "";
        	System.out.println(timestamp);
            String prehash = timestamp + method.toUpperCase() + requestPath + body;
            byte[] secretDecoded = Base64.getDecoder().decode(secretKey);
            SecretKeySpec keyspec = new SecretKeySpec(secretDecoded, "HmacSHA256");
            Mac sha256 = (Mac) GdaxConstants.SHARED_MAC.clone();
            sha256.init(keyspec);
            return Base64.getEncoder().encodeToString(sha256.doFinal(prehash.getBytes()));
        } catch (CloneNotSupportedException | InvalidKeyException e) {
            e.printStackTrace();
            throw new RuntimeErrorException(new Error("Cannot set up authentication headers."));
        }
    }
	
	public void start() {
		mMonitor = new MoneyBallMontior();
		mMonitor.init();
		mMonitor.registerObserver(this);
	}

	@Override
	public void onNewDataArrived(CoinType coinType, double price, long timeStamp) {
		// Currently we only deal with bitcoin
		if (coinType == CoinType.BITCOIN) {
			
		}
		
	}
	

}
