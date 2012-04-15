package cn.edu.nju.software.service.impl;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import cn.edu.nju.software.model.Comments;
import cn.edu.nju.software.model.Statuses;
import cn.edu.nju.software.model.TencentComments;
import cn.edu.nju.software.model.TencentStatuses;
import cn.edu.nju.software.service.MicroBlogService;
import cn.edu.nju.software.service.MyOAuth;
import cn.edu.nju.software.utils.MicroBlogType;
import cn.edu.nju.software.utils.SystemConfig;

import com.tencent.weibo.api.Friends_API;
import com.tencent.weibo.api.Statuses_API;
import com.tencent.weibo.api.T_API;
import com.tencent.weibo.api.User_API;
import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.utils.Utils;

public class TencentMicroBlogService implements MicroBlogService {
	
	private static OAuth oauth;
	private static MyOAuth loginOAuth;
	public static final String CALLBACKURL = "app:AuthorizeActivity";
	
	private static String userToken;
	private static String userTokenSecret;
	
	public final static String OAUTH_CONSUMER_KEY = "801123037";
	public final static String OAUTH_CONSUMER_SECRET = "eaa7227950c85c0137095087f05bc20a";
	
	private static String wifiIp;
	
	@Override
	public boolean isLogin(Activity activity) {
		// TODO Auto-generated method stub
		return ((!isNullOrEmpty(userToken)) && (!isNullOrEmpty(userTokenSecret)));
	}
	
	public static void SetUserToken(Context context, String token, String tokenSecret) {
		userToken = token;
		userTokenSecret = tokenSecret;
		oauth = new OAuth(OAUTH_CONSUMER_KEY, OAUTH_CONSUMER_SECRET, "");
		oauth.setOauth_token(token);
		oauth.setOauth_token_secret(tokenSecret);
		
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
	public void replyStatus(Activity activity, long id, String comment)
			throws Exception {
		// TODO Auto-generated method stub
		T_API tapi = new T_API();
		String s = tapi.reply(oauth, "json", comment, wifiIp, Long.toString(id));
		System.out.println(s);
	}

	@Override
	public void createfriendship(Activity activity, long id, String name)
			throws Exception {
		Friends_API fapi = new Friends_API();
		String str = fapi.add(oauth, "json", name, wifiIp);
		System.out.println(str);
	}

	@Override
	public void destoryStatus(Activity activity, long id) throws Exception {
		// TODO Auto-generated method stub
		T_API tapi = new T_API();
		String s = tapi.del(oauth, "json", Long.toString(id));
		System.out.println(s);
	}

	@Override
	public Comments getComments(Activity activity, long id, long sinceId,
			long maxId) throws Exception {
		T_API tapi = new T_API();
		
		String result = tapi.re_list(oauth, "json", Long.toString(id), "0", "0");
		
		return new TencentComments(result);
	}

	@Override
	public Statuses getFriendsTimeline(Activity activity, long sinceId,
			long maxId) throws Exception {
		Statuses_API tapi = new Statuses_API();
		String s = tapi.home_timeline(oauth, "json", "0", "0", "30");
		
		return new TencentStatuses(s);
	}

	@Override
	public Statuses getUserTimeline(Activity activity, long sinceId, long maxId)
			throws Exception {
		Statuses_API tapi = new Statuses_API();
		String s = tapi.broadcast_timeline(oauth, "json", "0", "0", "0", "30", "0", "7", "1");
		return new TencentStatuses(s);
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
		Statuses_API tapi = new Statuses_API();
		String s = tapi.mentions_timeline(oauth, "json", "0", "0", "30", "0");
		return new TencentStatuses(s);
	}

	@Override
	public void quit(Activity activity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void repost(Activity activity, long id, String status, int iscomment)
			throws Exception {
		// TODO Auto-generated method stub
		T_API tapi = new T_API();
		
		String s = tapi.re_add(oauth, "json", status, wifiIp, Long.toString(id));
		System.out.println(s);
	}

	@Override
	public void share2weibo(Activity activity, String content, String url)
			throws Exception {

		T_API tapi = new T_API();
		
		String s = tapi.add(oauth, "json", content, wifiIp, "", "");
		System.out.println(s);
	}

	@Override
	public void share2weibo(Activity activity, String content, File file)
			throws Exception {

		T_API tapi = new T_API();
		
		String s = tapi.add_pic(oauth, "json", content, wifiIp, file.getAbsolutePath());
		System.out.println(s);
	}

	public String[] getUserInfo(String username) {
		User_API uapi = new User_API();
		String str = null;
		String[] results = new String[]{"", "", ""};
		try {
			str = uapi.other_info(oauth, "json", username);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (str != null && !str.equals("")) {
			try {
				JSONObject dataObj = new JSONObject(str).getJSONObject("data");
				String fansnum = dataObj.optString("fansnum");
				String idolnum = dataObj.optString("idolnum");
				String tweetnum = dataObj.optString("tweetnum");
				
				results[0] = fansnum;
				results[1] = idolnum;
				results[2] = tweetnum;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return results;
	}

	@Override
	public void replyComment(Activity activity, long id, long cid,
			String comment) throws Exception {
		// TODO Auto-generated method stub
		
	}
}