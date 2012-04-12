package cn.edu.nju.software.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import cn.edu.nju.software.service.user.impl.UserServiceImpl;
import cn.edu.nju.software.utils.MicroBlogType;

public class AuthorizeView extends LinearLayout {

	private Button authSinaBtn;
	private Button authTencentBtn;
	
	private Button testBtn;

	private Activity activity;

	public AuthorizeView(Context activity, AttributeSet attrs) {
		super(activity, attrs);
		init((Activity) activity);
	}

	public AuthorizeView(Context activity) {
		super(activity);
		init((Activity) activity);
	}

	private void init(Activity activity) {
		this.activity = activity;
		LayoutInflater li = LayoutInflater.from(activity);
		addView(li.inflate(R.layout.authorize, null));

		authSinaBtn = (Button) findViewById(R.id.AuthSinaBtn);
		authTencentBtn = (Button) findViewById(R.id.AuthTencentBtn);

		authSinaBtn.setOnClickListener(new AuthSinaBtnListener());
		authTencentBtn.setOnClickListener(new AuthTencentBtnListener());
		
		testBtn = (Button) findViewById(R.id.TestBtn);
		testBtn.setOnClickListener(new TestBtnListener());
	}

	private class AuthSinaBtnListener implements OnClickListener {

		public void onClick(View v) {
			try {
				UserServiceImpl.getService().login(AuthorizeView.this.activity,
						MicroBlogType.Sina);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private class AuthTencentBtnListener implements OnClickListener {

		public void onClick(View v) {
			try {
				UserServiceImpl.getService().login(AuthorizeView.this.activity,
						MicroBlogType.Tencent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private class TestBtnListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(activity, ReplyActivity.class);
			activity.startActivity(intent);
		}
		
	}
}
