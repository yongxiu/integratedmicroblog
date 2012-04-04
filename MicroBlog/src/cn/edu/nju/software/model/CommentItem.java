package cn.edu.nju.software.model;

import cn.edu.nju.software.utils.MicroBlogType;

public interface CommentItem {

	public MicroBlogType getMicroBlogType();
	
	public String getId();
	
	public String getContent();
	
	public String getCreatedTime();
	
	public String getUserId();
	
	public String getUserName();
	
}
