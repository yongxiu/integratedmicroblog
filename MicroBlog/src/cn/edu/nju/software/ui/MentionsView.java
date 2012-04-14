package cn.edu.nju.software.ui;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
				statusBtn.setBackgroundResource(R.drawable.title_bar_mark);
				commentBtn.setBackgroundColor(Color.TRANSPARENT);
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
				commentBtn.setBackgroundResource(R.drawable.title_bar_mark);
				statusBtn.setBackgroundColor(Color.TRANSPARENT);
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
			public void onItemClick(final AdapterView<?> arg0, View view,
					int arg2, long arg3) {
				if (arg0.getItemAtPosition(arg2) == null) {
					progressDialog.show();
					new Thread() {
						public void run() {
							getMoreStatuses(Long.parseLong(((StatusItem) arg0
									.getItemAtPosition(arg0.getCount() - 2))
									.getId()));
							homeHandler
									.sendEmptyMessage(STATUS_REFRESH_COMPLETE);
						}
					}.start();
				} else {
					Serializable status = (Serializable) view.getTag();
					Intent intent = new Intent(MentionsView.this.activity,
							WeiboDetailActivity.class);
					intent.putExtra("status", status);
					MentionsView.this.activity.startActivity(intent);
				}
			}

		});

		commentList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(final AdapterView<?> arg0, final View view,
					int arg2, long arg3) {
				if (view.getTag() == null) {
					progressDialog.show();
					new Thread() {
						public void run() {
							getMoreComments(Long.parseLong(((CommentItem) arg0
									.getItemAtPosition(arg0.getCount() - 2))
									.getId()));
							homeHandler
									.sendEmptyMessage(COMMENT_REFRESH_COMPLETE);
						}
					}.start();
				} else {
					new AlertDialog.Builder(MentionsView.this.activity)
							.setItems(new String[] { "回复评论", "查看原微博" },
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface arg0, int arg1) {
											if (arg1 == 0) {
												Serializable obj = (Serializable) view
														.getTag();
												Intent intent = new Intent(
														MentionsView.this.activity,
														ReplyActivity.class);
												intent.putExtra("obj", obj);
												MentionsView.this.activity
														.startActivity(intent);
											} else {
												CommentItem comment = (CommentItem) view
														.getTag();
												Intent intent = new Intent(
														MentionsView.this.activity,
														WeiboDetailActivity.class);
												intent.putExtra("status",
														comment.getStatus());
												MentionsView.this.activity
														.startActivity(intent);
											}
											arg0.dismiss();
										}

									}).show();
				}
			}
		});

	}

	private void getMoreComments(long maxId) {
		try {
			Comments comments = UserServiceImpl.getService().mentionComment(
					activity, 0, maxId - 1, MicroBlogType.Sina);
			CommentItem[] lt = comments.getItems();
			List<CommentItem> commentList = Arrays.asList(lt);
			if (commentList != null) {
				commentAdapter.add(commentList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getMoreStatuses(long maxId) {
		try {
			Statuses status = UserServiceImpl.getService().mentionStatus(
					activity, 0, maxId - 1, MicroBlogType.Sina);
			StatusItem[] lt = status.getItems();
			List<StatusItem> weiboList = Arrays.asList(lt);
			if (weiboList != null) {
				statusAdapter.add(weiboList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
