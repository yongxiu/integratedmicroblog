package cn.edu.nju.software.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TencentComments implements Comments {

	private String comments;
	private CommentItem[] items;

	public TencentComments(String comments) {
		this.comments = comments;
	}

	@Override
	public CommentItem[] getItems() {
		try {
			
			JSONObject dataObj = new JSONObject(comments).getJSONObject("data");
			JSONArray array = dataObj.getJSONArray("info");
			
			if(array!=null&&array.length()>0){
				items = new TencentCommentItem[array.length()];
				int lenth =array.length();
				for(int i = 0;i<lenth;i++){
					TencentCommentItem item = new TencentCommentItem();
					JSONObject js = array.optJSONObject(i);
					item.setId(js.getString("id"));
					item.setContent(js.getString("text"));
					item.setCreatedTime(js.getString("timestamp"));
					item.setUserId(js.getString("openid"));
					item.setUserName(js.getString("name"));
					
					items[i] = item;
				}
			}
			return items;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
}
