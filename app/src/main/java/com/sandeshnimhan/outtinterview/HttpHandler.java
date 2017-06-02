package com.sandeshnimhan.outtinterview;

import java.net.URL;
import java.net.MalformedURLException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import android.util.Log;


/**
 * Created by nimha on 4/30/2017.
 * www.sandeshnimhan.com
 */

public class HttpHandler {

    /*
    * TAG to display class name/activity- Log on console
    * */
    private static final String TAG = HttpHandler.class.getSimpleName();

    /*
    * Constructor
    */
    public HttpHandler(){

    }

    /*
    * HTTP Connection Manager
    * */
    public String makeServiceCall(String inUrl) {
        String response = null;
        try {
            URL url = new URL(inUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET"); // GET Request to url
            // response from server
            InputStream inputStream = new BufferedInputStream(conn.getInputStream());
            Log.e(TAG, "Returned json string from Http call HttpHandler.java: " + inputStream.toString());
            response = convertInputStreamToString(inputStream);

        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }

    private String convertInputStreamToString(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder(); //StringBuilder to use mutable feature

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }
}
