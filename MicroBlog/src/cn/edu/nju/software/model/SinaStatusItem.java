package cn.edu.nju.software.model;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.nju.software.utils.MicroBlogType;
import cn.edu.nju.software.utils.Utils;

public class SinaStatusItem implements StatusItem {

	private JSONObject status;
	private static MicroBlogType TYPE = MicroBlogType.Sina;

	public SinaStatusItem(JSONObject status) {
		this.status = status;
	}

	@Override
	public MicroBlogType getMicroBlogType() {
		return TYPE;
	}

	@Override
	public String getContent() {
		return status.optString("text");
	}

	@Override
	public String getCreatedTime() {

		Date date = new Date(status.optString("created_at"));
		String time = Utils.ConvertTime(date);

		return time;
	}

	@Override
	public String getId() {
		return status.optString("id");
	}

	@Override
	public String getUserId() {
		try {
			JSONObject user = status.getJSONObject("user");
			return user.getString("id");
		} catch (JSONException e) {
			return null;
		}
	}

	@Override
	public String getUserName() {
		try {
			JSONObject user = status.getJSONObject("user");
			return user.getString("name");
		} catch (JSONException e) {
			return null;
		}
	}

	@Override
	public String getUserIcon() {
		try {
			JSONObject user = status.getJSONObject("user");
			return user.getString("profile_image_url");
		} catch (JSONException e) {
			return null;
		}
	}

	@Override
	public boolean isHaveImage() {
		return status.has("thumbnail_pic");
	}

	@Override
	public String getImgPath() {
		return status.optString("thumbnail_pic");
	}

	@Override
	public StatusItem getRetweetedStatus() {
		try {
			return new SinaStatusItem(status.getJSONObject("retweeted_status"));
		} catch (JSONException e) {
			return null;
		}
	}

}
