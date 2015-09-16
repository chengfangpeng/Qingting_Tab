package org.cnwir.mycache;

public class StringUtils {

	/**
	 * 将url转变为文件名 去掉一些特殊符号
	 * 
	 * @param url
	 * @return
	 */
	public static String checkUrlToFileName(String url) {
		String[] strs = url.split("/");
		return strs[strs.length - 4] + strs[strs.length - 3] + strs[strs.length - 2] + strs[strs.length - 1]
				+ strs[strs.length - 4] + ".cache";
	}
}
