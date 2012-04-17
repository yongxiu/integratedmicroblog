package cn.edu.nju.software.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.nju.software.model.StatusItem;
import cn.edu.nju.software.service.user.impl.UserServiceImpl;
import cn.edu.nju.software.utils.Utils;

public class WriteActivity extends Activity {

	private Button sendBtn;
	private ImageButton backBtn;
	private TextView wordCounterTextView;
	private EditText contentEditText;
	private CheckBox checkBox;

	private ProgressDialog dialog;

	private StatusItem status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.write);

		sendBtn = (Button) findViewById(R.id.Send);
		wordCounterTextView = (TextView) findViewById(R.id.remain_count);
		contentEditText = (EditText) findViewById(R.id.weibo_content);
		backBtn = (ImageButton) findViewById(R.id.Back);
		checkBox = (CheckBox) findViewById(R.id.checkbox);

		backBtn.setOnClickListener(new BackBtnListener());

		status = (StatusItem) getIntent().getSerializableExtra("status");

		contentEditText.setText("//" + status.getUserName() + ":"
				+ status.getContent());
		textCountSet();

		dialog = new ProgressDialog(this);
		dialog.setMessage("分享中...");
		dialog.setIndeterminate(false);
		dialog.setCancelable(true);

		sendBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isChecked()) {
					dialog.show();

					Thread thread = new Thread(new UpdateStatusThread());
					thread.start();
				}
			}
		});

		// 侦听EditText字数改变
		TextWatcher watcher = new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				textCountSet();
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				textCountSet();
			}

			@Override
			public void afterTextChanged(Editable s) {
				textCountSet();
			}
		};

		contentEditText.addTextChangedListener(watcher);
	}

	/**
	 * 设置稿件字数
	 */
	private void textCountSet() {
		String textContent = contentEditText.getText().toString();
		int currentLength = textContent.length();
		if (currentLength <= 140) {
			wordCounterTextView.setTextColor(Color.BLACK);
			wordCounterTextView.setText(String.valueOf(140 - textContent
					.length()));
		} else {
			wordCounterTextView.setTextColor(Color.RED);
			wordCounterTextView.setText(String.valueOf(140 - currentLength));
		}
	}

	/**
	 * 数据合法性判断
	 * 
	 * @return
	 */
	private boolean isChecked() {
		boolean ret = true;
		if (Utils.isBlank(contentEditText.getText().toString())) {
			Toast.makeText(this, "说点什么吧", Toast.LENGTH_SHORT).show();
			ret = false;
		} else if (contentEditText.getText().toString().length() > 140) {
			int currentLength = contentEditText.getText().toString().length();

			Toast.makeText(this, "已超出" + (currentLength - 140) + "字",
					Toast.LENGTH_SHORT).show();
			ret = false;
		}
		return ret;
	}

	Handler handle = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (dialog != null) {
				dialog.dismiss();
			}
			contentEditText.setText("");

			if (msg.what > 0) {
				Toast
						.makeText(WriteActivity.this, "微博分享成功",
								Toast.LENGTH_SHORT).show();
			} else {
				Toast
						.makeText(WriteActivity.this, "微博分享失败",
								Toast.LENGTH_SHORT).show();
			}
		}
	};

	Handler endSessionHandle = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			finish();
		}
	};

	// 分享线程
	class UpdateStatusThread implements Runnable {
		public void run() {
			int what = -1;
			try {
				String msg = contentEditText.getText().toString();
				UserServiceImpl.getService()
						.repost(WriteActivity.this,
								Long.parseLong(status.getId()), msg,
								checkBox.isChecked() ? 1 : 0,
								status.getMicroBlogType());
				what = 1;
			} catch (Exception e) {
				e.printStackTrace();
				Log.e("WeiboPub", e.getMessage());
			}
			handle.sendEmptyMessage(what);
		}
	}

	private class BackBtnListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			WriteActivity.this.finish();
		}

	}
}
