package cn.edu.nju.software.model;

import java.io.Serializable;

import cn.edu.nju.software.utils.MicroBlogType;

@SuppressWarnings("serial")
public abstract class CommentItem implements Serializable {

	public abstract MicroBlogType getMicroBlogType();

	public abstract String getId();

	public abstract String getContent();

	public abstract String getCreatedTime();

	public abstract String getUserId();

	public abstract String getUserName();

	public abstract String getUserIcon();

	public abstract StatusItem getStatus();

	public abstract CommentItem getReply();

}
