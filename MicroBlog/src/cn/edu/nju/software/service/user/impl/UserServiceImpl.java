package cn.edu.nju.software.service.user.impl;

import android.app.Activity;
import cn.edu.nju.software.model.Statuses;
import cn.edu.nju.software.service.MicroBlogService;
import cn.edu.nju.software.service.impl.MicroBlogServiceFactory;
import cn.edu.nju.software.service.user.UserService;
import cn.edu.nju.software.utils.MicroBlogType;

public class UserServiceImpl implements UserService {

	private static final UserService USER_SERVICE = new UserServiceImpl();

	UserServiceImpl() {
	};

	public static UserService getService() {
		return USER_SERVICE;
	}

	@Override
	public boolean login(Activity context, MicroBlogType type) throws Exception {
		MicroBlogService service = MicroBlogServiceFactory
				.getMicroBlogService(type);
		service.login(context, null, null);
		return true;
	}

	@Override
	public boolean addComment(Activity context, MicroBlogType type) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteComment(Activity context, MicroBlogType type) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteStatus(Activity context, MicroBlogType type) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addStatus(Activity context, MicroBlogType type) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createFriendship(Activity context, MicroBlogType type) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getComment(Activity context, MicroBlogType type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStatus(Activity context, MicroBlogType type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String mentions(Activity context, MicroBlogType type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean quit(Activity context, MicroBlogType type) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void repost(Activity context, MicroBlogType type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isLogin(Activity context, MicroBlogType type) {
		MicroBlogService service = MicroBlogServiceFactory.getMicroBlogService(type);
		
		return service.isLogin(context);
	}

	@Override
	public Statuses getFriendsTimeline(Activity context, MicroBlogType type, long sinceId,
			long maxId) throws Exception {
		MicroBlogService service = MicroBlogServiceFactory
		.getMicroBlogService(type);
		
		return service.getFriendsTimeline(context, sinceId, maxId);
	}

	@Override
	public void share2weibo(Activity activity,
			String content, String url) throws Exception {
		MicroBlogService service = MicroBlogServiceFactory
		.getMicroBlogService(MicroBlogType.Sina);
		
		service.share2weibo(activity, content, url);
	}

}
