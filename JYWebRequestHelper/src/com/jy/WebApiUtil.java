package com.jy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * 调用webapi工具类
 * 
 * */
@SuppressWarnings("rawtypes")
public class WebApiUtil extends JYWebRequest {

	private static CookieStore cookieStore;
	private int DEFAULTID = 0;

	public WebApiUtil(JYWebListener success, JYWebErrorListener error) {
		this.error = error;
		this.success = success;
	}

	// 如果需要的话 ，可以获取
	public static CookieStore getCookieStore() {
		return cookieStore;
	}

	/**
	 * HttpClient post方法
	 * 
	 * @param url
	 * @param nameValuePairs
	 * @return
	 * @throws IOException
	 */
	private void PostFromWebByHttpClient(int identifyId, String url,
			HashMap<String, String> header, NameValuePair... nameValuePairs) {
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			if (nameValuePairs != null) {
				for (int i = 0; i < nameValuePairs.length; i++) {
					params.add(nameValuePairs[i]);
				}
			}
			UrlEncodedFormEntity urlEncoded = new UrlEncodedFormEntity(params,
					CHARSET_UTF8);

			HttpPost httpPost = new HttpPost(url);
			if (header != null) {
				for (Iterator<Entry<String, String>> iter = header.entrySet()
						.iterator(); iter.hasNext();) {

					Entry entry = (Entry) iter.next();
					httpPost.addHeader((String) entry.getKey(),
							(String) entry.getValue());
				}
			}
			httpPost.setEntity(urlEncoded);
			DefaultHttpClient client = new CustomHttpClient(CHARSET_UTF8)
					.getHttpClient();

			if (cookieStore != null) {
				client.setCookieStore(cookieStore);
			}
			HttpResponse response = client.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

				HttpEntity resEntity = response.getEntity();
				cookieStore = client.getCookieStore();
				if (identifyId != 0) {
					success.onResponse(
							(resEntity == null) ? null : EntityUtils.toString(
									resEntity, CHARSET_UTF8), identifyId);
				} else {
					success.onResponse((resEntity == null) ? null : EntityUtils
							.toString(resEntity, CHARSET_UTF8));
				}
			} else {
				HttpEntity resEntity = response.getEntity();
				if (identifyId != 0) {
					error.onErrorResponse((resEntity == null) ? "网络连接失败，请检查URL"
							: EntityUtils.toString(resEntity, CHARSET_UTF8),
							identifyId);
				} else {
					error.onErrorResponse((resEntity == null) ? "网络连接失败，请检查URL"
							: EntityUtils.toString(resEntity, CHARSET_UTF8));
				}
			}
		} catch (Exception e) {
			error.onErrorResponse(ExceptionToString.getStackMsg(e));
		}
	}

	private void getFromWebByHttpClient(int identifyId, String url,
			HashMap<String, String> header, NameValuePair... nameValuePairs) {

		try {
			StringBuilder sb = new StringBuilder();
			sb.append(url);
			if (nameValuePairs != null && nameValuePairs.length > 0) {
				sb.append("?");
				for (int i = 0; i < nameValuePairs.length; i++) {
					if (i > 0) {
						sb.append("&");
					}
					sb.append(String.format("%s=%s",
							nameValuePairs[i].getName(),
							nameValuePairs[i].getValue()));
				}
			}

			// HttpGet连接对象
			HttpGet httpRequest = new HttpGet(sb.toString());
			if (header != null) {
				for (Iterator<Entry<String, String>> iter = header.entrySet()
						.iterator(); iter.hasNext();) {

					Entry entry = (Entry) iter.next();
					httpRequest.addHeader((String) entry.getKey(),
							(String) entry.getValue());
				}
			}
			DefaultHttpClient httpclient = new CustomHttpClient(CHARSET_UTF8)
					.getHttpClient();
			if (cookieStore != null) {
				httpclient.setCookieStore(cookieStore);
			}
			// 请求HttpClient，取得HttpResponse
			HttpResponse httpResponse = httpclient.execute(httpRequest);
			// 请求成功
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

				cookieStore = httpclient.getCookieStore();
				HttpEntity resEntity = httpResponse.getEntity();
				if (identifyId != 0) {
					success.onResponse(
							(resEntity == null) ? null : EntityUtils.toString(
									resEntity, CHARSET_UTF8), identifyId);
				} else {
					success.onResponse((resEntity == null) ? null : EntityUtils
							.toString(resEntity, CHARSET_UTF8));
				}
			} else {
				HttpEntity resEntity = httpResponse.getEntity();
				if (identifyId != 0) {
					error.onErrorResponse((resEntity == null) ? "网络连接失败，请检查URL"
							: EntityUtils.toString(resEntity, CHARSET_UTF8),
							identifyId);
				} else {
					error.onErrorResponse((resEntity == null) ? "网络连接失败，请检查URL"
							: EntityUtils.toString(resEntity, CHARSET_UTF8));
				}
			}
		} catch (Exception e) {
			error.onErrorResponse(e.getStackTrace().toString());
		}

	}

	@Override
	public void sendAPIRequest(boolean post, String url,
			HashMap<String, String> header, NameValuePair... nameValuePairs) {
		super.sendAPIRequest(post, url, header, nameValuePairs);
		try {
			if (post) {
				PostFromWebByHttpClient(DEFAULTID, url, header, nameValuePairs);
			} else {
				getFromWebByHttpClient(DEFAULTID, url, header, nameValuePairs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void sendAPIRequest(int identifyId, boolean post, String url,
			HashMap<String, String> header, NameValuePair... nameValuePairs) {
		// TODO Auto-generated method stub
		super.sendAPIRequest(identifyId, post, url, header, nameValuePairs);
		try {
			if (post) {
				PostFromWebByHttpClient(identifyId, url, header, nameValuePairs);
			} else {
				getFromWebByHttpClient(identifyId, url, header, nameValuePairs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
