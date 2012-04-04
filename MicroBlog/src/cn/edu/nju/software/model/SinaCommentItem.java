package cn.edu.nju.software.model;

import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.nju.software.utils.MicroBlogType;

public class SinaCommentItem implements CommentItem {

	private JSONObject comment;
	private static MicroBlogType TYPE = MicroBlogType.Sina;

	public SinaCommentItem(JSONObject comment) {
		this.comment = comment;
	}

	@Override
	public MicroBlogType getMicroBlogType() {
		return TYPE;
	}

	@Override
	public String getContent() {
		return comment.optString("text");
	}

	@Override
	public String getCreatedTime() {
		return comment.optString("created_at");
	}

	@Override
	public String getId() {
		return comment.optString("id");
	}

	@Override
	public String getUserId() {
		try {
			JSONObject user = comment.getJSONObject("user");
			return user.getString("id");
		} catch (JSONException e) {
			return null;
		}
	}

	@Override
	public String getUserName() {
		try {
			JSONObject user = comment.getJSONObject("user");
			return user.getString("name");
		} catch (JSONException e) {
			return null;
		}
	}
}
