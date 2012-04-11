package cn.edu.nju.software.ui;

import cn.edu.nju.software.utils.AsyncImageLoader;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class UserInfoActivity extends Activity {
	
	private ImageButton backBtn;
	private ImageButton homeBtn;
	
	private ImageView headiconView;
	private TextView nickTextView;
	private TextView usernameTextView;
	private TextView sexTextView;
	private TextView ageTextView;
	private TextView locationTextView;
	private TextView verifyTextView;
	
	private String userIcon;
	private String userName;
	private String userId;
	
	private AsyncImageLoader asyncImageLoader;
	
	public void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_info);
		
		asyncImageLoader = new AsyncImageLoader();
		
		backBtn = (ImageButton) findViewById(R.id.Back);
		homeBtn = (ImageButton) findViewById(R.id.Home);
		headiconView = (ImageView)findViewById(R.id.user_headicon);
		nickTextView = (TextView)findViewById(R.id.user_nick);
		usernameTextView = (TextView)findViewById(R.id.user_name);
		
		sexTextView = (TextView)findViewById(R.id.user_sex);
		ageTextView = (TextView)findViewById(R.id.user_age);
		locationTextView = (TextView)findViewById(R.id.user_location);
		verifyTextView = (TextView)findViewById(R.id.user_verifyinfo);
		
		backBtn.setOnClickListener(new BackBtnListener());
		homeBtn.setOnClickListener(new HomeBtnListener());
		
		userIcon = getIntent().getStringExtra("userIcon");
		userName = getIntent().getStringExtra("userName");
		userId = getIntent().getStringExtra("userId");
		
		asyncImageLoader.loadDrawable(userIcon, headiconView);
		nickTextView.setText(userName);
	}
	
	private class BackBtnListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			UserInfoActivity.this.finish();
		}
		
	}
	
	private class HomeBtnListener implements OnClickListener {

		@Override
		public void onClick(View v) {

		}
		
	}
}
