/**
 * 
 * 新浪微博提供的服务的接口
 * 
 * @author haosong
 */
package cn.edu.nju.software.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import cn.edu.nju.software.model.Comments;
import cn.edu.nju.software.model.SinaComments;
import cn.edu.nju.software.model.SinaStatuses;
import cn.edu.nju.software.model.Statuses;
import cn.edu.nju.software.service.MicroBlogService;

import com.weibo.android.R;
import com.weibo.net.AccessToken;
import com.weibo.net.AsyncWeiboRunner;
import com.weibo.net.DialogError;
import com.weibo.net.Utility;
import com.weibo.net.Weibo;
import com.weibo.net.WeiboDialogListener;
import com.weibo.net.WeiboException;
import com.weibo.net.WeiboParameters;

public class SinaMicroBlogService implements MicroBlogService {

	private static final String URL_ACTIVITY_CALLBACK = "http://wap.baidu.com";// Oauth2验证回调地址
	/**
	 * 两种HTTP访问方式
	 */
	private static final String GET = "GET";
	private static final String POST = "POST";

	private static Weibo WEIBO = Weibo.getInstance();

	SinaMicroBlogService() {
	}

	/**
	 * Oauth2隐式登录验证
	 */
	@Override
	public void login(Activity activity, String userid, String password) {
		WEIBO.setRedirectUrl(URL_ACTIVITY_CALLBACK);
		WEIBO.authorize(activity, new OAuth2DialogListener(activity));
	}

	/**
	 * 回复评论
	 */
	@Override
	public void replyStatus(Activity activity, long id, String comment)
			throws WeiboException {
		String url = Weibo.SERVER + "comments/create.json";
		WeiboParameters param = new WeiboParameters();
		param.add("source", Weibo.getAppKey());
		param.add("id", Long.toString(id));
		param.add("comment", comment);
		WEIBO.request(activity, url, param, POST, WEIBO.getAccessToken());
	}

	/**
	 * 回复微博
	 */
	@Override
	public void replyComment(Activity activity, long id, long cid,
			String comment) throws WeiboException {
		String url = Weibo.SERVER + "comments/reply.json";
		WeiboParameters param = new WeiboParameters();
		param.add("source", Weibo.getAppKey());
		param.add("cid", Long.toString(cid));
		param.add("id", Long.toString(id));
		param.add("comment", comment);
		WEIBO.request(activity, url, param, POST, WEIBO.getAccessToken());
	}

	/**
	 * 获取当前登录用户及其所关注用户的最新微博
	 */
	@Override
	public Statuses getFriendsTimeline(Activity activity, long sinceId,
			long maxId) throws WeiboException {
		String url = Weibo.SERVER + "statuses/friends_timeline.json";
		WeiboParameters param = new WeiboParameters();
		param.add("source", Weibo.getAppKey());
		param.add("since_id", Long.toString(sinceId));
		param.add("max_id", Long.toString(maxId));
		String result = WEIBO.request(activity, url, param, GET, WEIBO
				.getAccessToken());
		return new SinaStatuses(result);
	}

	/**
	 * 转发一条微博
	 * 
	 * @param id
	 *            要转发的微博ID
	 * @param status
	 *            添加的转发文本
	 * @param iscomment
	 *            是否在转发的同时发表评论，0：否、1：评论给当前微博、2：评论给原微博、3：都评论，默认为0
	 * 
	 */
	@Override
	public void repost(Activity activity, long id, String status, int iscomment)
			throws WeiboException {
		String url = Weibo.SERVER + "statuses/repost.json";
		WeiboParameters param = new WeiboParameters();
		param.add("source", Weibo.getAppKey());
		param.add("id", Long.toString(id));
		param.add("status", status);
		param.add("is_commment", Integer.toString(iscomment));
		WEIBO.request(activity, url, param, POST, WEIBO.getAccessToken());
	}

	/**
	 * 关注某人
	 */
	@Override
	public void createfriendship(Activity activity, long id, String name)
			throws Exception {
		String url = Weibo.SERVER + "friendships/create.json";
		WeiboParameters param = new WeiboParameters();
		param.add("source", Weibo.getAppKey());
		param.add("id", Long.toString(id));
		param.add("screen_name", name);
		WEIBO.request(activity, url, param, POST, WEIBO.getAccessToken());
	}

