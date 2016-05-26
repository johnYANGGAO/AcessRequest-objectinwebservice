package com.jy;

import java.util.HashMap;
import org.apache.http.NameValuePair;
import org.apache.http.protocol.HTTP;

public abstract class JYWebRequest {

	@SuppressWarnings("deprecation")
	String CHARSET_UTF8 = HTTP.UTF_8;
	// String CHARSET_GB2312 = "GB2312";
	JYWebListener success;
	JYWebErrorListener error;

	public void sendAPIRequest(boolean post, String url,
			HashMap<String, String> header, NameValuePair... nameValuePairs) {
	}

	public void sendAPIRequest(int identifyId, boolean post, String url,
			HashMap<String, String> header, NameValuePair... nameValuePairs) {
	}

	public void sendServiceRequest(String url, HashMap<String, String> map,
			HashMap<String, String> params, String... SHAM) {
	}

	public void sendServiceRequest(int identifyId, String url,
			HashMap<String, String> map, HashMap<String, String> params,
			String... SHAM) {
	}
}
