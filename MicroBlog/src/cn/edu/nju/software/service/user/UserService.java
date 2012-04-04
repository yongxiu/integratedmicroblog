package cn.edu.nju.software.service.user;

import android.app.Activity;
import cn.edu.nju.software.model.Statuses;
import cn.edu.nju.software.utils.MicroBlogType;

public interface UserService {

	public boolean login(Activity context, MicroBlogType type) throws Exception;

	public boolean isLogin(Activity context, MicroBlogType type);
	
	public boolean deleteComment(Activity context, MicroBlogType type);

	public boolean addComment(Activity context, MicroBlogType type);

	public boolean deleteStatus(Activity context, MicroBlogType type);
	
	public boolean addStatus(Activity context, MicroBlogType type);
	
	public String getComment(Activity context, MicroBlogType type);
	
	public void repost(Activity context, MicroBlogType type);
	
	public String getStatus(Activity context, MicroBlogType type);
	
	public boolean quit(Activity context, MicroBlogType type);
	
	public boolean createFriendship(Activity context, MicroBlogType type);
	
	public String mentions(Activity context, MicroBlogType type);

	public Statuses getFriendsTimeline(Activity context, MicroBlogType type, long sinceId,
			long maxId) throws Exception;
	
	public void share2weibo(Activity activity, String content, String url) throws Exception;
}
