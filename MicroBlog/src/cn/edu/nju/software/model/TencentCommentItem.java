package cn.edu.nju.software.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.edu.nju.software.utils.MicroBlogType;

@SuppressWarnings("serial")
public class TencentCommentItem extends CommentItem {

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

	@Override
	public StatusItem getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUserIcon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommentItem getReply() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return format.parse(format.format(Long.parseLong(createdTime)));
		} catch (ParseException e) {
			return null;
		}
	}

}
