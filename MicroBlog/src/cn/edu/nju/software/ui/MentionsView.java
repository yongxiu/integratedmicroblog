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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import cn.edu.nju.software.model.CommentItem;
import cn.edu.nju.software.model.Comments;
import cn.edu.nju.software.model.StatusItem;
import cn.edu.nju.software.model.Statuses;
import cn.edu.nju.software.service.user.impl.UserServiceImpl;
import cn.edu.nju.software.utils.MicroBlogType;

public class MentionsView extends LinearLayout {

	private ListView statusList;
	private ListView commentList;
	private Button statusBtn;
	private Button commentBtn;

	private Activity activity;

	private ProgressDialog progressDialog;

	private WeiBoAdapter statusAdapter;
	private AtCommentAdapter commentAdapter;

	private static final int STATUS_REFRESH_COMPLETE = 0;
	private static final int COMMENT_REFRESH_COMPLETE = 1;

	private Handler homeHandler = new HomeHandler();

	public MentionsView(Context activity, AttributeSet attrs) {
		super(activity, attrs);
		init((Activity) activity);
	}

	public MentionsView(Context activity) {
		super(activity);
		init((Activity) activity);
	}

	private void init(Activity activity) {
		this.activity = activity;
		LayoutInflater li = LayoutInflater.from(activity);
		addView(li.inflate(R.layout.mentions, null));

		statusList = (ListView) findViewById(R.mentions.statuslist);
		commentList = (ListView) findViewById(R.mentions.commentlist);

		statusBtn = (Button) findViewById(R.mentions.statusBtn);
		commentBtn = (Button) findViewById(R.mentions.commentBtn);

		progressDialog = new ProgressDialog(activity);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setTitle("请稍等");
		progressDialog.setMessage("正在读取数据中!");

		statusAdapter = new WeiBoAdapter(activity);
		commentAdapter = new AtCommentAdapter(activity);

		statusList.setAdapter(statusAdapter);
		commentList.setAdapter(commentAdapter);

		statusBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				commentList.setVisibility(GONE);
				statusList.setVisibility(VISIBLE);
				progressDialog.show();
				new Thread() {
					public void run() {
						statusRefresh();
						homeHandler.sendEmptyMessage(STATUS_REFRESH_COMPLETE);
					}
				}.start();
			}

		});

		commentBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				statusList.setVisibility(GONE);
				commentList.setVisibility(VISIBLE);
				progressDialog.show();
				new Thread() {
					public void run() {
						commentRefresh();
						homeHandler.sendEmptyMessage(COMMENT_REFRESH_COMPLETE);
					}
				}.start();
			}

		});

		statusList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
				Serializable status = (Serializable) view.getTag();
				Intent intent = new Intent(MentionsView.this.activity,
						WeiboDetailActivity.class);
				intent.putExtra("status", status);
				MentionsView.this.activity.startActivity(intent);
			}

		});

		commentList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
				// StatusItem status = (StatusItem) view.getTag();
				// Intent intent = new Intent(MentionsView.this.activity,
				// WeiboDetailActivity.class);
				// intent.putExtra("comment", status.toString());
				// intent.putExtra("type",
				// status.getMicroBlogType().toString());
				// MentionsView.this.activity.startActivity(intent);
			}
		});

	}

	private void more() {

	}

	private void statusRefresh() {
		try {
			Statuses status = UserServiceImpl.getService().mentionStatus(
					activity, 0, 0, MicroBlogType.Sina);
			StatusItem[] lt = status.getItems();
			List<StatusItem> weiboList = Arrays.asList(lt);
			if (weiboList != null) {
				statusAdapter.refresh(weiboList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void commentRefresh() {
		try {
			Comments comments = UserServiceImpl.getService().mentionComment(
					activity, 0, 0, MicroBlogType.Sina);
			CommentItem[] lt = comments.getItems();
			List<CommentItem> commentList = Arrays.asList(lt);
			if (commentList != null) {
				commentAdapter.refresh(commentList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class HomeHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case STATUS_REFRESH_COMPLETE:
				progressDialog.dismiss();
				statusAdapter.notifyDataSetChanged();
				break;
			case COMMENT_REFRESH_COMPLETE:
				progressDialog.dismiss();
				commentAdapter.notifyDataSetChanged();
				break;
			}
		}

	}

}
