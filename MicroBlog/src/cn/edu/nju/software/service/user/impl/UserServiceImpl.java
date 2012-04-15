package cn.edu.nju.software.service.user.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import cn.edu.nju.software.model.CommentItem;
import cn.edu.nju.software.model.StatusItem;
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
	public void replyComment(Activity activity, long id, long cid,
			String comment, MicroBlogType type) throws Exception {
		MicroBlogService service = MicroBlogServiceFactory
				.getMicroBlogService(type);
		service.replyComment(activity, id, cid, comment);
	}

	@Override
	public void replyStatus(Activity activity, long id, String comment,
			MicroBlogType type) throws Exception {
		MicroBlogService service = MicroBlogServiceFactory
				.getMicroBlogService(type);
		service.replyStatus(activity, id, comment);
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
	public CommentItem[] getComment(Activity activity, String id,
			String sinceId, String maxId, MicroBlogType type) {
		MicroBlogService service = MicroBlogServiceFactory
				.getMicroBlogService(type);
		try {
			return service.getComments(activity, id, sinceId, maxId).getItems();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public StatusItem[] getStatus(Activity activity, long sinceId, long maxId) {
		Iterator<MicroBlogService> iterator = MicroBlogServiceFactory
				.getAllMicroBlogService();
		List<StatusItem> list = new ArrayList<StatusItem>();
		while (iterator.hasNext()) {
			MicroBlogService service = iterator.next();
			if (!service.isLogin(activity))
				continue;
			try {
				list.addAll(Arrays.asList(service.getFriendsTimeline(activity,
						sinceId, maxId).getItems()));
			} catch (Exception e) {
				continue;
			}
		}
		Collections.sort(list);
		return list.toArray(new StatusItem[list.size()]);
	}

	@Override
	public StatusItem[] mentionStatus(Activity activity, long sinceId,
			long maxId) {
		Iterator<MicroBlogService> iterator = MicroBlogServiceFactory
				.getAllMicroBlogService();
		List<StatusItem> list = new ArrayList<StatusItem>();
		while (iterator.hasNext()) {
			MicroBlogService service = iterator.next();
			if (!service.isLogin(activity))
				continue;
			try {
				list.addAll(Arrays.asList(service.mentionsStatus(activity,
						sinceId, maxId).getItems()));
			} catch (Exception e) {
				continue;
			}
		}
		Collections.sort(list);
		return list.toArray(new StatusItem[list.size()]);
	}

	@Override
	public CommentItem[] mentionComment(Activity activity, long sinceId,
			long maxId) {
		Iterator<MicroBlogService> iterator = MicroBlogServiceFactory
				.getAllMicroBlogService();
		List<CommentItem> list = new ArrayList<CommentItem>();
		while (iterator.hasNext()) {
			MicroBlogService service = iterator.next();
			if (!service.isLogin(activity))
				continue;
			try {
				list.addAll(Arrays.asList(service.mentionsComment(activity,
						sinceId, maxId).getItems()));
			} catch (Exception e) {
				continue;
			}
		}
		Collections.sort(list);
		return list.toArray(new CommentItem[list.size()]);
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
	public StatusItem[] getFriendsTimeline(Activity activity, long sinceId,
			long maxId) {
		Iterator<MicroBlogService> iterator = MicroBlogServiceFactory
				.getAllMicroBlogService();
		List<StatusItem> list = new ArrayList<StatusItem>();
		while (iterator.hasNext()) {
			MicroBlogService service = iterator.next();
			if (!service.isLogin(activity))
				continue;
			try {
				list.addAll(Arrays.asList(service.getFriendsTimeline(activity,
						sinceId, maxId).getItems()));
			} catch (Exception e) {
				continue;
			}
		}
		Collections.sort(list);
		return list.toArray(new StatusItem[list.size()]);
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
