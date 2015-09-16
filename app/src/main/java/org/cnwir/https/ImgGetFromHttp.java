//package org.cnwir.https;
//
//import java.io.IOException;
//import java.io.InputStream;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.HttpStatus;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.DefaultHttpClient;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//
//public class ImgGetFromHttp {
//
//	public static Bitmap get(String url) {
//		HttpClient client = new DefaultHttpClient();
//		HttpGet get = new HttpGet(url);
//		InputStream inputStream = null;
//		try {
//			HttpResponse response = client.execute(get);
//			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//				System.out.println("连接成功");
//				HttpEntity entity = response.getEntity();
//				try {
//					if (entity != null) {
//						inputStream = entity.getContent();
//						Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//						return bitmap;
//					}
//				} finally {
//					if (inputStream != null) {
//						try {
//							inputStream.close();
//							inputStream = null;
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//						entity.consumeContent();
//					}
//				}
//			}
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			client.getConnectionManager().shutdown();
//		}
//		return null;
//	}
//}
