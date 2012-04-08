package cn.edu.nju.software.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class HomeView extends LinearLayout {

	private ListView msgList;
	private ImageButton refreshBtn;
	private Activity activity;

	private WeiBoAdapter adapter;

	private static final int REFRESH_COMPLETE = 0;

	private Handler homeHandler = new HomeHandler();

	public HomeView(Context activity, AttributeSet attrs) {
		super(activity, attrs);
		init((Activity) activity);
	}

	public HomeView(Context activity) {
		super(activity);
		init((Activity) activity);
	}

	private void init(Activity activity) {
		this.activity = activity;
		LayoutInflater li = LayoutInflater.from(activity);
		addView(li.inflate(R.layout.home, null));

		msgList = (ListView) findViewById(R.id.Msglist);
		refreshBtn = (ImageButton) findViewById(R.id.StatusRefresh);

		adapter = new WeiBoAdapter();
		msgList.setAdapter(adapter);

		msgList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
				Object obj = view.getTag();
				if (obj != null) {
					String id = obj.toString();
				}
			}

		});

		refreshBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				refreshBtn.setEnabled(false);
				new Thread() {
					public void run() {
						getMore();
						homeHandler.sendEmptyMessage(REFRESH_COMPLETE);
					}
				}.start();
			}

		});

	}

	private void refresh() {

	}

	private void getMore() {
		try {
			Statuses status = UserServiceImpl.getService().getFriendsTimeline(
					activity, 0, 0, MicroBlogType.Sina);
			StatusItem[] lt = status.getItems();
			List<StatusItem> weiboList = Arrays.asList(lt);
			if (weiboList != null) {
				adapter.add(weiboList);
				adapter.notifyDataSetChanged();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	private class HomeHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case REFRESH_COMPLETE:
				refreshBtn.setEnabled(true);
				break;
			}
		}

	}

	public class WeiBoAdapter extends BaseAdapter {

		private List<StatusItem> weiboList = new ArrayList<StatusItem>();
		private AsyncImageLoader asyncImageLoader;
		private LayoutInflater inflater;

		public WeiBoAdapter() {
			this.asyncImageLoader = new AsyncImageLoader();
			this.inflater = LayoutInflater.from(activity);
		}

		public WeiBoAdapter(List<StatusItem> weibolist) {
			this();
			this.weiboList.addAll(weibolist);

		}

		public void refresh(List<StatusItem> list) {
			weiboList.clear();
			weiboList.addAll(list);
		}

		public void add(List<StatusItem> list) {
			weiboList.addAll(list);
		}

		@Override
		public int getCount() {
			return weiboList.size();
		}

		@Override
		public Object getItem(int position) {
			return weiboList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = inflater.inflate(R.layout.weibo, null);
			WeiBoHolder wh = new WeiBoHolder();
			wh.wbicon = (ImageView) convertView.findViewById(R.id.wbicon);
			wh.wbtext = (TextView) convertView.findViewById(R.id.wbtext);
			wh.wbtime = (TextView) convertView.findViewById(R.id.wbtime);
			wh.wbuser = (TextView) convertView.findViewById(R.id.wbuser);
			wh.wbimage = (ImageView) convertView.findViewById(R.id.wbimage);
			StatusItem wb = weiboList.get(position);
			if (wb != null) {
				convertView.setTag(wb.getId());
				wh.wbuser.setText(wb.getUserName());
				wh.wbtime.setText(wb.getCreatedTime());
				wh.wbtext.setText(wb.getContent(),
						TextView.BufferType.SPANNABLE);
				// textHighlight(wh.wbtext, new char[] { '#' }, new char[] { '#'
				// });
				// textHighlight(wh.wbtext, new char[] { '@' }, new char[] {
				// ':',
				// ' ' });
				// textHighlight2(wh.wbtext, "http://", " ");

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
