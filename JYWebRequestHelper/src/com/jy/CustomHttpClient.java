package com.jy;

import org.apache.http.HttpVersion;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

public class CustomHttpClient {

	private String CHARSET_UTF8;

	public CustomHttpClient(String s) {
		this.CHARSET_UTF8 = s;
	}

	/**
	 * 创建httpClient实例
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public synchronized DefaultHttpClient getHttpClient() {
		DefaultHttpClient customerHttpClient;
		HttpParams params = new BasicHttpParams();
		// 设置基本参数
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, CHARSET_UTF8);
		HttpProtocolParams.setUseExpectContinue(params, true);

		HttpProtocolParams
				.setUserAgent(
						params,
						"Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) "
								+ "AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");

		int ConnectionTimeOut = 15000;
		/* 连接超时 */
		HttpConnectionParams.setConnectionTimeout(params, ConnectionTimeOut);
		/* 请求超时 */
		HttpConnectionParams.setSoTimeout(params, 40000);
		// 设置我们的HttpClient支持HTTP和HTTPS两种模式
		SchemeRegistry schReg = new SchemeRegistry();
		schReg.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		schReg.register(new Scheme("https",
				SSLSocketFactory.getSocketFactory(), 443));

		// 使用线程安全的连接管理来创建HttpClient
		PoolingClientConnectionManager conMgr = new PoolingClientConnectionManager(
				schReg);
		conMgr.setMaxTotal(200);
		customerHttpClient = new DefaultHttpClient(conMgr, params);

		return customerHttpClient;
	}
}
