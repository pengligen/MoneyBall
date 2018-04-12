package com.moneyball.notify;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import com.moneyball.crawler.CandleData;
import com.moneyball.math.Utils;

public class Sms {
	
	
	public static void sendSms(String phoneNumber, String content) {
		String response = "";
        HttpsURLConnection con = null;
        Scanner inStream = null;
        OutputStream os = null;
        BufferedWriter writer = null;
        int statusCode = 0;

        try {
        	StringBuilder constructedUrl = new StringBuilder();
        	constructedUrl.append("https://rest.nexmo.com/sms/json?api_key=fefe030a&api_secret=e3280cfe6fb97441&to=" + phoneNumber + "&from=12066202891&text=" + URLEncoder.encode(content));
        	System.out.println("Sms url" + constructedUrl);
            URL getUrl = new URL(constructedUrl.toString());
            con = (HttpsURLConnection) getUrl.openConnection();
            con.setDoInput(true);
            con.setReadTimeout(15000);
            con.setRequestMethod("POST");
            con.connect();

            statusCode = con.getResponseCode();
            StringBuilder result = new StringBuilder();

            if (statusCode == 200) {
                inStream = new Scanner(con.getInputStream());
                System.out.println("Short message sent successfully");
            } else {
                inStream = new Scanner(con.getErrorStream());
                System.out.println("Short message sent failed");
            }
            while (inStream.hasNextLine()) {
                result.append(inStream.nextLine());
            }
            // TODO : We might remove response in the future since we don't need this
            response = result.toString();
        } catch (Exception e) {
        	System.out.println("Short message sent failed" + e);
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
	}

}
