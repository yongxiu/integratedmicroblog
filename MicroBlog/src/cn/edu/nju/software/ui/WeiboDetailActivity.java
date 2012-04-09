package cn.edu.nju.software.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class WeiboDetailActivity extends Activity {
	
	private ImageButton backBtn;
	private ImageButton homeBtn;
	private Button toUserInfoBtn;
	private TextView redirectBtn;
	private TextView midCommentBtn;
	private TextView refreshBtn;
	private TextView commentBtn;
	private TextView repostBtn;
	
	private ImageView headiconView;
	private TextView nickTextView;
	private TextView emailTextView;
	private TextView origtextView;
	private ImageView imageView;
	private TextView mcountTextView;
	private TextView timeTextView;
	private TextView fromTextView;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weibo_detail);
		
		backBtn = (ImageButton) findViewById(R.id.Back);
		homeBtn = (ImageButton) findViewById(R.id.Home);
		toUserInfoBtn = (Button) findViewById(R.id.to_userinfo_btn);
		redirectBtn = (TextView) findViewById(R.id.redirect_btn);
		midCommentBtn = (TextView) findViewById(R.id.mid_comment_btn);
		refreshBtn = (TextView) findViewById(R.id.refresh_btn);
		commentBtn = (TextView) findViewById(R.id.comment_btn);
		repostBtn = (TextView) findViewById(R.id.repost_btn);
		
		headiconView = (ImageView)findViewById(R.id.show_headicon);
		nickTextView = (TextView)findViewById(R.id.show_nick);
		emailTextView = (TextView)findViewById(R.id.show_email);
		origtextView = (TextView)findViewById(R.id.show_origtext);
		imageView = (ImageView)findViewById(R.id.show_image);
		mcountTextView = (TextView)findViewById(R.id.show_count_mcount);
		timeTextView = (TextView)findViewById(R.id.show_time);
		fromTextView = (TextView)findViewById(R.id.show_from);
		
		backBtn.setOnClickListener(new BackBtnListener());
		homeBtn.setOnClickListener(new HomeBtnListener());
		toUserInfoBtn.setOnClickListener(new ToUserInfoBtnListener());
		redirectBtn.setOnClickListener(new RedirectBtnListener());
		midCommentBtn.setOnClickListener(new MidCommentBtnListener());
		refreshBtn.setOnClickListener(new RefreshBtnListener());
		commentBtn.setOnClickListener(new CommentBtnListener());
		repostBtn.setOnClickListener(new RepostBtnListener());
	}
	
	private class BackBtnListener implements OnClickListener {

		@Override
		public void onClick(View v) {

		}
		
	}
	
	private class HomeBtnListener implements OnClickListener {

		@Override
		public void onClick(View v) {

		}
		
	}
	
	private class ToUserInfoBtnListener implements OnClickListener {

		@Override
		public void onClick(View v) {

		}
		
	}
	
	private class RedirectBtnListener implements OnClickListener {

		@Override
		public void onClick(View v) {

		}
		
	}
	
	private class MidCommentBtnListener implements OnClickListener {

		@Override
		public void onClick(View v) {

		}
		
	}
	
	private class RefreshBtnListener implements OnClickListener {

		@Override
		public void onClick(View v) {

		}
		
	}
	
	private class CommentBtnListener implements OnClickListener {

		@Override
		public void onClick(View v) {

		}
		
	}
	
	private class RepostBtnListener implements OnClickListener {

		@Override
		public void onClick(View v) {

		}
		
	}
}
