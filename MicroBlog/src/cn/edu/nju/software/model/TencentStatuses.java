package cn.edu.nju.software.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TencentStatuses implements Statuses {

	private String status;
	private StatusItem[] items;

	public TencentStatuses(String status) {
		this.status = status;
	}

	@Override
	public StatusItem[] getItems() {
		try {
			
			JSONObject dataObj = new JSONObject(status).getJSONObject("data");
			JSONArray array = dataObj.getJSONArray("info");
			
			if(array!=null&&array.length()>0){
				items = new StatusItem[array.length()];
				int lenth =array.length();
				for(int i = 0;i<lenth;i++){
					TencentStatusItem item = new TencentStatusItem(array.getJSONObject(i));
					JSONObject js = array.optJSONObject(i);
					item.setId(js.getString("id"));
					item.setContent(js.getString("text"));
					item.setCreatedTime(js.getString("timestamp"));
					item.setRepostsCount(js.getString("count"));
					item.setCommentsCount(js.getString("mcount"));
					if (js.getString("image").equals("null")) {
						item.setHaveImage(false);
					} else {
						item.setHaveImage(true);
					}
					item.setUserIcon(js.getString("head"));
					item.setUserId(js.getString("openid"));
					item.setUserName(js.getString("nick"));
					
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
