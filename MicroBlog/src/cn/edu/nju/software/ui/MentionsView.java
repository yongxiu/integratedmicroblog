package cn.edu.nju.software.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class MentionsView extends LinearLayout {
	
	private Activity activity;
	private ProgressDialog progressDialog;
	
	public MentionsView(Context activity, AttributeSet attrs) {
		super(activity, attrs);
		init((Activity) activity);
	}
	
	public MentionsView(Context context) {
		super(context);
		init((Activity) activity);
	}
	
	private void init(Activity activity) {
		this.activity = activity;
		LayoutInflater li = LayoutInflater.from(activity);
		addView(li.inflate(R.layout.mentions, null));
		
		/*progressDialog = new ProgressDialog(activity);// 生成一个进度条
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setTitle("请稍等");
		progressDialog.setMessage("正在读取数据中!");
		
		progressDialog.show();*/
	}
}
