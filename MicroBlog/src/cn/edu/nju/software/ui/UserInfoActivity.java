package cn.edu.nju.software.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import cn.edu.nju.software.service.impl.MicroBlogServiceFactory;
import cn.edu.nju.software.utils.AsyncImageLoader;
import cn.edu.nju.software.utils.MicroBlogType;

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
	private GridView gridView;
	
	private Handler handler;
	private AsyncImageLoader asyncImageLoader;
	private UserInfoThread thread;
	private String[] returnUserInfo;
	
	private String followersCount;
	private String friendsCount;
	private String statusesCount;
	
	private MicroBlogType userType;
	
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
		
		/*sexTextView = (TextView)findViewById(R.id.user_sex);
		ageTextView = (TextView)findViewById(R.id.user_age);
		locationTextView = (TextView)findViewById(R.id.user_location);
		verifyTextView = (TextView)findViewById(R.id.user_verifyinfo);*/
		
		backBtn.setOnClickListener(new BackBtnListener());
		homeBtn.setOnClickListener(new HomeBtnListener());
		
		userIcon = getIntent().getStringExtra("userIcon");
		userName = getIntent().getStringExtra("userName");
		userId = getIntent().getStringExtra("userId");

		userType = MicroBlogType.valueOf(getIntent().getStringExtra("userType"));
		followersCount = getIntent().getStringExtra("followersCount");
		friendsCount = getIntent().getStringExtra("friendsCount");
		statusesCount = getIntent().getStringExtra("statusesCount");
		
		asyncImageLoader.loadDrawable(userIcon, headiconView);
		nickTextView.setText(userName);
		gridView = (GridView)findViewById(R.id.user_grid);
		
		if (followersCount == null || followersCount.equals("")) {
			handler = new UserInfoHandler();
			thread = new UserInfoThread();
			thread.start();//开启一个线程获取数据
		} else {
			List<String> numsList = new ArrayList<String>();
			numsList.add(statusesCount);
			numsList.add(followersCount);
			numsList.add(friendsCount);
			gridView.setAdapter(new GridAdapter(UserInfoActivity.this, numsList));
		}
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
			Intent intent = new Intent(UserInfoActivity.this,
					MainActivity.class);
			intent.putExtra("view", "home");
			UserInfoActivity.this.startActivity(intent);
		}
		
	}
	
	class UserInfoThread extends Thread {
		@Override
		public void run() {
			returnUserInfo = MicroBlogServiceFactory.getMicroBlogService(MicroBlogType.Tencent).getUserInfo(userId);
			//通知handler处理数据
			Message msg = handler.obtainMessage();
			handler.sendMessage(msg);
		}
	}
	
	class UserInfoHandler extends Handler { 
		@Override
		public void handleMessage(Message msg){
			final List<String> numsList = new ArrayList<String>();
			numsList.add(returnUserInfo[0]);
			numsList.add(returnUserInfo[1]);
			numsList.add(returnUserInfo[2]);
			gridView.setAdapter(new GridAdapter(UserInfoActivity.this, numsList));
		}
	}
	
	class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		List<String> numList;
		
		public GridAdapter(Context context, List<String> numList) {
			super();
			this.numList = numList;
			this.inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return numList.size();
		}

		@Override
		public Object getItem(int position) {
			return numList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent){
			convertView = inflater.inflate(R.layout.userinfo_grid_item, null);
			TextView num = (TextView)convertView.findViewById(R.id.userinfo_grid_num);
			TextView title = (TextView)convertView.findViewById(R.id.userinfo_grid_title);
			ImageView image = (ImageView)convertView.findViewById(R.id.userinfo_grid_image);
			switch (position) {
			case 0:
				num.setText(numList.get(0));
				title.setText("广播");
				image.setVisibility(View.VISIBLE);
				break;
			case 1:
				num.setText(numList.get(1));
				title.setText("听众");
				image.setVisibility(View.VISIBLE);
				break;
			case 2:
				num.setText(numList.get(2));
				title.setText("收听");
				break;

			default:
				break;
			}
			return convertView;
		}
	}

}
