package com.moneyball.network;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.moneyball.math.Utils;

public class AllSymbols {
	
	public String TAG = "AllSymbols";
	
	public ArrayList<String> getAllSymbolsHitbtc() {
	    ArrayList<String> allSymbols = new ArrayList<String>();
        String response = "";
        HttpsURLConnection con = null;
        Scanner inStream = null;
        OutputStream os = null;
        BufferedWriter writer = null;
        int statusCode = 0;

        try {
        	StringBuilder constructedUrl = new StringBuilder();
        	constructedUrl.append("https://api.hitbtc.com/api/2/public/symbol");
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
        	System.out.println(TAG + "Failed to retrieve symbols");
        }
        
        String symboliter = "";
        
        for (int i = 0 ; i < result.size(); i++) {
            symboliter = Utils.parseBaseCurrencyHITBIT(result.get(i));
            if (!Utils.isEmpty(symboliter)) {
            	allSymbols.add(symboliter);
            }
        }
        return allSymbols;
	}
	
	
	public ArrayList<String> getAllSymbolsBitfinex() {
	    ArrayList<String> allSymbols = new ArrayList<String>();
        String response = "";
        HttpsURLConnection con = null;
        Scanner inStream = null;
        OutputStream os = null;
        BufferedWriter writer = null;
        int statusCode = 0;

        try {
        	StringBuilder constructedUrl = new StringBuilder();
        	constructedUrl.append("https://api.bitfinex.com/v1/symbols");
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
        
        JSONArray responseJSONArray = null;
        try {
			responseJSONArray = new JSONArray(response);
		} catch (JSONException e) {
		}

        if ((responseJSONArray == null) || (responseJSONArray.length() == 0)) {
        	System.out.println(TAG + "Failed to retrieve symbols");
        }
        
        String symboliter = "";
        
        for (int i = 0 ; i < responseJSONArray.length(); i++) {
            try {
				symboliter = responseJSONArray.getString(i);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            if (!Utils.isEmpty(symboliter)) {
            	allSymbols.add(symboliter);
            }
        }
        return allSymbols;
	}
	
	public ArrayList<String> getAllSymbolsBinance() {
	    ArrayList<String> allSymbols = new ArrayList<String>();
        String response = "";
        HttpsURLConnection con = null;
        Scanner inStream = null;
        OutputStream os = null;
        BufferedWriter writer = null;
        int statusCode = 0;

        try {
        	StringBuilder constructedUrl = new StringBuilder();
        	constructedUrl.append("https://api.binance.com/api/v1/ticker/allPrices");
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
        
        JSONArray responseJSONArray = null;
        try {
			responseJSONArray = new JSONArray(response);
		} catch (JSONException e) {
		}

        if ((responseJSONArray == null) || (responseJSONArray.length() == 0)) {
        	System.out.println(TAG + "Failed to retrieve symbols");
        }
        
        JSONObject jsonIter = new JSONObject();
        String symbolIter = "";
        
        for (int i = 0 ; i < responseJSONArray.length(); i++) {
            try {
            	jsonIter = responseJSONArray.optJSONObject(i);
            	symbolIter = jsonIter.optString("symbol");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            if ((!Utils.isEmpty(symbolIter)) && (!symbolIter.endsWith("ETH")) && (!symbolIter.endsWith("BNB"))) {
            	allSymbols.add(symbolIter);
            }
        }
        return allSymbols;
	}

}
