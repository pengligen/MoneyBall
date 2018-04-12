package com.moneyball.transaction.gdax;

public interface IOrder {
	
	
	// Return value : true means the order has been successfully placed
	//                false means that the order has not been successfully placed
	public boolean place(double limit, double quantity, int expirationDate);
	
	public boolean cancel();

}