	/**
	 * 删除微博
	 */
	@Override
	public void destoryStatus(Activity activity, long id) throws Exception {
		String url = Weibo.SERVER + "statuses/destroy.json";
		WeiboParameters param = new WeiboParameters();
		param.add("source", Weibo.getAppKey());
		param.add("id", Long.toString(id));
		WEIBO.request(activity, url, param, POST, WEIBO.getAccessToken());
	}

	/**
	 * 获取评论
	 */
	@Override
	public Comments getComments(Activity activity, String id, String sinceId,
			String maxId) throws Exception {
		String url = Weibo.SERVER + "comments/show.json";
		WeiboParameters param = new WeiboParameters();
		param.add("source", Weibo.getAppKey());
		param.add("id", id);
		param.add("since_id", sinceId);
		param.add("max_id", maxId);
		String result = WEIBO.request(activity, url, param, GET, WEIBO
				.getAccessToken());
		return new SinaComments(result);
	}

	/**
	 * 获取自己发的微博
	 */
	@Override
	public Statuses getUserTimeline(Activity activity, long sinceId, long maxId)
			throws Exception {
		String url = Weibo.SERVER + "statuses/user_timeline.json";
		JSONObject json = new JSONObject(getUser(activity));
		WeiboParameters param = new WeiboParameters();
		param.add("source", Weibo.getAppKey());
		param.add("uid", Long.toString(json.getLong("uid")));
		param.add("since_id", Long.toString(sinceId));
		param.add("max_id", Long.toString(maxId));
		String result = WEIBO.request(activity, url, param, GET, WEIBO
				.getAccessToken());
		return new SinaStatuses(result);
	}

	/**
	 * 提到我的微博
	 */
	@Override
	public Statuses mentionsStatus(Activity activity, long sinceId, long maxId)
			throws Exception {
		String url = Weibo.SERVER + "statuses/mentions.json";
		WeiboParameters param = new WeiboParameters();
		param.add("source", Weibo.getAppKey());
		param.add("since_id", Long.toString(sinceId));
		param.add("max_id", Long.toString(maxId));
		String result = WEIBO.request(activity, url, param, GET, WEIBO
				.getAccessToken());
		return new SinaStatuses(result);
	}

	/**
	 * 提到我的评论
	 */
	@Override
	public Comments mentionsComment(Activity activity, long sinceId, long maxId)
			throws Exception {
		String url = Weibo.SERVER + "comments/to_me.json";
		WeiboParameters param = new WeiboParameters();
		param.add("source", Weibo.getAppKey());
		param.add("since_id", Long.toString(sinceId));
		param.add("max_id", Long.toString(maxId));
		String result = WEIBO.request(activity, url, param, GET, WEIBO
				.getAccessToken());
		return new SinaComments(result);
	}

	/**
	 * 退出
	 */
	@Override
	public void quit(Activity activity) throws Exception {
		String url = Weibo.SERVER + "account/end_session.json";
		WeiboParameters param = new WeiboParameters();
		param.add("source", Weibo.getAppKey());
		WEIBO.request(activity, url, param, GET, WEIBO.getAccessToken());
	}

	/**
	 * 发微博
	 */
	@Override
	public void share2weibo(Activity activity, String content, String url)
			throws Exception {
		if (!TextUtils.isEmpty((String) (WEIBO.getAccessToken().getToken()))) {
			if (!TextUtils.isEmpty(url)) {
				upload(activity, Weibo.getAppKey(), url, content, "", "");

			} else {
				update(activity, Weibo.getAppKey(), content, "", "");
			}
		} else {
			Toast.makeText(activity, activity.getString(R.string.please_login),
					Toast.LENGTH_LONG);
		}
	}

