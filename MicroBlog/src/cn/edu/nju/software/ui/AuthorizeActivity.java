package cn.edu.nju.software.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import cn.edu.nju.software.model.Statuses;
import cn.edu.nju.software.service.MicroBlogService;
import cn.edu.nju.software.service.impl.MicroBlogServiceFactory;
import cn.edu.nju.software.service.impl.TencentMicroBlogService;
import cn.edu.nju.software.service.user.impl.UserServiceImpl;
import cn.edu.nju.software.utils.MicroBlogType;

public class AuthorizeActivity extends Activity {
	
	
	private Button authSinaBtn;
	private Button authTencentBtn;
	
	private Button goHomeBtn;
	
	private Button testBtn;
	
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
		
		testBtn = (Button) findViewById(R.id.TestBtn);
		testBtn.setOnClickListener(new TestBtnListener());
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
	
	class TestBtnListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			TencentMicroBlogService.SetUserToken(AuthorizeActivity.this, 
					"9d0033d5b1fe4da6b11ea2c6b9054b1b", 
					"b8ec9466203ce9625b62eaa040fe0cf0");
			
			MicroBlogService service = MicroBlogServiceFactory.getMicroBlogService(MicroBlogType.Tencent);
			
			try {
				Statuses s = service.getFriendsTimeline(AuthorizeActivity.this, 0, 0);
				s.getItems();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
