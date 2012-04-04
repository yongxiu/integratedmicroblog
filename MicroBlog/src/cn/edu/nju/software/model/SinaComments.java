package cn.edu.nju.software.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SinaComments implements Comments {

	private String comments;
	private CommentItem[] items;

	public SinaComments(String comments) {
		this.comments = comments;
	}

	@Override
	public CommentItem[] getItems() {
		try {
			JSONObject tem = new JSONObject(comments);
			JSONArray array = tem.getJSONArray("comments");
			items = new SinaCommentItem[array.length()];
			for (int i = 0; i < items.length; i++) {
				items[i] = new SinaCommentItem(array.getJSONObject(i));
			}
			return items;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
}
