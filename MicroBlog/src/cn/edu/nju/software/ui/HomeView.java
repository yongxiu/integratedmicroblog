package cn.edu.nju.software.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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

	private LinearLayout loadingLayout;
	private ListView msgList;
	private Activity activity;

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

		loadingLayout = (LinearLayout) findViewById(R.id.loadingLayout);
		msgList = (ListView) findViewById(R.id.Msglist);
		loadList();
	}

	private void loadList() {
		try {
			Statuses status = UserServiceImpl.getService().getFriendsTimeline(
					activity, 0, 0, MicroBlogType.Sina);
			StatusItem[] lt = status.getItems();
			List<StatusItem> weiboList = Arrays.asList(lt);
			if (weiboList != null) {
				WeiBoAdapter adapater = new WeiBoAdapter(weiboList);

				msgList.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View view,
							int arg2, long arg3) {
						Object obj = view.getTag();
						if (obj != null) {
							String id = obj.toString();
						}
					}

				});
				msgList.setAdapter(adapater);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// loadingLayout.setVisibility(View.GONE);
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

	public class WeiBoAdapter extends BaseAdapter {

		private List<StatusItem> weiboList = new ArrayList<StatusItem>();
		private AsyncImageLoader asyncImageLoader;

		public WeiBoAdapter(List<StatusItem> weibolist) {
			this.weiboList.addAll(weibolist);
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
			asyncImageLoader = new AsyncImageLoader();
			convertView = LayoutInflater.from(activity).inflate(R.layout.weibo,
					null);
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
