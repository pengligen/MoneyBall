package com.moneyball.network;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import com.moneyball.crawler.CandleData;
import com.moneyball.executor.MoneyBall;
import com.moneyball.math.Utils;

public class SymbolBTC1DCandles {
	
    public String TAG = "SymbolBTC1DCandles";
	
	public ArrayList<CandleData> getHistoricalCandleHITBTC(String symbol) {
        String response = "";
        HttpsURLConnection con = null;
        Scanner inStream = null;
        OutputStream os = null;
        BufferedWriter writer = null;
        int statusCode = 0;
        ArrayList<CandleData> returnResult = new ArrayList<CandleData>();

        try {
        	StringBuilder constructedUrl = new StringBuilder();
        	constructedUrl.append("https://api.hitbtc.com/api/2/public/candles/");
        	if ((!Utils.isEmpty(symbol))){
        		constructedUrl.append(symbol + "BTC?period=D1");
        	}
        	//System.out.println("Constructed url" + constructedUrl);
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
        ArrayList<JSONObject> result = Utils.getAllJSONObjects(response);
        if ((result == null) || (result.size() == 0)) {
        	System.out.println(TAG + "Failed to retrieve candle data");
        	return null;
        }
        
        for (int i = 0; i < result.size(); i++) {
        	CandleData oneCandle = Utils.parseCandaleDataHITBTC(result.get(i));
        	returnResult.add(oneCandle);
        }
        
        return returnResult;
	}
	
	public ArrayList<CandleData> getHistoricalCandleBitfinex(String symbol) {
        String response = "";
        HttpsURLConnection con = null;
        Scanner inStream = null;
        OutputStream os = null;
        BufferedWriter writer = null;
        int statusCode = 0;
        ArrayList<CandleData> returnResult = new ArrayList<CandleData>();

        try {
        	StringBuilder constructedUrl = new StringBuilder();
        	constructedUrl.append("https://api.bitfinex.com/v2/candles/trade:1D:t");
        	if ((!Utils.isEmpty(symbol))){
        		constructedUrl.append(symbol.toUpperCase() + "/hist");
        	}
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
			        double close = iter.optDouble(2);
			        double high = iter.optDouble(3);
			        double low = iter.optDouble(4);
			        double volume = iter.optDouble(5);
			        CandleData oneCandle = new CandleData(timeStamp, "", low, high, open, close, volume);
			        returnResult.add(oneCandle);
			    }
			} catch (Exception e) {
				if (MoneyBall.isDebug) {
					System.out.println("Error in getting bitfinex data" + e);
				}
			}
		}
        
        return returnResult;
	}
	
	public ArrayList<CandleData> getHistoricalCandleBinance(String symbol) {
        String response = "";
        HttpsURLConnection con = null;
        Scanner inStream = null;
        OutputStream os = null;
        BufferedWriter writer = null;
        int statusCode = 0;
        ArrayList<CandleData> returnResult = new ArrayList<CandleData>();

        try {
        	long endTime = System.currentTimeMillis();
        	long startTime = endTime - Utils.D10_MILLI;
        	StringBuilder constructedUrl = new StringBuilder();
        	constructedUrl.append("https://api.binance.com/api/v1/klines?symbol=" + symbol + "&limit=500&interval=1d&startTime=" + startTime + "&endTime=" + endTime);
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
