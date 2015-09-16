package org.cnwir.https;

public class UrlContent {
	public static final String URLHEADER = "http://61.157.217.217:8000/AppInterface/";

	public static final String getURL(final String urlArgs, final String content) {
		return URLHEADER + urlArgs + "?" + content;
	};
}
