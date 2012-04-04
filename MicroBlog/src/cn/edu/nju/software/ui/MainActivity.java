package cn.edu.nju.software.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import cn.edu.nju.software.utils.AndroidHelper;

public class MainActivity extends Activity {

	private Context mContext;
	public static MainActivity webInstance = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
		// 背景自动适应
		AndroidHelper.AutoBackground(this, layout, R.drawable.bg_v, R.drawable.bg_h);

		webInstance = this;
		mContext = getApplicationContext();

	}

	@Override
	protected void onResume() {
		super.onResume();

			Intent intent = new Intent();
			intent.setClass(mContext, AuthorizeActivity.class);
			startActivity(intent);
		
	}

}