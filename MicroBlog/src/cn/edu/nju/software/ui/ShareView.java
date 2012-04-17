package cn.edu.nju.software.ui;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.nju.software.service.user.impl.UserServiceImpl;
import cn.edu.nju.software.utils.FileUtils;
import cn.edu.nju.software.utils.InfoHelper;
import cn.edu.nju.software.utils.MediaUtils;
import cn.edu.nju.software.utils.Utils;

public class ShareView extends LinearLayout {

	private Button button = null;
	private Button imgChooseBtn = null;
	private ImageView imgView = null;
	private TextView wordCounterTextView = null;
	private EditText contentEditText = null;
	private ProgressDialog dialog = null;

	private Activity activity;

	public static final int REQUEST_CODE_GETIMAGE_BYSDCARD = 0;
	public static final int REQUEST_CODE_GETIMAGE_BYCAMERA = 1;
	private String thisLarge = null, theSmall = null;

	protected final String SDCARD_MNT = "/mnt/sdcard";
	protected final String SDCARD = "/sdcard";

	public ShareView(Context activity, AttributeSet attrs) {
		super(activity, attrs);
		init((Activity) activity);
	}

	public ShareView(Context activity) {
		super(activity);
		init((Activity) activity);
	}

	private void init(Activity activity) {
		this.activity = activity;
		LayoutInflater li = LayoutInflater.from(activity);
		addView(li.inflate(R.layout.share, null));

		button = (Button) findViewById(R.id.Send);
		imgChooseBtn = (Button) findViewById(R.id.add_cmamera_btn);
		imgView = (ImageView) findViewById(R.id.share_image);
		wordCounterTextView = (TextView) findViewById(R.id.remain_count);
		contentEditText = (EditText) findViewById(R.id.weibo_content);

		dialog = new ProgressDialog(activity);
		dialog.setMessage("分享中...");
		dialog.setIndeterminate(false);
		dialog.setCancelable(true);

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isChecked()) {
					dialog.show();

					Thread thread = new Thread(new UpdateStatusThread());
					thread.start();
				}
			}
		});

		imgChooseBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CharSequence[] items = { "手机相册", "手机拍照", "清除照片" };
				imageChooseItem(items);
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

		if (!Utils.isBlank(thisLarge)) {
			String imgName = FileUtils.getFileName(thisLarge);

			Bitmap bitmap = loadImgThumbnail(imgName,
					MediaStore.Images.Thumbnails.MICRO_KIND);
			if (bitmap != null) {
				imgView.setBackgroundDrawable(new BitmapDrawable(bitmap));
				imgView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.setAction(android.content.Intent.ACTION_VIEW);
						intent.setDataAndType(
								Uri.fromFile(new File(thisLarge)), "image/*");
						ShareView.this.activity.startActivity(intent);
					}
				});
			}
		}
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

	public void refresh(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_GETIMAGE_BYSDCARD) {
			if (resultCode != Activity.RESULT_OK) {
				return;
			}
			if (data == null)
				return;
			Uri thisUri = data.getData();
			String thePath = InfoHelper
					.getAbsolutePathFromNoStandardUri(thisUri);
			// 如果是标准Uri
			if (Utils.isBlank(thePath)) {
				thisLarge = getAbsoluteImagePath(thisUri);
			} else {
				thisLarge = thePath;
			}
			String attFormat = FileUtils.getFileFormat(thisLarge);
			if (!"photo".equals(MediaUtils.getContentType(attFormat))) {
				Toast.makeText(activity, "请选择图片文件！", Toast.LENGTH_SHORT).show();
				return;
			}
			String imgName = FileUtils.getFileName(thisLarge);
			Bitmap bitmap = loadImgThumbnail(imgName,
					MediaStore.Images.Thumbnails.MICRO_KIND);
			if (bitmap != null) {
				imgView.setBackgroundDrawable(new BitmapDrawable(bitmap));
			}
		}
		// 拍摄图片
		else if (requestCode == REQUEST_CODE_GETIMAGE_BYCAMERA) {
			if (resultCode != Activity.RESULT_OK) {
				return;
			}

			Bitmap bitmap = InfoHelper.getScaleBitmap(activity, theSmall);

			if (bitmap != null) {
				imgView.setBackgroundDrawable(new BitmapDrawable(bitmap));
			}
		}

		imgView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction(android.content.Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(new File(thisLarge)),
						"image/*");
				activity.startActivity(intent);
			}
		});
	}

	/**
	 * 操作选择
	 * 
	 * @param items
	 */
	public void imageChooseItem(CharSequence[] items) {
		AlertDialog imageDialog = new AlertDialog.Builder(activity).setTitle(
				"增加图片").setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				// 手机选图
				if (item == 0) {
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
					intent.setType("image/*");
					activity.startActivityForResult(intent,
							REQUEST_CODE_GETIMAGE_BYSDCARD);
				}
				// 拍照
				else if (item == 1) {
					Intent intent = new Intent(
							"android.media.action.IMAGE_CAPTURE");

					String camerName = InfoHelper.getFileName();
					String fileName = "Share" + camerName + ".tmp";

					File camerFile = new File(InfoHelper.getCamerPath(),
							fileName);

					theSmall = InfoHelper.getCamerPath() + fileName;
					thisLarge = getLatestImage();

					Uri originalUri = Uri.fromFile(camerFile);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, originalUri);
					activity.startActivityForResult(intent,
							REQUEST_CODE_GETIMAGE_BYCAMERA);
				} else if (item == 2) {
					thisLarge = null;
					imgView.setBackgroundDrawable(null);
				}
			}
		}).create();

		imageDialog.show();
	}

	/**
	 * 数据合法性判断
	 * 
	 * @return
	 */
	private boolean isChecked() {
		boolean ret = true;
		if (Utils.isBlank(contentEditText.getText().toString())) {
			Toast.makeText(activity, "说点什么吧", Toast.LENGTH_SHORT).show();
			ret = false;
		} else if (contentEditText.getText().toString().length() > 140) {
			int currentLength = contentEditText.getText().toString().length();

			Toast.makeText(activity, "已超出" + (currentLength - 140) + "字",
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

			thisLarge = null;
			contentEditText.setText("");
			imgView.setBackgroundDrawable(null);

			if (msg.what > 0) {
				Toast.makeText(activity, "微博分享成功", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(activity, "微博分享失败", Toast.LENGTH_SHORT).show();
			}
		}
	};

	// 分享线程
	class UpdateStatusThread implements Runnable {
		public void run() {
			int what = -1;
			try {
				String msg = contentEditText.getText().toString();
				UserServiceImpl.getService().share2weibo(
						ShareView.this.activity, msg, thisLarge);
				what = 1;
			} catch (Exception e) {
				e.printStackTrace();
				Log.e("WeiboPub", e.getMessage());
			}
			handle.sendEmptyMessage(what);
		}
	}

	/**
	 * 通过uri获取文件的绝对路径
	 * 
	 * @param uri
	 * @return
	 */
	protected String getAbsoluteImagePath(Uri uri) {
		String imagePath = "";
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = activity.managedQuery(uri, proj, // Which columns to
				// return
				null, // WHERE clause; which rows to return (all rows)
				null, // WHERE clause selection arguments (none)
				null); // Order-by clause (ascending by name)

		if (cursor != null) {
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			if (cursor.getCount() > 0 && cursor.moveToFirst()) {
				imagePath = cursor.getString(column_index);
			}
		}

		return imagePath;
	}

	/**
	 * 获取图片缩略图 只有Android2.1以上版本支持
	 * 
	 * @param imgName
	 * @param kind
	 *            MediaStore.Images.Thumbnails.MICRO_KIND
	 * @return
	 */
	protected Bitmap loadImgThumbnail(String imgName, int kind) {
		Bitmap bitmap = null;

		String[] proj = { MediaStore.Images.Media._ID,
				MediaStore.Images.Media.DISPLAY_NAME };

		Cursor cursor = activity.managedQuery(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, proj,
				MediaStore.Images.Media.DISPLAY_NAME + "='" + imgName + "'",
				null, null);

		if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
			ContentResolver crThumb = activity.getContentResolver();
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 1;
			bitmap = MediaStore.Images.Thumbnails.getThumbnail(crThumb, cursor
					.getInt(0), kind, options);
		}
		return bitmap;
	}

	/**
	 * 获取SD卡中最新图片路径
	 * 
	 * @return
	 */
	protected String getLatestImage() {
		String latestImage = null;
		String[] items = { MediaStore.Images.Media._ID,
				MediaStore.Images.Media.DATA };
		Cursor cursor = activity.managedQuery(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, items, null,
				null, MediaStore.Images.Media._ID + " desc");

		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			for (cursor.moveToFirst(); !cursor.isAfterLast();) {
				latestImage = cursor.getString(1);
				break;
			}
		}

		return latestImage;
	}

}