//package org.cnwir.https;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.UnsupportedEncodingException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.HttpStatus;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.protocol.HTTP;
//import org.json.JSONException;
//import com.cnwir.gongxin.bean.RequestVo;
//import com.cnwir.gongxin.util.LogUtil;
//
//public class JsonGetFromHttp {
//
//	public static Object post(RequestVo vo) {
//		String url = vo.requestUrl;
//		String result = "";
//		LogUtil.v("JsonGetFromHttp", "send post url:" + vo.requestUrl);
//		HttpPost post = new HttpPost(vo.requestUrl);
//		try {
//			if (vo.requestDataMap != null) {
//				HashMap<String, String> map = vo.requestDataMap;
//				ArrayList<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>();
//				int i = 0;
//				for (Map.Entry<String, String> entry : map.entrySet()) {
//					BasicNameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue());
//					LogUtil.v("JsonGetFromHttp", entry.getKey() + "=" + entry.getValue());
//					if (i == 0) {
//						url = url.concat("?").concat(entry.getKey()).concat("=").concat(entry.getValue());
//					} else {
//						url = url.concat("&").concat(entry.getKey()).concat("=").concat(entry.getValue());
//					}
//					pairList.add(pair);
//					i = 1;
//				}
//				LogUtil.v("JsonGetFromHttp", url);
//				UrlEncodedFormEntity encode = new UrlEncodedFormEntity(pairList, HTTP.UTF_8);
//				post.setEntity(encode);
//			}
//			HttpClient httpClient = new DefaultHttpClient();
//			HttpResponse response = httpClient.execute(post);
//			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//				HttpEntity resEntity = response.getEntity();
//				BufferedReader bReader = new BufferedReader(new InputStreamReader(resEntity.getContent()));
//				String line = null;
//				while ((line = bReader.readLine()) != null) {
//					result = result + line;
//				}
//				// ConfigCacheUtil.setUrlCache(result, url);
//				LogUtil.d("JsonGetFromHttp", "POST --> " + result);
//				if (vo.jsonParser != null) {
//					try {
//						return vo.jsonParser.parseJSON(result);
//					} catch (JSONException e) {
//						e.printStackTrace();
//					}
//				}
//			} else {
//				throw new RuntimeException("请求失败");
//			}
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return result;
//	}
//
//	public static Object get(RequestVo vo) {
//		HttpClient httpClient = new DefaultHttpClient();
//		LogUtil.d("JsonGetFromHttp", "send get url:" + vo.requestUrl);
//		HttpGet httpGet = new HttpGet(vo.requestUrl);
//		httpGet.setHeader(
//				"User-Agent",
//				"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_5) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.5 Safari/537.31");
//		HttpResponse response = null;
//		String result = "";
//		try {
//			response = httpClient.execute(httpGet);
//			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//				HttpEntity entity = response.getEntity();
//				BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
//				String line = null;
//				while ((line = reader.readLine()) != null) {
//					result = result + line;
//				}
//				// ConfigCacheUtil.setUrlCache(result, url);
//				LogUtil.d("JsonGetFromHttp", "GET --> " + result);
//				if (vo.jsonParser != null) {
//					try {
//						return vo.jsonParser.parseJSON(result);
//					} catch (JSONException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return result;
//	}
//
//	/* HttpClient GET和POST 两种方法从服务器端获取数据* */
//	public static String PostLoadJson(String url, HashMap<String, String> map) {
//		ArrayList<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>();
//		LogUtil.v("JsonGetFromHttp", "send post url:" + url);
//		for (Map.Entry<String, String> entry : map.entrySet()) {
//			BasicNameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue());
//			LogUtil.v("JsonGetFromHttp", entry.getKey() + "=" + entry.getValue());
//			pairList.add(pair);
//		}
//		// List<NameValuePair> nvPairs = new ArrayList<NameValuePair>();
//		// if (pairs != null) {
//		// for (int i = 0; i < pairs.length; i++) {
//		// nvPairs.add(pairs[i]);
//		// }
//		// }
//		String result = "";
//		try {
//			HttpClient httpClient = new DefaultHttpClient();
//			HttpPost post = new HttpPost(url);
//			UrlEncodedFormEntity encode = new UrlEncodedFormEntity(pairList, HTTP.UTF_8);
//			post.setEntity(encode);
//			HttpResponse response = httpClient.execute(post);
//			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//				HttpEntity resEntity = response.getEntity();
//				BufferedReader bReader = new BufferedReader(new InputStreamReader(resEntity.getContent()));
//				String line = null;
//				while ((line = bReader.readLine()) != null) {
//					result = result + line;
//				}
//			} else {
//				throw new RuntimeException("请求失败");
//			}
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ClientProtocolException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		LogUtil.d("JsonGetFromHttp", "POST --> " + result);
//		return result;
//	}
//
//	public static String GetDownloadJson(String url) {
//		HttpClient httpClient = new DefaultHttpClient();
//		LogUtil.d("JsonGetFromHttp", "send get url:" + url);
//		HttpGet httpGet = new HttpGet(url);
//		httpGet.setHeader(
//				"User-Agent",
//				"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_5) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.5 Safari/537.31");
//		HttpResponse response = null;
//		String result = "";
//		try {
//			response = httpClient.execute(httpGet);
//			HttpEntity entity = response.getEntity();
//			BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
//			String line = null;
//			while ((line = reader.readLine()) != null) {
//				result = result + line;
//			}
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		LogUtil.d("JsonGetFromHttp", "GET --> " + result);
//		return result;
//	}
//}
