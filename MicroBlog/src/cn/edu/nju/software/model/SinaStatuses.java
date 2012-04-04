package cn.edu.nju.software.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SinaStatuses implements Statuses {

	private String status;
	private StatusItem[] items;

	public SinaStatuses(String status) {
		this.status = status;
	}

	@Override
	public StatusItem[] getItems() {
		try {
			JSONObject tem = new JSONObject(status);
			JSONArray array = tem.getJSONArray("statuses");
			items = new StatusItem[array.length()];
			for (int i = 0; i < items.length; i++) {
				items[i] = new SinaStatusItem(array.getJSONObject(i));
			}
			return items;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

}
