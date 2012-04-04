package cn.edu.nju.software.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import cn.edu.nju.software.model.StatusItem;
import cn.edu.nju.software.model.Statuses;
import cn.edu.nju.software.service.user.impl.UserServiceImpl;
import cn.edu.nju.software.utils.AsyncImageLoader;
import cn.edu.nju.software.utils.MicroBlogType;
import cn.edu.nju.software.utils.WeiBoHolder;
import cn.edu.nju.software.utils.AsyncImageLoader.ImageCallback;

public class HomeActivity extends Activity {

	private LinearLayout loadingLayout;
	private ImageButton writeBtn;

	public void onCreate(Bundle savedInstanceState) {
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		loadingLayout = (LinearLayout) findViewById(R.id.loadingLayout);
		writeBtn = (ImageButton) findViewById(R.id.writeBtn);

		writeBtn.setOnClickListener(new WriteBtnListener());
		loadList();
	}

	private List<StatusItem> wbList;

	private void loadList() {

		// 显示当前用户名称
		TextView showName = (TextView) findViewById(R.id.showName);
		// showName.setText(user.getUserName());

		// try {
		try {
			Statuses sts = UserServiceImpl.getService().getFriendsTimeline(
					this, MicroBlogType.Sina, 0, 0);

			StatusItem[] lt = sts.getItems();
			wbList = new ArrayList<StatusItem>();
			for (int i = 0; i < lt.length; i++) {
				wbList.add(lt[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (wbList != null) {
			WeiBoAdapater adapater = new WeiBoAdapater();
			ListView Msglist = (ListView) findViewById(R.id.Msglist);
			Msglist.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View view,
						int arg2, long arg3) {
					Object obj = view.getTag();
					if (obj != null) {
						String id = obj.toString();
						Intent intent = new Intent(HomeActivity.this,
								ViewActivity.class);
						Bundle b = new Bundle();
						b.putString("key", id);
						intent.putExtras(b);
						startActivity(intent);
					}
				}

			});
			Msglist.setAdapter(adapater);
		}

		loadingLayout.setVisibility(View.GONE);
	}

	class WriteBtnListener implements OnClickListener {

		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(HomeActivity.this, ShareActivity.class);
			startActivity(intent);
		}
	}

	private void textHighlight(TextView textView, char[] start, char[] end) {

	}

	private void textHighlight2(TextView textView, String start, String end) {
		Spannable sp = (Spannable) textView.getText();
		String text = textView.getText().toString();
		int n = 0;
		int s = -1;
		int e = -1;
		while (n < text.length()) {
			s = text.indexOf(start, n);
			if (s != -1) {
				e = text.indexOf(end, s + start.length());
				if (e != -1) {
					e = e + end.length();
				} else {
					e = text.length();
				}
				n = e;
				sp.setSpan(new ForegroundColorSpan(Color.BLUE), s, e,
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				s = e = -1;
			} else {
				n = text.length();
			}
		}
	}

	// 微博列表Adapater
	public class WeiBoAdapater extends BaseAdapter {

		private AsyncImageLoader asyncImageLoader;

		@Override
		public int getCount() {
			return wbList.size();
		}

		@Override
		public Object getItem(int position) {
			return wbList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			asyncImageLoader = new AsyncImageLoader();
			convertView = LayoutInflater.from(getApplicationContext()).inflate(
					R.layout.weibo, null);
			WeiBoHolder wh = new WeiBoHolder();
			wh.wbicon = (ImageView) convertView.findViewById(R.id.wbicon);
			wh.wbtext = (TextView) convertView.findViewById(R.id.wbtext);
			wh.wbtime = (TextView) convertView.findViewById(R.id.wbtime);
			wh.wbuser = (TextView) convertView.findViewById(R.id.wbuser);
			wh.wbimage = (ImageView) convertView.findViewById(R.id.wbimage);
			StatusItem wb = wbList.get(position);
			if (wb != null) {
				convertView.setTag(wb.getId());
				wh.wbuser.setText(wb.getUserName());
				wh.wbtime.setText(wb.getCreatedTime());
				wh.wbtext.setText(wb.getContent(),
						TextView.BufferType.SPANNABLE);
				textHighlight(wh.wbtext, new char[] { '#' }, new char[] { '#' });
				textHighlight(wh.wbtext, new char[] { '@' }, new char[] { ':',
						' ' });
				textHighlight2(wh.wbtext, "http://", " ");

				if (wb.isHaveImage()) {
					wh.wbimage.setImageResource(R.drawable.images);
				}
				Drawable cachedImage = asyncImageLoader.loadDrawable(wb
						.getUserIcon(), wh.wbicon, new ImageCallback() {

					@Override
					public void imageLoaded(Drawable imageDrawable,
							ImageView imageView, String imageUrl) {
						imageView.setImageDrawable(imageDrawable);
					}

				});
				if (cachedImage == null) {
					wh.wbicon.setImageResource(R.drawable.usericon);
				} else {
					wh.wbicon.setImageDrawable(cachedImage);
				}
			}

			return convertView;
		}
	}

}
