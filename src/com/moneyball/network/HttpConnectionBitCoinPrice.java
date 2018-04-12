package com.moneyball.network;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import com.moneyball.crawler.CandleData;
import com.moneyball.math.Utils;

public class HttpConnectionBitCoinPrice {

	public double getCurrentPrice() {
		//TODO : Implement
		return 0;
	}
	
	public double getPrice(int year, int month, int day) {
		//TODO : Implement
		return 0;
	}
	
	public ArrayList<CandleData> getHistoricalCandle(String start, String end, int granularity) {
        String response = "";
        HttpsURLConnection con = null;
        Scanner inStream = null;
        OutputStream os = null;
        BufferedWriter writer = null;
        int statusCode = 0;

        try {
        	StringBuilder constructedUrl = new StringBuilder();
        	constructedUrl.append("https://api.gdax.com/products/BTC-USD/candles?");
        	if ((!Utils.isEmpty(start)) && (!Utils.isEmpty(start)) && (granularity > 0)) {
        		constructedUrl.append("start=" + start + "&");
        		constructedUrl.append("end=" + end + "&");
        		constructedUrl.append("granularity=" + granularity);
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
        //ArrayList<CandleData> result = Utils.parseResponse(response);
        ArrayList<CandleData> result = null;
        if ((result == null) || (result.size() == 0)) {
        	System.out.println("Network data wrong for " +  start);
        } else {
        	System.out.println("Network data is correct :)");
        }
        try {
            Thread.sleep(330);
        } catch (Exception e) {
        	
        }
        return result;
	}
	
	
	
}
