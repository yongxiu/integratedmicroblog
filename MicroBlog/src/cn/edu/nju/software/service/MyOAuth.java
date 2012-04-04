package cn.edu.nju.software.service;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.security.KeyStore;
import java.util.List;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import cn.edu.nju.software.bean.UserInfo;
import cn.edu.nju.software.service.impl.TencentMicroBlogService;
import cn.edu.nju.software.ui.WebViewActivity;
import cn.edu.nju.software.utils.MicroBlogType;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import commonshttp.CommonsHttpOAuthConsumer;
import commonshttp.CommonsHttpOAuthProvider;

public class MyOAuth {
	
	public static final String TAG = MyOAuth.class.getCanonicalName();
	public static final String SIGNATURE_METHOD = "HMAC-SHA1";
	public static OAuthConsumer consumer;
	public static OAuthProvider provider;
	public String consumerKey;
	public String consumerSecret;
	public String verifier;
	private MicroBlogType weiboId;
	public static VerfierRecivier reciver;
	
	private Context callBackActivity;
	
	public MyOAuth(MicroBlogType weiboId, String consumerKey, String consumerSecret){
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
		this.weiboId = weiboId;
	}
	
	public String requestAccessToken(Activity activity, String callbackurl,
			String requestToken, String accessToken, String authorization) {
		callBackActivity = activity;
		consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
		provider = new CommonsHttpOAuthProvider(requestToken, accessToken, authorization);
		String authUrl = "";
		try {
			if (weiboId.equals(MicroBlogType.Tencent)) {
				authUrl = provider.retrieveRequestTokenForTencent(consumer, callbackurl);
			} else {
				authUrl = provider.retrieveRequestToken(consumer, callbackurl);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.i(MyOAuth.class.getCanonicalName(), "token:" + consumer.getToken() + ",tokenSecret:" + consumer.getTokenSecret());
		Intent intent = new Intent();
		Bundle extras = new Bundle();
		extras.putString("url", authUrl);
		intent.setClass(activity.getApplicationContext(),WebViewActivity.class);
		intent.putExtras(extras);
		activity.startActivity(intent);
		IntentFilter filter = new IntentFilter();
		filter.addAction("ACTION_VERFIER");
		reciver = new VerfierRecivier();
		activity.registerReceiver(reciver, filter);
		return authUrl;
	}
	
	public HttpURLConnection signRequest(String token, String tokenSecret, HttpURLConnection conn) {
		consumer = new DefaultOAuthConsumer(consumerKey, consumerSecret);
		consumer.setTokenWithSecret(token, tokenSecret);
		try {
			consumer.sign(conn);
		} catch (OAuthMessageSignerException e1) {
			e1.printStackTrace();
		} catch (OAuthExpectationFailedException e1) {
			e1.printStackTrace();
		} catch (OAuthCommunicationException e1) {
			e1.printStackTrace();
		}
		try {
			conn.connect();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public HttpResponse signRequest(String token, String tokenSecret,
			String url, List<BasicNameValuePair> params) {
		HttpPost post = new HttpPost(url);
		try {
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// 关闭Expect:100-Continue握手
		// 100-Continue握手需谨慎使用，因为遇到不支持HTTP/1.1协议的服务器或者代理时会引起问题
		post.getParams().setBooleanParameter(
				CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
		return signRequest(token, tokenSecret, post);
	}

	public HttpResponse signRequest(String token, String tokenSecret,
			HttpPost post) {
		consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
		consumer.setTokenWithSecret(token, tokenSecret);
		HttpResponse response = null;
		try {
			consumer.sign(post);
		} catch (OAuthMessageSignerException e1) {
			e1.printStackTrace();
		} catch (OAuthExpectationFailedException e1) {
			e1.printStackTrace();
		} catch (OAuthCommunicationException e1) {
			e1.printStackTrace();
		}
		// 取得HTTP response
		try {
			response = (HttpResponse) getNewHttpClient().execute(post);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}
	
	public HttpClient getNewHttpClient() {
	    try {
	        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
	        trustStore.load(null, null);

	        SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
	        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

	        HttpParams params = new BasicHttpParams();
	        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

	        SchemeRegistry registry = new SchemeRegistry();
	        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
	        registry.register(new Scheme("https", sf, 443));

	        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

	        return new DefaultHttpClient(ccm, params);
	    } catch (Exception e) {
	        return new DefaultHttpClient();
	    }
	}
	
	
	public class VerfierRecivier extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			UserInfo userinfo = new UserInfo();
			Bundle bundle = intent.getExtras();
			if(bundle != null){
				verifier = bundle.getString("verifier");
				Log.i(VerfierRecivier.class.getCanonicalName(), "oauth:" + verifier);
				try{
					provider.setOAuth10a(true);
					if (weiboId.equals(MicroBlogType.Tencent)) {
						provider.retrieveAccessTokenForTencent(consumer, verifier);
						
						TencentMicroBlogService.SetUserToken(context, consumer.getToken(), consumer.getTokenSecret());
					} else {
						provider.retrieveAccessToken(consumer, verifier);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				String userKey = consumer.getToken();
				String userSecret = consumer.getTokenSecret();
				Log.i(VerfierRecivier.class.getCanonicalName(), "userKey:" + userKey);
				Log.i(VerfierRecivier.class.getCanonicalName(), "userSecret:" + userSecret);
				
				userinfo.setToken(userKey);
				userinfo.setTokenSecret(userSecret);
				userinfo.setMbType(weiboId);
				
				Intent intentAuthorize = new Intent();
				intentAuthorize.setClass(context, callBackActivity.getClass());
				intentAuthorize.putExtra("userinfo", userinfo);
				context.startActivity(intentAuthorize);
			}
		}
	}
}
