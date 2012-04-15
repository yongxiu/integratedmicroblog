package cn.edu.nju.software.ui;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.edu.nju.software.model.StatusItem;
import cn.edu.nju.software.utils.AsyncImageLoader;
import cn.edu.nju.software.utils.Utils;

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

	private String userIcon;
	private String userName;
	private String userId;

	private AsyncImageLoader asyncImageLoader;
	private StatusItem statusItem;

	public void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

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

		headiconView = (ImageView) findViewById(R.id.show_headicon);
		nickTextView = (TextView) findViewById(R.id.show_nick);
		emailTextView = (TextView) findViewById(R.id.show_email);
		origtextView = (TextView) findViewById(R.id.show_origtext);
		imageView = (ImageView) findViewById(R.id.show_image);
		mcountTextView = (TextView) findViewById(R.id.show_count_mcount);
		timeTextView = (TextView) findViewById(R.id.show_time);
		fromTextView = (TextView) findViewById(R.id.show_from);

		backBtn.setOnClickListener(new BackBtnListener());
		homeBtn.setOnClickListener(new HomeBtnListener());
		toUserInfoBtn.setOnClickListener(new ToUserInfoBtnListener());
		redirectBtn.setOnClickListener(new RepostBtnListener());
		midCommentBtn.setOnClickListener(new MidCommentBtnListener());
		refreshBtn.setOnClickListener(new RefreshBtnListener());
		commentBtn.setOnClickListener(new CommentBtnListener());
		repostBtn.setOnClickListener(new RepostBtnListener());

		this.asyncImageLoader = new AsyncImageLoader();

		Bundle extras = getIntent().getExtras();
		StatusItem status = (StatusItem) extras.getSerializable("status");
		statusItem = status;
		userIcon = status.getUserIcon();
		userName = status.getUserName();
		userId = status.getUserId();

		redirectBtn.setText(status.getRepostsCount());
		midCommentBtn.setText(status.getCommentsCount());
		nickTextView.setText(status.getUserName());
		timeTextView.setText(Utils
				.ConvertTime(new Date(status.getCreatedTime())));
		origtextView
				.setText(status.getContent(), TextView.BufferType.SPANNABLE);
		Utils.textHighlight(origtextView, "http://", " ");
		// 头像
		asyncImageLoader.loadDrawable(status.getUserIcon(), headiconView);
		// 转发微博的原微博
		StatusItem retweetedstatus = status.getRetweetedStatus();
		String path = null;
		ImageView wbimage = null;
		if (retweetedstatus != null) {
			LinearLayout source = (LinearLayout) findViewById(R.id.show_retweeted);
			source.setVisibility(View.VISIBLE);
			TextView sourceText = (TextView) source
					.findViewById(R.id.sourceText);
			sourceText.setText("@" + retweetedstatus.getUserName() + ":"
					+ retweetedstatus.getContent(),
					TextView.BufferType.SPANNABLE);
			Utils.textHighlight(sourceText, "http://", " ");
			if ((path = retweetedstatus.getImgPath()) != null)
				wbimage = (ImageView) source.findViewById(R.id.sourceImage);
		}
		if (path == null && (path = status.getImgPath()) != null
				&& !path.equals(""))
			wbimage = (ImageView) findViewById(R.id.show_image);
		if (path != null && !path.equals("")) {
			wbimage.setVisibility(View.VISIBLE);
			asyncImageLoader.loadDrawable(path, wbimage);
		}

		fromTextView.setText(status.getMicroBlogType().toString());
	}

	private class BackBtnListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			WeiboDetailActivity.this.finish();
		}

	}

	private class HomeBtnListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(WeiboDetailActivity.this,
					MainActivity.class);
			intent.putExtra("view", "home");
			WeiboDetailActivity.this.startActivity(intent);
		}

	}

	private class ToUserInfoBtnListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(WeiboDetailActivity.this,
					UserInfoActivity.class);
			intent.putExtra("userIcon", userIcon);
			intent.putExtra("userName", userName);
			intent.putExtra("userId", userId);
			intent.putExtra("userType", statusItem.getMicroBlogType().name());
			intent.putExtra("followersCount", statusItem.getFollowersCount());
			intent.putExtra("friendsCount", statusItem.getFriendsCount());
			intent.putExtra("statusesCount", statusItem.getStatusesCount());
			WeiboDetailActivity.this.startActivity(intent);
		}

	}

	private class MidCommentBtnListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(WeiboDetailActivity.this,
					CommentsActivity.class);
			intent.putExtra("obj", statusItem);
			WeiboDetailActivity.this.startActivity(intent);
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
			Intent intent = new Intent(WeiboDetailActivity.this,
					ReplyActivity.class);
			intent.putExtra("obj", statusItem);
			WeiboDetailActivity.this.startActivity(intent);
		}

	}

	private class RepostBtnListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(WeiboDetailActivity.this,
					WriteActivity.class);
			intent.putExtra("sendText", "//" + userName + ":"
					+ origtextView.getText());
			WeiboDetailActivity.this.startActivity(intent);
		}

	}
}
