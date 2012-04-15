package cn.edu.nju.software.model;

import java.io.Serializable;
import java.util.Date;

import cn.edu.nju.software.utils.MicroBlogType;

@SuppressWarnings("serial")
public abstract class StatusItem implements Serializable,
		Comparable<StatusItem> {

	public abstract MicroBlogType getMicroBlogType();

	public abstract String getId();

	public abstract String getContent();

	public abstract String getCreatedTime();

	public abstract String getUserId();

	public abstract String getUserName();

	public abstract String getUserIcon();

	public abstract String getImgPath();

	public abstract StatusItem getRetweetedStatus();

	public abstract String getRepostsCount();

	public abstract String getCommentsCount();

	public abstract String getFollowersCount();

	public abstract String getFriendsCount();

	public abstract String getStatusesCount();

	@Override
	public int compareTo(StatusItem s) {
		Date a = new Date(getCreatedTime());
		Date b = new Date(s.getCreatedTime());
		return a.compareTo(b) * -1;
	}
}
