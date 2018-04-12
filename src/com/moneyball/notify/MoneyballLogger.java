package com.moneyball.notify;

import java.util.logging.Logger;

import com.moneyball.executor.MoneyBall;

public class MoneyballLogger {
	
	private static final Logger LOGGER = Logger.getLogger("MoneyBall");

	
	public static void log(String content) {
		
		if (MoneyBall.isDebug) {
			System.out.println(content);
		}
	}

}
