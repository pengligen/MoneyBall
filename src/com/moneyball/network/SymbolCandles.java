package com.moneyball.network;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;

import com.moneyball.crawler.CandleData;
import com.moneyball.executor.MoneyBall;
import com.moneyball.executor.MoneyBall.TimeInterval;
import com.moneyball.math.Utils;

public class SymbolCandles {
	
	public ArrayList<CandleData> getHistoricalCandleBinance(String symbol, TimeInterval timeInterval, long end) {
        String response = "";
        HttpsURLConnection con = null;
        Scanner inStream = null;
        OutputStream os = null;
        BufferedWriter writer = null;
        int statusCode = 0;
        ArrayList<CandleData> returnResult = new ArrayList<CandleData>();

        try {
        	long endTime = end;
        	long startTime = endTime - Utils.D5_MILLI;
        	String interval = "";
        	switch (timeInterval) {
        	case H1:
        		interval= "1h";
        		break;
        	case H2:
        		interval= "2h";
        		break;
        	case H4:
        		interval= "4h";
        		break;
        	case H6:
        		interval= "6h";
        		break;
        	case H12:
        		interval= "12h";
        		break;
        	case D1:
        		interval= "1d";
        		break;
        	default:
        		interval= "1d";
        		break;
        	}
        	StringBuilder constructedUrl = new StringBuilder();
        	constructedUrl.append("https://api.binance.com/api/v1/klines?symbol=" + symbol + "&limit=500&interval=" + interval + "&startTime=" + startTime + "&endTime=" + endTime);
        	System.out.println("Constructed url" + constructedUrl);
            URL getUrl = new URL(constructedUrl.toString());
            con = (HttpsURLConnection) getUrl.openConnection();
            con.setDoInput(true);
            con.setReadTimeout(15000);
            con.setRequestMethod("GET");
            con.connect();

            statusCode = con.getResponseCode();
            StringBuilder result = new StringBuilder();

            if (statusCode == 200) {
                inStream = new Scanner(con.getInputStream());
            } else {
                inStream = new Scanner(con.getErrorStream());
            }
            while (inStream.hasNextLine()) {
                result.append(inStream.nextLine());
            }
            // TODO : We might remove response in the future since we don't need this
            response = result.toString();
        } catch (Exception e) {
            // TODO : Ignore here
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e) {
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (Exception e) {
                }
            }
            if (con != null) {
                con.disconnect();
            }
            if (inStream != null) {
                inStream.close();
            }
        }

		if (!Utils.isEmpty(response)) {
			try {
			    JSONArray array = new JSONArray(response);
			    for (int i = 0; i < array.length(); i++) {
			        JSONArray iter = array.getJSONArray(i);
			        long timeStamp = iter.optLong(0);
			        double open = iter.optDouble(1);
			        double high = iter.optDouble(2);
			        double low = iter.optDouble(3);
			        double close = iter.optDouble(4);
			        double volume = iter.optDouble(7);
			        CandleData oneCandle = new CandleData(timeStamp, "", low, high, open, close, volume);
			        returnResult.add(oneCandle);
			    }
			} catch (Exception e) {
				if (MoneyBall.isDebug) {
					System.out.println("Error in getting binance data" + e);
				}
			}
		}
        
        return returnResult;
	}

}
