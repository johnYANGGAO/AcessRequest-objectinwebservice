package com.jy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;

/**
 * 调用web_service工具类
 * 
 * */
public class WebserviceUtil extends JYWebRequest {
	private int DEFAULTID = 0;

	public WebserviceUtil(JYWebListener success, JYWebErrorListener error) {
		this.error = error;
		this.success = success;
	}

	@SuppressWarnings("rawtypes")
	private void setWebserviceHeader(String space, Element[] header,
			HashMap<String, String> map) {

		for (Iterator<Entry<String, String>> iter = map.entrySet().iterator(); iter
				.hasNext();) {
			Entry entry = (Entry) iter.next();

			Element element = new Element().createElement(space,
					(String) entry.getKey());
			element.addChild(Node.TEXT, (String) entry.getValue());

			header[0].addChild(Node.ELEMENT, element);
		}

	}

	@SuppressWarnings("rawtypes")
	private void setWebserviceProperty(SoapObject soapObject,
			HashMap<String, String> param) {

		for (Iterator<Entry<String, String>> iter = param.entrySet().iterator(); iter
				.hasNext();) {
			Entry entry = (Entry) iter.next();
			soapObject.addProperty((String) entry.getKey(),
					(String) entry.getValue());

		}

	}

	private void jyGetResultBySoap(int identifyId, String url,
			HashMap<String, String> map, HashMap<String, String> params,
			String... SHAM) {
		try {
			List<String> sha = new ArrayList<String>();
			if (SHAM != null) {
				for (int i = 0; i < SHAM.length; i++) {
					sha.add(SHAM[i]);
				}
			}
			String space = sha.get(0);
			String headName = sha.get(1);
			String action = sha.get(2);
			String method = sha.get(3);

			SoapObject soapObject = new SoapObject(space, method);

			Element[] header = new Element[1];
			header[0] = new Element().createElement(space, headName);

			setWebserviceHeader(space, header, map);

			setWebserviceProperty(soapObject, params);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);

			envelope.headerOut = header;
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			envelope.setOutputSoapObject(soapObject);

			(new MarshalBase64()).register(envelope);
			HttpTransportSE httpTranstation = new HttpTransportSE(url);

			httpTranstation.call(action, envelope);
			if (envelope.bodyIn != null) {
				if (identifyId != 0) {
					success.onResponse(((SoapObject) envelope.bodyIn)
							.getProperty(0).toString(), identifyId);
				} else {
					success.onResponse(((SoapObject) envelope.bodyIn)
							.getProperty(0).toString());
				}
			} else {
				if (identifyId != 0) {
					error.onErrorResponse("网络连接错误 ,请检测URL方法名相关", identifyId);
				} else {
					error.onErrorResponse("网络连接错误 ,请检测URL方法名相关");
				}
			}
		} catch (Exception e) {
			error.onErrorResponse(ExceptionToString.getStackMsg(e));
		}

	}

	@Override
	public void sendServiceRequest(String url, HashMap<String, String> map,
			HashMap<String, String> params, String... SHAM) {
		super.sendServiceRequest(url, map, params, SHAM);

		jyGetResultBySoap(DEFAULTID, url, map, params, SHAM);

	}

	@Override
	public void sendServiceRequest(int identifyId, String url,
			HashMap<String, String> map, HashMap<String, String> params,
			String... SHAM) {
		super.sendServiceRequest(identifyId, url, map, params, SHAM);
		jyGetResultBySoap(identifyId, url, map, params, SHAM);
	}

}
