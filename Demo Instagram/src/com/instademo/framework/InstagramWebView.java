package com.instademo.framework;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;

public class InstagramWebView {
	
	public static JSONObject makeWebServiceCall(String serviceUrl) {
		HttpURLConnection connection = null;
		try {
			URL url = new URL(serviceUrl);
			connection = (HttpURLConnection) 
					url.openConnection();
			connection.setConnectTimeout(0);
			connection.setReadTimeout(0);

			
			int status = connection.getResponseCode();
			if (status == HttpURLConnection.HTTP_UNAUTHORIZED) {
			} else if (status != HttpURLConnection.HTTP_OK) {
			}

			InputStream stream = new BufferedInputStream(
					connection.getInputStream());
			return new JSONObject(getResponseText(stream));

		} catch (MalformedURLException e) {
			e.getMessage();
		} catch (SocketTimeoutException e) {
			e.getMessage();
		} catch (IOException e) {
			e.getMessage();
		} catch (JSONException e) {
			e.getMessage();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}		

		return null;
	}
	
	public static Bitmap retriveBitmap(String url) {
	    final AndroidHttpClient httpClient = AndroidHttpClient.newInstance("Android");
	    final HttpGet getRequest = new HttpGet(url);

	    try {
	        HttpResponse response = httpClient.execute(getRequest);
	        final int statusCode = response.getStatusLine().getStatusCode();
	        if (statusCode != HttpStatus.SC_OK) { 
	            return null;
	        }
	        
	        final HttpEntity entity = response.getEntity();
	        if (entity != null) {
	            InputStream input = null;
	            try {
	                input = entity.getContent(); 
	                final Bitmap bitmap = BitmapFactory.decodeStream(input);
	                return bitmap;
	            } finally {
	                if (input != null) {
	                    input.close();  
	                }
	                entity.consumeContent();
	            }
	        }
	    } catch (Exception e) {
	        getRequest.abort();
	    } finally {
	        if (httpClient != null) {
	            httpClient.close();
	        }
	    }
	    return null;
	}

	@SuppressWarnings("resource")
	private static String getResponseText(InputStream inStream) {
		return new Scanner(inStream).useDelimiter("\\A").next();
	}

}