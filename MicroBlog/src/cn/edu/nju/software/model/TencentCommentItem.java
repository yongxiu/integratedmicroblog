package cn.edu.nju.software.model;

import cn.edu.nju.software.utils.MicroBlogType;

public class TencentCommentItem implements CommentItem {

	private static MicroBlogType TYPE = MicroBlogType.Sina;
	
	private String content;
	private String createdTime;
	private String id;
	private String userId;
	private String userName;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public MicroBlogType getMicroBlogType() {
		// TODO Auto-generated method stub
		return TYPE;
	}
	

}
