package com.cnwir.gongxin.service;

public class RequestURL {

	// public static final String http =
	// "Http://192.168.1.148:8080/GongXinApp/app/";
	public static final String http = "http://www.qingting.org.cn/app";
	
	public static final String testHttp = "http://192.168.1.220:8080/GongXinApp/app";
	
	public static final String httpIP = "http://www.qingting.org.cn";

	/**
	 * 描述：添加取消收藏
	 * 
	 * @param appId
	 *            应用Id
	 * @param token
	 *            用户的token
	 * */
	public static String addCollection(int appId, String token) {

		return http + "/" + appId + "/collect?token=" + token;
	}

	/**
	 * 描述：统计分享次数
	 * 
	 * @param appId
	 *            应用Id
	 * 
	 * */

	public static String countShare(int appId) {

		return http + "/" + appId + "/share";
	}

	/**
	 * 
	 * 描述：从本地服务器获取用户的信息
	 * 
	 * */
	public static String getUserInfoFromLocalServer(String token) {

		return http + "/user/info?token=" + token;

	}

	/**
	 * 
	 * 描述：本地服务器注册用户
	 * */

	public static String RegisterLocalServier() {

		return http + "/user/register";
	}

	/**
	 * 
	 * 描述：星级评论
	 * 
	 * @param appId
	 *            应用id， startNum 评价的星数
	 * 
	 * */

	public static String commentStar() {

		return http + "/star";

	}

	/**
	 * 描述：添加到桌面
	 * 
	 * @param appId
	 *            应用Id
	 * 
	 * 
	 * */

	public static String addToDesktop(int appId) {

		return http + "/" + appId + "/addtodesktop";

	}

	/**
	 * 
	 * 描述：精选卡片信息
	 * 
	 * @param cardId
	 *            要获取的卡片名称 获取个卡片时参数可以用逗号隔开添加到接口后
	 * */

	public static String getJingXuanCardInfo(String cardId) {

		return http + "/nice/getModules?ids=" + cardId;
	}

	/**
	 * 
	 * 描述：添加自定义应用
	 * 
	 * 
	 * */

	public static String addApp() {

		return http + "/user/addapp";
	}

	/**
	 * 
	 * 描述：修改自定义应用
	 * 
	 * */

	public static String updateApp() {

		return http + "/user/updateapp";

	}

	/**
	 * 
	 * 描述：删除自定义应用
	 * 
	 * */

	public static String deleteApp(String token, int appId) {

		return http + "/user/delapp?token=" + token + "&id=" + appId;
	}

	/**
	 * 描述：获取收藏列表
	 * 
	 * */
	public static String getCollectAppList(String token, int start) {

		return http + "/collect?token=" + token + "&start=" + start;
	}

	/**
	 * 
	 * 描述：获取自定义应用创建列表
	 * 
	 * */

	public static String getCreateAppList(String token, int start) {

		return http + "/user/app?token=" + token + "&start=" + start;
	}

	/**
	 * 
	 * 描述：获取发现栏目的数据
	 * 
	 * */

	public static String discoveryGridViewData(int start) {

		return http + "/discover?start=" + start;
	}

	/**
	 * 描述：提交反馈意见
	 * 
	 * */

	public static String submitFeedback() {

		return http + "/user/suggest";
	}

	/**
	 * 描述：获取名站信息
	 * 
	 * */
	public static String getMingZhanInfo() {

		return http + "/site/index";
	}

	/**
	 * 描述：获取精选轮播图
	 * 
	 * */

	public static String getJingXuanLooperImg() {

		return http + "/switchimg/jingxuan";
	}

	/**
	 * 
	 * 描述：获取分类内容
	 * 
	 * */

	public static String getCategory() {

		return http + "/category/index";
	}

	/**
	 * 描述：获取游戏内容
	 * 
	 * 
	 * */

	public static String getPlays() {

		return http + "/category/youxis/listsub";
	}

	/**
	 * 
	 * 描述：获取人气内容
	 * */

	public static String getRenqiInfo() {

		return http + "/popularity";
	}

	/**
	 * 
	 * 描述：获取搜索关键字
	 * 
	 * @param start
	 *            从第几条数据开始
	 * 
	 * */

	public static String getSearchKeyWords(int start) {

		// return http + "/keywords?start=" + start;
		return http + "/keywords?start=" + start;
	}

	/**
	 * 描述：获取搜索结果
	 * 
	 * */
	public static String getSearchResultByKeyword() {

		return http + "/search";

	}
	/**
	 * 获取卡片管理信息
	 * 
	 * */
	public static String getCardManagerInfo(){
		
		return http + "/indexlist";
	}
	
	/**
	 * 
	 * 获取与本地已安装想匹配的应用
	 * 
	 * */
	
	public static String getInstallAppInfo(){
		
		return http + "/clouddesktop";
		
		
	}
	
	public static String getJingXuanBanner(String category){
		
		return http + "/switchimg/" + category;
	}
	
	
	

}
