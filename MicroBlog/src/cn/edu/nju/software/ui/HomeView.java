package cn.edu.nju.software.ui;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import cn.edu.nju.software.model.StatusItem;
import cn.edu.nju.software.model.Statuses;
import cn.edu.nju.software.service.user.impl.UserServiceImpl;
import cn.edu.nju.software.utils.MicroBlogType;

public class HomeView extends LinearLayout {

	private ListView msgList;
	private ImageButton refreshBtn;
	private Activity activity;

	private ProgressDialog progressDialog;

	private WeiBoAdapter adapter;

	private static final int REFRESH_COMPLETE = 0;

	private Handler homeHandler = new HomeHandler();

	public HomeView(Context activity, AttributeSet attrs) {
		super(activity, attrs);
		init((Activity) activity);
	}

	public HomeView(Context activity) {
		super(activity);
		init((Activity) activity);
	}

	private void init(Activity activity) {
		this.activity = activity;
		LayoutInflater li = LayoutInflater.from(activity);
		addView(li.inflate(R.layout.home, null));

		msgList = (ListView) findViewById(R.home.Msglist);
		refreshBtn = (ImageButton) findViewById(R.home.StatusRefresh);

		progressDialog = new ProgressDialog(activity);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setTitle("请稍等");
		progressDialog.setMessage("正在读取数据中!");

		adapter = new WeiBoAdapter(activity);
		msgList.setAdapter(adapter);

		msgList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(final AdapterView<?> arg0, View view,
					int arg2, long arg3) {
				if (arg0.getItemAtPosition(arg2) == null) {
					progressDialog.show();
					new Thread() {
						public void run() {
							getMore(Long.parseLong(((StatusItem) arg0
									.getItemAtPosition(arg0.getCount() - 2))
									.getId()));
							homeHandler.sendEmptyMessage(REFRESH_COMPLETE);
						}
					}.start();
				} else {
					Serializable status = (Serializable) view.getTag();
					Intent intent = new Intent(HomeView.this.activity,
							WeiboDetailActivity.class);
					intent.putExtra("status", status);
					HomeView.this.activity.startActivity(intent);
				}
			}

		});

		refreshBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				refreshBtn.setEnabled(false);
				progressDialog.show();
				new Thread() {
					public void run() {
						refresh();
						homeHandler.sendEmptyMessage(REFRESH_COMPLETE);
					}
				}.start();
			}

		});

	}

	private void getMore(long maxId) {
		StatusItem[] status = UserServiceImpl.getService().getFriendsTimeline(
				activity, 0, maxId - 1);
		List<StatusItem> weiboList = Arrays.asList(status);
		if (weiboList != null) {
			adapter.add(weiboList);
		}
	}

	private void refresh() {
		StatusItem[] status = UserServiceImpl.getService().getFriendsTimeline(
				activity, 0, 0);
		List<StatusItem> weiboList = Arrays.asList(status);
		if (weiboList != null) {
			adapter.refresh(weiboList);
		}
	}

	private class HomeHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case REFRESH_COMPLETE:
				progressDialog.dismiss();
				refreshBtn.setEnabled(true);
				adapter.notifyDataSetChanged();
				break;
			}
		}

	}

}
