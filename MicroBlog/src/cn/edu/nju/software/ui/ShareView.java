package cn.edu.nju.software.ui;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.nju.software.service.user.impl.UserServiceImpl;

import com.weibo.android.R;
import com.weibo.net.WeiboException;
import com.weibo.net.AsyncWeiboRunner.RequestListener;

public class ShareView extends LinearLayout implements RequestListener {

	private TextView textView;
	private Button sendBtn;
	private ImageButton closeBtn;
	private EditText editText;
	private FrameLayout piclayout;
	private LinearLayout total;
	private ImageView picture;

	private String mPicPath = "";
	private String mContent = "";

	private OnClickListener clickListener = new ClickListener();

	private Activity activity;

	public static final int WEIBO_MAX_LENGTH = 140;

	public ShareView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init((Activity) context);
	}

	public ShareView(Context context) {
		super(context);
		init((Activity) context);
	}

	private void init(Activity activity) {
		this.activity = activity;
		LayoutInflater li = LayoutInflater.from(activity);
		addView(li.inflate(R.layout.share, null));

		closeBtn = (ImageButton) findViewById(R.id.btnClose);
		sendBtn = (Button) findViewById(R.id.btnSend);
		total = (LinearLayout) findViewById(R.id.ll_text_limit_unit);
		textView = (TextView) findViewById(R.id.tv_text_limit);
		editText = (EditText) findViewById(R.id.etEdit);
		picture = (ImageView) findViewById(R.id.ivDelPic);

		closeBtn.setOnClickListener(clickListener);
		sendBtn.setOnClickListener(clickListener);
		total.setOnClickListener(clickListener);
		picture.setOnClickListener(clickListener);
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
		editText.setText(mContent);
		piclayout = (FrameLayout) findViewById(R.id.flPic);
		if (TextUtils.isEmpty(this.mPicPath)) {
			piclayout.setVisibility(View.GONE);
		} else {
			piclayout.setVisibility(View.VISIBLE);
			File file = new File(mPicPath);
			if (file.exists()) {
				Bitmap pic = BitmapFactory.decodeFile(this.mPicPath);
				ImageView image = (ImageView) this.findViewById(R.id.ivImage);
				image.setImageBitmap(pic);
			} else {
				piclayout.setVisibility(View.GONE);
			}
		}
	}

	private class ClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			int viewId = v.getId();

			if (viewId == R.id.btnClose) {
				activity.finish();
			} else if (viewId == R.id.btnSend) {
				ShareView.this.mContent = editText.getText().toString();
				if (!TextUtils.isEmpty(mPicPath)) {
					try {
						UserServiceImpl.getService().share2weibo(activity,
								ShareView.this.mContent,
								ShareView.this.mPicPath);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					try {
						UserServiceImpl.getService().share2weibo(activity,
								ShareView.this.mContent, "");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else if (viewId == R.id.ll_text_limit_unit) {
				Dialog dialog = new AlertDialog.Builder(activity).setTitle(
						R.string.attention).setMessage("是否清除内容？")
						.setPositiveButton(R.string.ok,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										editText.setText("");
									}
								}).setNegativeButton(R.string.cancel, null)
						.create();
				dialog.show();
			} else if (viewId == R.id.ivDelPic) {
				Dialog dialog = new AlertDialog.Builder(activity).setTitle(
						R.string.attention).setMessage(R.string.del_pic)
						.setPositiveButton(R.string.ok,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										piclayout.setVisibility(View.GONE);
									}
								}).setNegativeButton(R.string.cancel, null)
						.create();
				dialog.show();
			}
		}
	}

	@Override
	public void onComplete(String response) {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(activity, R.string.send_sucess,
						Toast.LENGTH_LONG).show();
			}
		});

		activity.finish();
	}

	@Override
	public void onIOException(IOException e) {

	}

	@Override
	public void onError(final WeiboException e) {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(
						activity,
						String.format(activity.getString(R.string.send_failed)
								+ ":%s", e.getMessage()), Toast.LENGTH_LONG)
						.show();
			}
		});

	}

}
