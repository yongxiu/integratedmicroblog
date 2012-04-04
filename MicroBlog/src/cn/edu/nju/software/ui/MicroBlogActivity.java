package cn.edu.nju.software.ui;

import android.app.Activity;
import android.os.Bundle;
import cn.edu.nju.software.service.user.impl.UserServiceImpl;
import cn.edu.nju.software.utils.MicroBlogType;

public class MicroBlogActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		try {
			UserServiceImpl.getService().login(this, MicroBlogType.Sina);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}