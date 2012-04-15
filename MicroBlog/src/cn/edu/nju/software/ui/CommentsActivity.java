package cn.edu.nju.software.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import cn.edu.nju.software.model.CommentItem;
import cn.edu.nju.software.model.StatusItem;
import cn.edu.nju.software.service.user.impl.UserServiceImpl;
import cn.edu.nju.software.utils.Utils;

public class CommentsActivity extends Activity {

	private ImageButton backBtn;
	private Button replyBtn;
	private ListView cmtsList;

	private CommentsAdapter adapter;

	private ProgressDialog progressDialog;
	private static final int REFRESH_COMPLETE = 0;

	private Handler homeHandler = new HomeHandler();

	private StatusItem status;

	public void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.comments);

		backBtn = (ImageButton) findViewById(R.id.Back);
		replyBtn = (Button) findViewById(R.id.Reply);
		cmtsList = (ListView) findViewById(R.id.Cmtslist);
		backBtn.setOnClickListener(new BackBtnListener());
		replyBtn.setOnClickListener(new ReplyBtnListener());

		progressDialog = new ProgressDialog(this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setTitle("请稍等");
		progressDialog.setMessage("正在读取数据中!");

		Object obj = getIntent().getSerializableExtra("obj");
		if (obj != null && obj instanceof StatusItem) {
			status = (StatusItem) obj;
		}

		adapter = new CommentsAdapter(this);
		cmtsList.setAdapter(adapter);

		progressDialog.show();
		new Thread() {
			public void run() {
				refresh();
				homeHandler.sendEmptyMessage(REFRESH_COMPLETE);
			}
		}.start();
	}

	private void refresh() {
		try {
			CommentItem[] comments = UserServiceImpl.getService().getComment(
					CommentsActivity.this, status.getId(), "0", "0",
					status.getMicroBlogType());
			List<CommentItem> commentList = Arrays.asList(comments);
			if (commentList != null) {
				adapter.refresh(commentList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class BackBtnListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			CommentsActivity.this.finish();
		}

	}

	private class ReplyBtnListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(CommentsActivity.this,
					ReplyActivity.class);
			intent.putExtra("obj", status);
			CommentsActivity.this.startActivity(intent);
		}

	}

	private class HomeHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case REFRESH_COMPLETE:
				progressDialog.dismiss();
				adapter.notifyDataSetChanged();
				break;
			}
		}

	}

	private void getMore(long maxId) {

	}

	private class CommentsAdapter extends BaseAdapter {

		private boolean hasMore;
		private List<CommentItem> commentList = new ArrayList<CommentItem>();
		private LayoutInflater inflater;

		public CommentsAdapter(Activity activity) {
			this.inflater = LayoutInflater.from(activity);
		}

		public CommentsAdapter(List<CommentItem> weibolist, Activity activity) {
			this(activity);
			this.commentList.addAll(weibolist);

		}

		public void refresh(List<CommentItem> list) {
			commentList.clear();
			commentList.addAll(list);
			hasMore = list.size() != 0;
		}

		public void add(List<CommentItem> list) {
			hasMore = list.size() != 0;
			commentList.addAll(list);
		}

		@Override
		public int getCount() {
			return hasMore ? commentList.size() + 1 : commentList.size();
		}

		@Override
		public Object getItem(int position) {
			return position < commentList.size() ? commentList.get(position)
					: null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (position < commentList.size()) {
				convertView = inflater.inflate(R.layout.comment, null);
				TextView wbuser = (TextView) convertView
						.findViewById(R.id.wbuser);
				TextView wbtime = (TextView) convertView
						.findViewById(R.id.wbtime);
				TextView wbtext = (TextView) convertView
						.findViewById(R.id.wbtext);
				TextView wbfrom = (TextView) convertView
						.findViewById(R.id.status_from);

				CommentItem wb = commentList.get(position);
				convertView.setTag(wb);

				wbuser.setText(wb.getUserName());
				wbtime
						.setText(Utils
								.ConvertTime(new Date(wb.getCreatedTime())));
				wbtext.setText(wb.getContent(), TextView.BufferType.SPANNABLE);
				Utils.textHighlight(wbtext, "http://", " ");
				wbfrom.setText("来自：" + wb.getMicroBlogType());
			} else {
				convertView = inflater.inflate(R.layout.more, null);
			}

			return convertView;
		}

	}
}
