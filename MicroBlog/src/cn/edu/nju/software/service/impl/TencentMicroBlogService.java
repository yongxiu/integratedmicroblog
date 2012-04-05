package cn.edu.nju.software.service.impl;

import java.io.File;

import com.tencent.weibo.api.T_API;
import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.utils.Utils;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import cn.edu.nju.software.bean.UserInfo;
import cn.edu.nju.software.model.Comments;
import cn.edu.nju.software.model.Statuses;
import cn.edu.nju.software.service.MicroBlogService;
import cn.edu.nju.software.service.MyOAuth;
import cn.edu.nju.software.utils.MicroBlogType;
import cn.edu.nju.software.utils.SystemConfig;

public class TencentMicroBlogService implements MicroBlogService {
	
	private static OAuth oauth;
	private static MyOAuth loginOAuth;
	public static final String CALLBACKURL = "app:AuthorizeActivity";
	
	private static String userToken;
	private static String userTokenSecret;
	
	private static String wifiIp;
	
	@Override
	public boolean isLogin(Activity activity) {
		// TODO Auto-generated method stub
		return ((!isNullOrEmpty(userToken)) && (!isNullOrEmpty(userTokenSecret)));
	}
	
	public static void SetUserToken(Context context, String token, String tokenSecret) {
		userToken = token;
		userTokenSecret = tokenSecret;
		oauth = new OAuth();
		oauth.setOauth_token(token);
		oauth.setOauth_token_secret(tokenSecret);
		
		oauth.setOauth_consumer_key("801123037");
		oauth.setOauth_consumer_secret("eaa7227950c85c0137095087f05bc20a");
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);   
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();   
		int ipAddress = wifiInfo.getIpAddress();
		
		wifiIp = Utils.intToIp(ipAddress);
	}
	
	private boolean isNullOrEmpty(String str) {
		if (str == null) {
			return true;
		} else if (str.trim().equals("")) {
			return true;
		}
		return false;
	}
	@Override
	public void addComment(Activity activity, long id, String comment)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createfriendship(Activity activity, long id, String name)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destoryStatus(Activity activity, long id) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Comments getComments(Activity activity, long id, long sinceId,
			long maxId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Statuses getFriendsTimeline(Activity activity, long sinceId,
			long maxId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Statuses getUserTimeline(Activity activity, long sinceId, long maxId)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void login(Activity activity, String userid, String password) {
		// TODO Auto-generated method stub
		loginOAuth = new MyOAuth(MicroBlogType.Tencent, SystemConfig.CONSUMERKEY_TENCENT, SystemConfig.CONSUMERSECRET_TENCENT);
		loginOAuth.requestAccessToken(activity, CALLBACKURL,
				SystemConfig.requestTokenEndpointUrl_tencent,
				SystemConfig.accessTokenEndpointUrl_tencent,
				SystemConfig.authorizationWebsiteUrl_tencent);
	}

	@Override
	public Comments mentionsComment(Activity activity, long sinceId, long maxId)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Statuses mentionsStatus(Activity activity, long sinceId, long maxId)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void quit(Activity activity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void repost(Activity activity, long id, String status, int iscomment)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void share2weibo(Activity activity, String content, String url)
			throws Exception {
		// TODO Auto-generated method stub
		
		T_API tapi = new T_API();
		
		String s = tapi.add(oauth, "json", content, wifiIp, "", "");
		System.out.println(s);
	}

	@Override
	public void share2weibo(Activity activity, String content, File file)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}