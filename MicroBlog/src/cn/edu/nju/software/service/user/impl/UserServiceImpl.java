package cn.edu.nju.software.service.user.impl;

import android.app.Activity;
import cn.edu.nju.software.model.Comments;
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
	public void login(Activity context, MicroBlogType type) {
		MicroBlogService service = MicroBlogServiceFactory
				.getMicroBlogService(type);
		service.login(context, null, null);
	}

	@Override
	public void addComment(Activity activity, long id, String comment,
			MicroBlogType type) throws Exception {
		MicroBlogService service = MicroBlogServiceFactory
				.getMicroBlogService(type);
		service.addComment(activity, id, comment);
	}

	@Override
	public void deleteComment(Activity context, MicroBlogType type) {
		// TODO Auto-generated method stub
	}

	@Override
	public void deleteStatus(Activity activity, long id, MicroBlogType type)
			throws Exception {
		MicroBlogService service = MicroBlogServiceFactory
				.getMicroBlogService(type);
		service.destoryStatus(activity, id);
	}

	@Override
	public void addStatus(Activity activity, String content, String url,
			MicroBlogType type) throws Exception {
		MicroBlogService service = MicroBlogServiceFactory
				.getMicroBlogService(type);
		service.share2weibo(activity, content, url);
	}

	@Override
	public void createFriendship(Activity activity, long id, String name,
			MicroBlogType type) throws Exception {
		MicroBlogService service = MicroBlogServiceFactory
				.getMicroBlogService(type);
		service.createfriendship(activity, id, name);
	}

	@Override
	public Comments getComment(Activity activity, long id, long sinceId,
			long maxId, MicroBlogType type) throws Exception {
		MicroBlogService service = MicroBlogServiceFactory
				.getMicroBlogService(type);
		return service.getComments(activity, id, sinceId, maxId);
	}

	@Override
	public Statuses getStatus(Activity activity, long sinceId, long maxId,
			MicroBlogType type) throws Exception {
		MicroBlogService service = MicroBlogServiceFactory
				.getMicroBlogService(type);
		return service.getUserTimeline(activity, sinceId, maxId);
	}

	@Override
	public Statuses mentionStatus(Activity activity, long sinceId, long maxId,
			MicroBlogType type) throws Exception {
		MicroBlogService service = MicroBlogServiceFactory
				.getMicroBlogService(type);
		return service.mentionsStatus(activity, sinceId, maxId);
	}

	@Override
	public Comments mentionComment(Activity activity, long sinceId, long maxId,
			MicroBlogType type) throws Exception {
		MicroBlogService service = MicroBlogServiceFactory
				.getMicroBlogService(type);
		return service.mentionsComment(activity, sinceId, maxId);
	}

	@Override
	public void quit(Activity activity, MicroBlogType type) throws Exception {
		MicroBlogService service = MicroBlogServiceFactory
				.getMicroBlogService(type);
		service.quit(activity);
	}

	@Override
	public void repost(Activity activity, long id, String status,
			int iscomment, MicroBlogType type) throws Exception {
		MicroBlogService service = MicroBlogServiceFactory
				.getMicroBlogService(type);
		service.repost(activity, id, status, iscomment);
	}

	@Override
	public boolean isLogin(Activity context, MicroBlogType type) {
		MicroBlogService service = MicroBlogServiceFactory
				.getMicroBlogService(type);
		return service.isLogin(context);
	}

	@Override
	public Statuses getFriendsTimeline(Activity context, long sinceId,
			long maxId, MicroBlogType type) throws Exception {
		MicroBlogService service = MicroBlogServiceFactory
				.getMicroBlogService(type);
		return service.getFriendsTimeline(context, sinceId, maxId);
	}

	@Override
	public void share2weibo(Activity activity, String content, String url)
			throws Exception {
		MicroBlogService service = MicroBlogServiceFactory
				.getMicroBlogService(MicroBlogType.Tencent);

		service.share2weibo(activity, content, url);

		service = MicroBlogServiceFactory
				.getMicroBlogService(MicroBlogType.Sina);

		service.share2weibo(activity, content, url);
	}

}
