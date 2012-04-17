package cn.edu.nju.software.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ViewFlipper;

public class MainActivity extends Activity {

	private static final int PRE_VIEW = -1;
	private static final int HOME_VIEW = 0;
	private static final int SHARE_VIEW = 1;
	private static final int STATUSMENTIONS_VIEW = 2;
	private static final int COMMENTMENTIONS_VIEW = 3;
	private static final int AUTHORIZE_VIEW = 4;

	private int currentView = HOME_VIEW;

	private ViewFlipper viewFlipper;

	private ImageButton homeBtn;
	private ImageButton shareBtn;
	private ImageButton MentionsBtn;
	private ImageButton selfBtn;
	private ImageButton authorizeBtn;

	private Handler handler;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		handler = new UIHandler();

		viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);

		homeBtn = (ImageButton) findViewById(R.menu.HomeBtn);
		shareBtn = (ImageButton) findViewById(R.menu.ShareBtn);
		MentionsBtn = (ImageButton) findViewById(R.menu.MentionsBtn);
		selfBtn = (ImageButton) findViewById(R.menu.SelfBtn);
		authorizeBtn = (ImageButton) findViewById(R.menu.AuthorizeBtn);

		homeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				handler.sendEmptyMessage(HOME_VIEW);
				setMenuButton(homeBtn);
				resetMenuButton(shareBtn);
				resetMenuButton(MentionsBtn);
				resetMenuButton(selfBtn);
				resetMenuButton(authorizeBtn);
			}

		});

		shareBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				handler.sendEmptyMessage(SHARE_VIEW);
				setMenuButton(shareBtn);
				resetMenuButton(homeBtn);
				resetMenuButton(MentionsBtn);
				resetMenuButton(selfBtn);
				resetMenuButton(authorizeBtn);
			}

		});

		MentionsBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				handler.sendEmptyMessage(STATUSMENTIONS_VIEW);
				setMenuButton(MentionsBtn);
				resetMenuButton(homeBtn);
				resetMenuButton(shareBtn);
				resetMenuButton(selfBtn);
				resetMenuButton(authorizeBtn);
			}

		});

		selfBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				handler.sendEmptyMessage(COMMENTMENTIONS_VIEW);
				setMenuButton(selfBtn);
				resetMenuButton(homeBtn);
				resetMenuButton(shareBtn);
				resetMenuButton(MentionsBtn);
				resetMenuButton(authorizeBtn);
			}

		});

		authorizeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				handler.sendEmptyMessage(AUTHORIZE_VIEW);
				setMenuButton(authorizeBtn);
				resetMenuButton(homeBtn);
				resetMenuButton(shareBtn);
				resetMenuButton(MentionsBtn);
				resetMenuButton(selfBtn);
			}

		});

	}

	private void setMenuButton(ImageButton button) {
		button.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.menu_btn_selected));
	}

	private void resetMenuButton(ImageButton button) {
		button.setBackgroundColor(Color.TRANSPARENT);
	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this).setTitle("确定退出微博").setPositiveButton(
				"确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						System.exit(0);
					}
				}).setNegativeButton("取消", null).show();
	}

	public class UIHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == PRE_VIEW)
				viewFlipper.showPrevious();
			else if (currentView != msg.what) {
				viewFlipper.setDisplayedChild(msg.what);
				currentView = msg.what;
			}
			super.handleMessage(msg);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		ShareView share = (ShareView) viewFlipper.getChildAt(SHARE_VIEW);
		share.refresh(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		String gotoView = intent.getStringExtra("view");
		if (gotoView != null && gotoView.equals("home")) {
			homeBtn.performClick();
		}

		super.onNewIntent(intent);
	}

}