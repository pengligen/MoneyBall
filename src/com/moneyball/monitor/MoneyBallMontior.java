package com.moneyball.monitor;

import java.util.ArrayList;

import com.moneyball.monitor.IMonitor.OnNewDataArrivedObserver;

public class MoneyBallMontior {

	public ArrayList<OnNewDataArrivedObserver> mObservers; 
	
	
	public void start() {
		// Create timer and begin firing request to get data
	}
	
	public void registerObserver(OnNewDataArrivedObserver observer) {
		mObservers.add(observer);
	}
	
	public void init() {
		mObservers = new ArrayList<OnNewDataArrivedObserver>();
	}
}
