package com.moneyball.monitor;

public class IMonitor {
	
	public enum CoinType {
		BITCOIN,
		LITECOIN,
		ETH
	}
	
	public interface OnNewDataArrivedObserver {
		void onNewDataArrived(CoinType coinType, double price, long timeStamp);
	}

}
