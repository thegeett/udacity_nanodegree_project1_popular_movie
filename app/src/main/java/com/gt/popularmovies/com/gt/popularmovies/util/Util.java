package com.gt.popularmovies.com.gt.popularmovies.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by geetthaker on 7/10/15.
 */
public class Util {

    private static final String USER_AGENT = "Mozilla/5.0";

    public static String makeHttpPostCall(String urlStr, HashMap<String, String> postDataParams) {
        URL url;
        String response = "";
        try {
            url = new URL("http://api.themoviedb.org/3/discover/movie");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            //conn.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
            //conn.setRequestProperty("Accept","*/*");


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            String query = "sort_by=popularity.desc&api_key=aa45deac18e57932618dcaf9e6d0baca";
            writer.write(query);

            writer.flush();
            writer.close();
            os.close();
            System.out.println("ggggg" + conn.getURL());
            int responseCode = conn.getResponseCode();

            //if (responseCode == HttpsURLConnection.HTTP_OK) {
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = br.readLine()) != null) {
                response += line;
            }
            //} else {
            //   response = "";
            //}
        } catch (Exception e) {
            System.out.println("geet");
            e.printStackTrace();
        }

        return response;
    }

    // HTTP GET request
    public static String makeHttpGetCall(String urlStr) throws Exception {

        URL obj = new URL(urlStr);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + urlStr);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());
        if (response != null && !response.toString().isEmpty()) {
            return response.toString();
        }
        return null;
    }

    public static String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
