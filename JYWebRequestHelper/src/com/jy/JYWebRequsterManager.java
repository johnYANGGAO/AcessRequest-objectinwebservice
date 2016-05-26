package com.jy;

import java.util.HashMap;

import org.apache.http.NameValuePair;

public class JYWebRequsterManager {
	// private static final Object sPoolSync = new Object();
	//
	// private static JYWebRequsterManager sPool;
	//
	// private JYWebRequsterManager next;
	//
	// private static int sPoolSize = 0;
	//
	// private static final int MAX_POOL_SIZE = 15;

	private JYWebRequest request;

	private JYWebRequsterManager(JYWebRequest mRequest) {

		this.request = mRequest;
	}

	/**
	 * 注释的部分(暂保留)，不适合本类 因为构造函数有个其他不同对象的引用
	 */
	public static JYWebRequsterManager obtainHttpVolleyRequest(
			JYWebRequest mRequest) {

		// synchronized (sPoolSync) {
		// if (sPool != null) {
		// JYWebRequsterManager jrm = sPool;
		// sPool = jrm.next;
		// jrm.next = null;
		// sPoolSize--;
		// System.out.println("拿了一个出去" + "池内有 :" + sPoolSize);
		// return jrm;
		// }
		// }
		// System.out.println("管理对象为空");
		return new JYWebRequsterManager(mRequest);
	}

	// // 用完放回
	// public void recycle() {
	//
	// synchronized (sPoolSync) {
	// if (sPoolSize < MAX_POOL_SIZE) {
	// next = sPool;
	// sPool = this;
	// sPoolSize++;
	// System.out.println("放了一个进来" + "池内有 :" + sPoolSize);
	// }
	// }
	// }

	public void sendApiRequest(boolean post, String url,
			HashMap<String, String> header, NameValuePair... nameValuePairs) {
		request.sendAPIRequest(post, url, header, nameValuePairs);
	}

	public void sendApiRequest(int identifyId, boolean post, String url,
			HashMap<String, String> header, NameValuePair... nameValuePairs) {
		request.sendAPIRequest(identifyId, post, url, header, nameValuePairs);
	}

	public void sendWebServiceRequest(String url, HashMap<String, String> map,
			HashMap<String, String> params, String... SHAM) {
		request.sendServiceRequest(url, map, params, SHAM);
	}

	public void sendWebServiceRequest(int identifyId, String url,
			HashMap<String, String> map, HashMap<String, String> params,
			String... SHAM) {
		request.sendServiceRequest(identifyId, url, map, params, SHAM);
	}
}
