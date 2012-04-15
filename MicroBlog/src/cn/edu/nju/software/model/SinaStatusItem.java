package cn.edu.nju.software.model;

import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.nju.software.utils.MicroBlogType;

@SuppressWarnings("serial")
public class SinaStatusItem extends StatusItem {

	private static MicroBlogType TYPE = MicroBlogType.Sina;

	private String text;
	private String created_at;
	private String id;
	private String userId;
	private String userName;
	private String userIcon;
	private String thumbnail_pic;

	private StatusItem retweeted_status;
	private String repostsCount;
	private String commentsCount;

	private String followersCount;
	private String friendsCount;
	private String statusesCount;

	public String getRepostsCount() {
		return repostsCount;
	}

	public void setRepostsCount(String repostsCount) {
		this.repostsCount = repostsCount;
	}

	public String getCommentsCount() {
		return commentsCount;
	}

	public void setCommentsCount(String commentsCount) {
		this.commentsCount = commentsCount;
	}

	public SinaStatusItem(JSONObject status) {
		text = status.optString("text");
		repostsCount = status.optString("reposts_count");
		commentsCount = status.optString("comments_count");
		created_at = status.optString("created_at");
		id = status.optString("id");
		try {
			JSONObject user = status.getJSONObject("user");
			userId = user.optString("id");
			userName = user.optString("name");
			userIcon = user.optString("profile_image_url");
			followersCount = user.optString("followers_count");
			friendsCount = user.optString("friends_count");
			statusesCount = user.optString("statuses_count");
		} catch (JSONException e) {
		}
		thumbnail_pic = status.optString("thumbnail_pic");
		try {
			retweeted_status = new SinaStatusItem(status
					.getJSONObject("retweeted_status"));
		} catch (JSONException e) {
		}
	}

	@Override
	public MicroBlogType getMicroBlogType() {
		return TYPE;
	}

	@Override
	public String getContent() {
		return text;
	}

	@Override
	public String getCreatedTime() {
		return created_at;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getUserId() {
		return userId;
	}

	@Override
	public String getUserName() {
		return userName;
	}

	@Override
	public String getUserIcon() {
		return userIcon;
	}

	@Override
	public String getImgPath() {
		return thumbnail_pic;
	}

	@Override
	public StatusItem getRetweetedStatus() {
		return retweeted_status;
	}

	public String getFollowersCount() {
		return followersCount;
	}

	public void setFollowersCount(String followersCount) {
		this.followersCount = followersCount;
	}

	public String getFriendsCount() {
		return friendsCount;
	}

	public void setFriendsCount(String friendsCount) {
		this.friendsCount = friendsCount;
	}

	public String getStatusesCount() {
		return statusesCount;
	}

	public void setStatusesCount(String statusesCount) {
		this.statusesCount = statusesCount;
	}

}
