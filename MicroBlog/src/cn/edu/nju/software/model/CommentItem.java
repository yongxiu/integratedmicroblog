package cn.edu.nju.software.model;

import java.io.Serializable;
import java.util.Date;

import cn.edu.nju.software.utils.MicroBlogType;

@SuppressWarnings("serial")
public abstract class CommentItem implements Serializable,
		Comparable<CommentItem> {

	public abstract MicroBlogType getMicroBlogType();

	public abstract String getId();

	public abstract String getContent();

	public abstract String getCreatedTime();

	public abstract String getUserId();

	public abstract String getUserName();

	public abstract String getUserIcon();

	public abstract StatusItem getStatus();

	public abstract CommentItem getReply();

	@Override
	public int compareTo(CommentItem c) {
		Date a = getDate();
		Date b = c.getDate();
		return a.compareTo(b) * -1;
	}

	public abstract Date getDate();

}
