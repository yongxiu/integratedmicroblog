package cn.edu.nju.software.service.impl;

import android.app.Activity;
import cn.edu.nju.software.model.Comments;
import cn.edu.nju.software.model.Statuses;
import cn.edu.nju.software.service.MicroBlogService;

public class TencentMicroBlogService implements MicroBlogService {

	@Override
	public boolean isLogin(Activity activity) {
		// TODO Auto-generated method stub
		return true;
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

	}

}