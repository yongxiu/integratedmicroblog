package cn.edu.nju.software.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.edu.nju.software.model.CommentItem;
import cn.edu.nju.software.model.StatusItem;
import cn.edu.nju.software.service.user.impl.UserServiceImpl;

import com.weibo.android.R;

public class ReplyActivity extends Activity {

	private static final int WEIBO_MAX_LENGTH = 140;

	private Button sendBtn;
	private ImageButton closeBtn;

	private EditText editText;
	private TextView textView;
	private LinearLayout total;

	private ProgressDialog progressDialog;

	private Handler mHandler = new MHandler();

	private static final int SEND_COMPLETE = 0;

	private OnClickListener clickListener = new ClickListener();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addcomment);

		progressDialog = new ProgressDialog(this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setTitle("请稍等");
		progressDialog.setMessage("正在发送!");

		sendBtn = (Button) findViewById(R.add.btnSend);
		closeBtn = (ImageButton) findViewById(R.add.btnClose);
		total = (LinearLayout) findViewById(R.add.ll_text_limit_unit);
		textView = (TextView) findViewById(R.add.tv_text_limit);

		sendBtn.setOnClickListener(clickListener);
		closeBtn.setOnClickListener(clickListener);
		total.setOnClickListener(clickListener);

		editText = (EditText) findViewById(R.add.etEdit);
		editText.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String mText = editText.getText().toString();
				int len = mText.length();
				if (len <= WEIBO_MAX_LENGTH) {
					len = WEIBO_MAX_LENGTH - len;
					textView.setTextColor(R.color.text_num_gray);
					if (!sendBtn.isEnabled())
						sendBtn.setEnabled(true);
				} else {
					len = len - WEIBO_MAX_LENGTH;

					textView.setTextColor(Color.RED);
					if (sendBtn.isEnabled())
						sendBtn.setEnabled(false);
				}
				textView.setText(String.valueOf(len));
			}
		});

	}

	private class ClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			int viewId = v.getId();
			if (viewId == R.add.btnClose) {
				finish();
			} else if (viewId == R.add.btnSend) {
				progressDialog.show();
				new Thread() {
					public void run() {
						Object obj = ReplyActivity.this.getIntent()
								.getSerializableExtra("obj");
						if (obj instanceof StatusItem) {
							try {
								StatusItem status = (StatusItem) obj;
								UserServiceImpl.getService().replyStatus(
										ReplyActivity.this,
										Long.parseLong(status.getId()),
										editText.getText().toString(),
										status.getMicroBlogType());
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							CommentItem comment = (CommentItem) obj;
							try {
								UserServiceImpl.getService().replyComment(
										ReplyActivity.this,
										Long.parseLong(comment.getStatus()
												.getId()),
										Long.parseLong(comment.getId()),
										editText.getText().toString(),
										comment.getMicroBlogType());
							} catch (NumberFormatException e) {
								e.printStackTrace();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						ReplyActivity.this.mHandler
								.sendEmptyMessage(SEND_COMPLETE);
					}
				}.start();
			} else if (viewId == R.add.ll_text_limit_unit) {
				Dialog dialog = new AlertDialog.Builder(ReplyActivity.this)
						.setTitle(R.string.attention).setMessage("是否清空内容？")
						.setPositiveButton(R.string.ok,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										editText.setText("");
									}
								}).setNegativeButton(R.string.cancel, null)
						.create();
				dialog.show();
			}
		}
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	private class MHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SEND_COMPLETE:
				progressDialog.dismiss();
				finish();
				break;
			}
		}
	}

}