package cn.edu.nju.software.service;

import java.io.File;

import android.app.Activity;
import cn.edu.nju.software.model.Comments;
import cn.edu.nju.software.model.Statuses;

public interface MicroBlogService {

	public void login(Activity activity, String userid, String password);

	public void addComment(Activity activity, long id, String comment)
			throws Exception;

	public Statuses getFriendsTimeline(Activity activity, long sinceId,
			long maxId) throws Exception;

	public void repost(Activity activity, long id, String status, int iscomment)
			throws Exception;

	public Comments getComments(Activity activity, long id, long sinceId,
			long maxId) throws Exception;

	public Statuses getUserTimeline(Activity activity, long sinceId, long maxId)
			throws Exception;

	public void destoryStatus(Activity activity, long id) throws Exception;

	public void share2weibo(Activity activity, String content, String url)
			throws Exception;

	public void share2weibo(Activity activity, String content, File file)
	throws Exception;
	
	public Statuses mentionsStatus(Activity activity, long sinceId, long maxId)
			throws Exception;

	public Comments mentionsComment(Activity activity, long sinceId, long maxId)
			throws Exception;

	public void createfriendship(Activity activity, long id, String name)
			throws Exception;

	public void quit(Activity activity) throws Exception;

	public boolean isLogin(Activity activity);

	public String[] getUserInfo(String username);
}
