package cn.edu.nju.software.model;

import java.util.Date;

import org.json.JSONObject;

import cn.edu.nju.software.utils.MicroBlogType;

@SuppressWarnings("serial")
public class TencentStatusItem extends StatusItem {

	private static MicroBlogType TYPE = MicroBlogType.Tencent;

	private String createdTime;
	private String id;
	private String userId;
	private String userName;
	private boolean haveImage;
	private String userIcon;

	private String content;

	private String repostsCount;
	private String commentsCount;

	public String getFollowersCount() {
		return null;
	}

	public String getFriendsCount() {
		return null;
	}

	public String getStatusesCount() {
		return null;
	}

	public void setRepostsCount(String repostsCount) {
		this.repostsCount = repostsCount;
	}

	public void setCommentsCount(String commentsCount) {
		this.commentsCount = commentsCount;
	}


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

	public boolean isHaveImage() {
		return haveImage;
	}

	public void setHaveImage(boolean haveImage) {
		this.haveImage = haveImage;
	}

	public String getUserIcon() {
		return userIcon;
	}

	public void setUserIcon(String userIcon) {
		this.userIcon = userIcon;
	}

	@Override
	public MicroBlogType getMicroBlogType() {
		return TYPE;
	}

	@Override
	public String getImgPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StatusItem getRetweetedStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRepostsCount() {
		// TODO Auto-generated method stub
		return repostsCount;
	}

	@Override
	public String getCommentsCount() {
		// TODO Auto-generated method stub
		return commentsCount;
	}

	@Override
	public Date getDate() {
		if (createdTime != null && !createdTime.equals("")) {
			long timestamp = Long.parseLong(createdTime) * 1000;
			return new Date(timestamp);
		} else {
			return null;
		}
	}

}
