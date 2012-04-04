package cn.edu.nju.software.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;

public class SystemConfig {

	public static String textSize;
	public static boolean autoRemind;
	public static String checkTime;
	public static boolean autoLoadMode;
	
	public static final String SUCCESS = "success";
	public static final String ERROR = "error";
	
	public static final String HTTP_GET = "GET";
	public static final String HTTP_POST = "POST";
	public static final String HTTP_DELETE = "DELETE";
	
	
	public static final int SDK_VERSION_FROYO = 8;
	public static boolean PRE_FROYO = getSDKVersionNumber() < SDK_VERSION_FROYO ? true : false;
	
	public static boolean ISACCESS;
	
	public static final String OAUTH_TOKEN = "oauth_token";
	public static final String OAUTH_TOKEN_SECRET = "oauth_token_secret";
	
	public static final String SINA_WEIBO = "sina_weibo";
	public static final String TENCENT_WEIBO = "tencent_weibo";
	public static final String SOHU_WEIBO = "sohu_weibo";
	public static final String NETEASE_WEIBO = "netease_weibo";
	
	public static final String CONSUMERKEY_SINA = "consumerkey for sina";
	public static final String CONSUMERSECRET_SINA = "consumersecret for sina";
	
	public static final String CONSUMERKEY_TENCENT = "801123037";
	public static final String CONSUMERSECRET_TENCENT = "eaa7227950c85c0137095087f05bc20a";
	
	public static final String CONSUMERKEY_SOHU = "consumerkey for sohu";
	public static final String CONSUMERSECRET_SOHU = "consumersecret for sohu";
	
	public static final String CONSUMERKEY_NETEASE = "consumerkey for netease";
	public static final String CONSUMERSECRET_NETEASE = "consumersecret for netease";
	
	/**获取未授权的Request Token*/
	public static final String requestTokenEndpointUrl_sina = "http://api.t.sina.com.cn/oauth/request_token";
	public static final String requestTokenEndpointUrl_tencent = "https://open.t.qq.com/cgi-bin/request_token";
	public static final String requestTokenEndpointUrl_sohu = "http://api.t.sohu.com/oauth/request_token";
	public static final String requestTokenEndpointUrl_netease = "http://api.t.163.com/oauth/request_token";
	
	/**使用授权后的Request Token换取Access Token*/
	public static final String accessTokenEndpointUrl_sina = "http://api.t.sina.com.cn/oauth/access_token";
	public static final String accessTokenEndpointUrl_tencent = "https://open.t.qq.com/cgi-bin/access_token";
	public static final String accessTokenEndpointUrl_sohu = "http://api.t.sohu.com/oauth/access_token";
	public static final String accessTokenEndpointUrl_netease = "http://api.t.163.com/oauth/access_token";

	/**请求用户授权Request Token*/
	public static final String authorizationWebsiteUrl_sina = "http://api.t.sina.com.cn/oauth/authorize?display=mobile";
	public static final String authorizationWebsiteUrl_tencent = "https://open.t.qq.com/cgi-bin/authorize";
	public static final String authorizationWebsiteUrl_sohu = "http://api.t.sohu.com/oauth/authorize";
	public static final String authorizationWebsiteUrl_netease = "http://api.t.163.com/oauth/authorize?client_type=mobile";
	
	public static int getSDKVersionNumber() {
		int sdkVersion;
		try {
			sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
		} catch (NumberFormatException e) {
			sdkVersion = 0;
		}
		return sdkVersion;
	}
	
	/**
	 * 完成退出程序
	 * @param activity
	 */
	public static void back(Activity activity){
		ActivityManager activityManager = (ActivityManager)activity.getSystemService(Activity.ACTIVITY_SERVICE);
		if(PRE_FROYO){
			activityManager.restartPackage(activity.getPackageName());
		}else{
			Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(startMain);
            System.exit(0);//退出程序
		}
	}
}