	/**
	 * 发布微博，包含图片
	 * 
	 * @param activity
	 * @param source
	 * @param file
	 * @param status
	 * @param lon
	 * @param lat
	 * @return
	 * @throws WeiboException
	 */
	private String upload(Activity activity, String source, String file,
			String status, String lon, String lat) throws WeiboException {
		WeiboParameters bundle = new WeiboParameters();
		bundle.add("source", source);
		bundle.add("pic", file);
		bundle.add("status", status);
		if (!TextUtils.isEmpty(lon)) {
			bundle.add("lon", lon);
		}
		if (!TextUtils.isEmpty(lat)) {
			bundle.add("lat", lat);
		}
		String rlt = "";
		String url = Weibo.SERVER + "statuses/upload.json";
		AsyncWeiboRunner weiboRunner = new AsyncWeiboRunner(WEIBO);
		weiboRunner.request(activity, url, bundle, Utility.HTTPMETHOD_POST,
				new RequestListener(activity));

		return rlt;
	}

	/**
	 * 发布微博，仅包含文字
	 * 
	 * @param activity
	 * @param source
	 * @param status
	 * @param lon
	 * @param lat
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws WeiboException
	 */
	private String update(Activity activity, String source, String status,
			String lon, String lat) throws MalformedURLException, IOException,
			WeiboException {
		WeiboParameters bundle = new WeiboParameters();
		bundle.add("source", source);
		bundle.add("status", status);
		if (!TextUtils.isEmpty(lon)) {
			bundle.add("lon", lon);
		}
		if (!TextUtils.isEmpty(lat)) {
			bundle.add("lat", lat);
		}
		String rlt = "";
		String url = Weibo.SERVER + "statuses/update.json";
		AsyncWeiboRunner weiboRunner = new AsyncWeiboRunner(WEIBO);
		weiboRunner.request(activity, url, bundle, Utility.HTTPMETHOD_POST,
				new RequestListener(activity));
		return rlt;
	}

	private String getUser(Activity activity) throws Exception {
		String url = Weibo.SERVER + "account/get_uid";
		WeiboParameters param = new WeiboParameters();
		param.add("source", Weibo.getAppKey());
		return WEIBO.request(activity, url, param, GET, WEIBO.getAccessToken());
	}

	@Override
	public boolean isLogin(Activity activity) {
		if (WEIBO.getAccessToken() == null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 
	 * Oauth2验证对话框监听
	 * 
	 * @author haosong
	 * 
	 */
	private class OAuth2DialogListener implements WeiboDialogListener {

		private Activity activity;

		OAuth2DialogListener(Activity activity) {
			this.activity = activity;
		}

		@Override
		public void onComplete(Bundle values) {
			String token = values.getString("access_token");
			String expires_in = values.getString("expires_in");
			AccessToken accessToken = new AccessToken(token, Weibo
					.getAppSecret());
			accessToken.setExpiresIn(expires_in);
			WEIBO.setAccessToken(accessToken);
			Log.d(token, expires_in);
		}

		@Override
		public void onError(DialogError e) {
			Toast.makeText(activity.getApplicationContext(),
					"Auth error : " + e.getMessage(), Toast.LENGTH_LONG).show();
		}

		@Override
		public void onCancel() {
			Toast.makeText(activity.getApplicationContext(), "Auth cancel",
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(activity.getApplicationContext(),
					"Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}

	}

	/**
	 * 发微博监听
	 */
	private static class RequestListener implements
			AsyncWeiboRunner.RequestListener {

		private Activity activity;

		public RequestListener(Activity activity) {
			this.activity = activity;
		}

		@Override
		public void onComplete(String response) {
			activity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(activity, R.string.send_sucess,
							Toast.LENGTH_LONG).show();
				}
			});

			activity.finish();
		}

		@Override
		public void onError(final WeiboException e) {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast
							.makeText(
									activity,
									String.format(activity
											.getString(R.string.send_failed)
											+ ":%s", e.getMessage()),
									Toast.LENGTH_LONG).show();
				}
			});
		}

		@Override
		public void onIOException(IOException e) {

		}

	}

	@Override
	public void share2weibo(Activity activity, String content, File file)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public String[] getUserInfo(String username) {
		// TODO Auto-generated method stub
		return null;
	}

}
