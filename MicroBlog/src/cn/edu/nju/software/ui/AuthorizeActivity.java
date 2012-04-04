package cn.edu.nju.software.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import cn.edu.nju.software.service.user.impl.UserServiceImpl;
import cn.edu.nju.software.utils.MicroBlogType;

public class AuthorizeActivity extends Activity {
	
	
	private Button authSinaBtn;
	private Button authTencentBtn;
	
	private Button goHomeBtn;
	
	@Override
	protected void onResume() {
		super.onResume();
		
		if (UserServiceImpl.getService().isLogin(this, MicroBlogType.Sina)
				&& UserServiceImpl.getService().isLogin(this, MicroBlogType.Tencent)) {
			Intent intent = new Intent();
			intent.setClass(this, HomeActivity.class);
			startActivity(intent);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.authorize);
		
		authSinaBtn = (Button) findViewById(R.id.AuthSinaBtn);
		authTencentBtn = (Button) findViewById(R.id.AuthTencentBtn);
		goHomeBtn = (Button) findViewById(R.id.GoHomeBtn);
		
		authSinaBtn.setOnClickListener(new AuthSinaBtnListener());
		authTencentBtn.setOnClickListener(new AuthTencentBtnListener());
		goHomeBtn.setOnClickListener(new GoHomeBtnListener());
	}
	
	class AuthSinaBtnListener implements OnClickListener {

		public void onClick(View v) {
			try {
				UserServiceImpl.getService().login(AuthorizeActivity.this, MicroBlogType.Sina);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	class AuthTencentBtnListener implements OnClickListener {

		public void onClick(View v) {
			try {
				UserServiceImpl.getService().login(AuthorizeActivity.this, MicroBlogType.Tencent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	class GoHomeBtnListener implements OnClickListener {

		public void onClick(View v) {
			if (UserServiceImpl.getService().isLogin(AuthorizeActivity.this, MicroBlogType.Sina)
					&& UserServiceImpl.getService().isLogin(AuthorizeActivity.this, MicroBlogType.Tencent)) {
				Intent intent = new Intent();
				intent.setClass(AuthorizeActivity.this, HomeActivity.class);
				startActivity(intent);
			}
		}
	}
}
