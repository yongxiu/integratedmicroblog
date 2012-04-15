package cn.edu.nju.software.model;

import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.nju.software.utils.MicroBlogType;

@SuppressWarnings("serial")
public class SinaCommentItem extends CommentItem {

	private static MicroBlogType TYPE = MicroBlogType.Sina;

	private String text;
	private String created_at;
	private String id;
	private String userId;
	private String userName;
	private String userIcon;
	private StatusItem status;
	private CommentItem reply_comment;

	public SinaCommentItem(JSONObject comment) {
		text = comment.optString("text");
		created_at = comment.optString("created_at");
		id = comment.optString("id");
		try {
			JSONObject user = comment.getJSONObject("user");
			userId = user.optString("id");
			userName = user.optString("name");
			userIcon = user.optString("profile_image_url");
		} catch (JSONException e) {
		}
		try {
			status = new SinaStatusItem(comment.getJSONObject("status"));
		} catch (JSONException e) {
		}
		try {
			reply_comment = new SinaCommentItem(comment
					.getJSONObject("reply_comment"));
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
	public StatusItem getStatus() {
		return status;
	}

	@Override
	public String getUserIcon() {
		return userIcon;
	}

	@Override
	public CommentItem getReply() {
		return reply_comment;
	}
}
