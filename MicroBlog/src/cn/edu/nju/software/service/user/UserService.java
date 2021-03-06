package cn.edu.nju.software.service.user;

import android.app.Activity;
import cn.edu.nju.software.model.CommentItem;
import cn.edu.nju.software.model.StatusItem;
import cn.edu.nju.software.utils.MicroBlogType;

public interface UserService {

	public void login(Activity context, MicroBlogType type);

	public boolean isLogin(Activity context, MicroBlogType type);

	public void deleteComment(Activity context, MicroBlogType type);

	public void replyComment(Activity activity, long id, long cid,
			String comment, MicroBlogType type) throws Exception;

	public void replyStatus(Activity activity, long id, String comment,
			MicroBlogType type) throws Exception;

	public void deleteStatus(Activity activity, long id, MicroBlogType type)
			throws Exception;

	public void addStatus(Activity activity, String content, String url,
			MicroBlogType type) throws Exception;

	public CommentItem[] getComment(Activity activity, String id,
			String sinceId, String maxId, MicroBlogType type);

	public void repost(Activity activity, long id, String status,
			int iscomment, MicroBlogType type) throws Exception;

	public StatusItem[] getStatus(Activity activity, long sinceId, long maxId);

	public void quit(Activity activity, MicroBlogType type) throws Exception;

	public void createFriendship(Activity activity, long id, String name,
			MicroBlogType type) throws Exception;
	
	public StatusItem[] getUserTimeLine(Activity activity, long sinceId,
			long maxId);
	
	public StatusItem[] mentionStatus(Activity activity, long sinceId,
			long maxId);

	public CommentItem[] mentionComment(Activity activity, long sinceId,
			long maxId);

	public StatusItem[] getFriendsTimeline(Activity context, long sinceId,
			long maxId);

	public void share2weibo(Activity activity, String content, String url)
			throws Exception;
}
