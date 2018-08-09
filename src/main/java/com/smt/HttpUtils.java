package com.sbl.pay.subaccount.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


public class HttpUtils {
	private static final CloseableHttpClient httpClient;
	public static final String CHARSET = "UTF-8";

	static {
		RequestConfig config = RequestConfig.custom().setConnectTimeout(60000).setSocketTimeout(15000).build();
		httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
	}

	/**
	 * 发送一个get请求
	 * @param url 请求地址
	 * @param params 名值对参数
	 * @return
	 */
	public static String get(String url, Map<String, String> params) {
		return get(url, params, CHARSET);
	}
	
	/**
	 * 发送一个名值对形式 的请求
	 * @param url 请求地址
	 * @param params 请求参数
	 * @return
	 */
	public static String post(String url, Map<String, String> params) {
		return post(url, params, CHARSET);
	}

	/**
	 * 发送一个get请求
	 * @param url 请求地址
	 * @param params 名值对参数
	 * @param charset 编码
	 * @return
	 */
	public static String get(String url, Map<String, String> params,
			String charset) {
		if (StringUtils.isBlank(url)) {
			return null;
		}
		try {
			if (params != null && !params.isEmpty()) {
				List<NameValuePair> pairs = new ArrayList<NameValuePair>(
						params.size());
				for (Map.Entry<String, String> entry : params.entrySet()) {
					String value = entry.getValue();
					if (value != null) {
						pairs.add(new BasicNameValuePair(entry.getKey(), value));
					}
				}
				url += "?"
						+ EntityUtils.toString(new UrlEncodedFormEntity(pairs,
								charset));
			}
			HttpGet httpGet = new HttpGet(url);
			CloseableHttpResponse response = httpClient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				httpGet.abort();
				return null;
			}
			HttpEntity entity = response.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(entity, charset);
			}
			EntityUtils.consume(entity);
			response.close();
			return result;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 发送一个名值对形式 的请求
	 * @param url 请求地址
	 * @param params 请求参数
	 * @param charset 编码方式
	 * @return
	 */
	public static String post(String url, Map<String, String> params,String charset) {
		if (StringUtils.isBlank(url)) {
			return null;
		}
		try {
			List<NameValuePair> pairs = null;
			if (params != null && !params.isEmpty()) {
				pairs = new ArrayList<NameValuePair>(params.size());
				for (Map.Entry<String, String> entry : params.entrySet()) {
					String value = entry.getValue();
					if (value != null) {
						pairs.add(new BasicNameValuePair(entry.getKey(), value));
					}
				}
			}
			HttpPost httpPost = new HttpPost(url);
			if (pairs != null && pairs.size() > 0) {
				httpPost.setEntity(new UrlEncodedFormEntity(pairs, CHARSET));
			}
			CloseableHttpResponse response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				httpPost.abort();
				throw new RuntimeException("HttpClient,error status code :"+ statusCode);
			}
			HttpEntity entity = response.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(entity, CHARSET);
			}
			EntityUtils.consume(entity);
			response.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 发起一个json的post 请求
	 * @param url 请求地址
	 * @param jsonBody 请求的json体
	 * @return
	 */
	public static String jsonPost(String url, String jsonBody) {
		String responseContent = null;
		try {
			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader("Content-Type", "application/json");
			
			StringEntity requestEntity = new StringEntity(jsonBody, CHARSET );
			httpPost.setEntity( requestEntity );
			
			CloseableHttpResponse response = httpClient.execute(httpPost);
			HttpEntity responseEntity = response.getEntity();
			responseContent = EntityUtils.toString(responseEntity, CHARSET);
			response.close();
			httpClient.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseContent;
	}
	
	/**
	 * 发起一个 带文件的post请求
	 * @param url 请求地址
	 * @param fileUrl 文件地址
	 * @return
	 */
	public static String filePost(String url, String fileUrl) {
		try {
			HttpPost httpPost = new HttpPost(url);
			
			FileBody file = new FileBody(new File(fileUrl));
			StringBody orgId = new StringBody( SXFConfig.orgId , ContentType.TEXT_PLAIN );
			StringBody reqId = new StringBody(UUID.randomUUID().toString().replace("-", ""), ContentType.TEXT_PLAIN);
			
			
			HttpEntity requestEntity = MultipartEntityBuilder.create().addPart( "file", file ).addPart("orgId", orgId).addPart("reqId", reqId).build();
			httpPost.setEntity(requestEntity);
			
			CloseableHttpResponse response = httpClient.execute(httpPost);
			HttpEntity responseEntity = response.getEntity();
			String responseContent = EntityUtils.toString(responseEntity, CHARSET );
			response.close();
			httpClient.close();
			
			return responseContent;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}