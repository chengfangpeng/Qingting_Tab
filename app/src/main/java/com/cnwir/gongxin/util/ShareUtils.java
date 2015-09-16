package com.cnwir.gongxin.util;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cnwir.gongxin.R;
import com.cnwir.gongxin.service.ClientTask;
import com.cnwir.gongxin.service.RequestURL;
import com.cnwir.gongxin.view.CustomShareBoard;
import com.cnwir.gongxin.wxapi.Constants;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.MailShareContent;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.RenrenShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.SmsShareContent;
import com.umeng.socialize.media.TencentWbShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.RenrenSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

/**
 * 功能描述：启动分享的工具类
 * @author Cheng.F.P
 * 
 * */
public class ShareUtils {

	private static UMSocialService mController = null;
	private SHARE_MEDIA mPlatform = SHARE_MEDIA.SINA;

	public static void showShare(Context context, int appId, String title, String contents, String targetUrl, String imageUrl) {
		mController = UMServiceFactory.getUMSocialService("com.umeng.share");
		countShare(context, appId);
		configPlatforms(context);
		// 设置分享的内容
		setShareContent(context, title, contents, targetUrl, imageUrl);
		// mController.openShare((Activity) context, false);
		CustomShareBoard shareBoard = new CustomShareBoard((Activity) context);
		Activity activity = (Activity) context;
		shareBoard.showAtLocation(activity.getWindow().getDecorView(), Gravity.LEFT, activity.getWindow()
				.getDecorView().getWidth() / 10, 50);
		
		

	}
	
	/**
	 * 功能描述：设置分享的内容
	 * 
	 * */

	private static void setShareContent(Context context, String title, String contents, String targetUrl,
			String imageUrl) {

		UMImage localImage = new UMImage(context, R.drawable.ic_launcher);
		UMImage urlImage = new UMImage(context, imageUrl);
		urlImage.setTargetUrl(targetUrl);

		WeiXinShareContent weixinContent = new WeiXinShareContent();
		weixinContent.setShareContent(contents + targetUrl);
		weixinContent.setTitle(title);
		weixinContent.setTargetUrl(targetUrl);
		weixinContent.setShareMedia(urlImage);
		mController.setShareMedia(weixinContent);

		// 设置朋友圈分享的内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent(contents + targetUrl);
		circleMedia.setTitle(title);
		circleMedia.setShareMedia(urlImage);
		circleMedia.setTargetUrl(targetUrl);
		mController.setShareMedia(circleMedia);

		// 设置renren分享内容
		RenrenShareContent renrenShareContent = new RenrenShareContent();
		renrenShareContent.setShareContent(contents);
		renrenShareContent.setAppWebSite(targetUrl);
		mController.setShareMedia(renrenShareContent);

		// 设置QQ空间分享内容
		QZoneShareContent qzone = new QZoneShareContent();
		qzone.setShareContent(contents);
		qzone.setTargetUrl(targetUrl);
		qzone.setTitle(title);
		qzone.setShareMedia(urlImage);
		mController.setShareMedia(qzone);

		QQShareContent qqShareContent = new QQShareContent();
		qqShareContent.setShareContent(contents);
		qqShareContent.setTitle(title);
		qqShareContent.setTargetUrl(targetUrl);
		qqShareContent.setShareMedia(urlImage);
		mController.setShareMedia(qqShareContent);

		TencentWbShareContent tencent = new TencentWbShareContent();
		tencent.setShareContent(contents);
		// 设置tencent分享内容
		tencent.setAppWebSite(targetUrl);
		tencent.setTargetUrl(targetUrl);
		tencent.setShareMedia(urlImage);
		tencent.setTitle(title);
		mController.setShareMedia(tencent);

		// 设置邮件分享内容， 如果需要分享图片则只支持本地图片
		MailShareContent mail = new MailShareContent(localImage);
		mail.setShareContent(contents + targetUrl);
		// 设置tencent分享内容
		mail.setTitle(title);
		mController.setShareMedia(mail);

		// 设置短信分享内容
		SmsShareContent sms = new SmsShareContent();
		sms.setShareContent(contents + targetUrl);
		sms.setShareImage(urlImage);
		mController.setShareMedia(sms);

		SinaShareContent sinaContent = new SinaShareContent();
		sinaContent.setShareContent(contents + targetUrl);
		sinaContent.setAppWebSite(targetUrl);
		sinaContent.setShareMedia(urlImage);
		sinaContent.setTitle(title);
		mController.setShareMedia(sinaContent);

	}

	private static void configPlatforms(Context context) {

		// 添加新浪SSO授权
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		// 添加腾讯微博SSO授权
		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
		// 添加人人网SSO授权
		RenrenSsoHandler renrenSsoHandler = new RenrenSsoHandler(context, "201874", "28401c0964f04a72a14c812d6132fcef",
				"3bf66e42db1e4fa9829b955cc300b737");
		mController.getConfig().setSsoHandler(renrenSsoHandler);

		// 添加QQ、QZone平台
		addQQQZonePlatform(context);

		// 添加微信、微信朋友圈平台
		addWXPlatform(context);

	}

	private static void addWXPlatform(Context context) {

		// 注意：在微信授权的时候，必须传递appSecret
		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(context, Constants.APP_ID, Constants.APP_SECRET);
		wxHandler.addToSocialSDK();

		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(context, Constants.APP_ID, Constants.APP_SECRET);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();

	}

	private static void addQQQZonePlatform(Context context) {

		String appId = "1104521742";
		String appKey = "0B5EqUdNiDF0wYnP";
		// 添加QQ支持, 并且设置QQ分享内容的target url
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((Activity) context, appId, appKey);
		qqSsoHandler.addToSocialSDK();

		// 添加QZone平台
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler((Activity) context, appId, appKey);
		qZoneSsoHandler.addToSocialSDK();

	}
	
	/**
	 * 统计每个应用分享的次数
	 * 
	 * @param addId 应用的Id
	 * 
	 * */
	
	private static void countShare(Context context, int appId){
		
		JsonObjectRequest request = new JsonObjectRequest(RequestURL.countShare(appId), null, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject arg0) {
				
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				
			}
		});	
		
		ClientTask.getInstance(context).addToRequestQueue(request);
		
		
	}

}
