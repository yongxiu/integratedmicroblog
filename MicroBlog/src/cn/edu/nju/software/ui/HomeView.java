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
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void textHighlight(TextView textView, String start, String end) {
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
				adapter.notifyDataSetChanged();
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
			convertView = inflater.inflate(R.layout.status, null);
			WeiBoHolder wh = new WeiBoHolder();
			wh.wbicon = (ImageView) convertView.findViewById(R.id.wbicon);
			wh.wbuser = (TextView) convertView.findViewById(R.id.wbuser);
			wh.wbtime = (TextView) convertView.findViewById(R.id.wbtime);
			wh.wbcontent = (TextView) convertView.findViewById(R.id.wbtext);

			StatusItem wb = weiboList.get(position);
			if (wb != null) {
				convertView.setTag(wb.getId());
				wh.wbuser.setText(wb.getUserName());
				wh.wbtime.setText(wb.getCreatedTime());
				wh.wbcontent.setText(wb.getContent(),
						TextView.BufferType.SPANNABLE);
				textHighlight(wh.wbcontent, "http://", " ");
				// 头像
				asyncImageLoader.loadDrawable(wb.getUserIcon(), wh.wbicon,
						new ImageCallback() {
							@Override
							public void imageLoaded(Drawable imageDrawable,
									ImageView imageView) {
								imageView.setImageDrawable(imageDrawable);
							}
						});
				// 转发微博的原微博
				StatusItem retweetedstatus = wb.getRetweetedStatus();
				if (retweetedstatus == null) {
					if (wb.isHaveImage()) {
						wh.wbimage = (ImageView) convertView
								.findViewById(R.id.wbimage);
						wh.wbimage.setVisibility(VISIBLE);
						asyncImageLoader.loadDrawable(wb.getImgPath(),
								wh.wbimage, new ImageCallback() {
									@Override
									public void imageLoaded(
											Drawable imageDrawable,
											ImageView imageView) {
										imageView
												.setImageDrawable(imageDrawable);
									}
								});
					}
				} else {
					wh.source = (LinearLayout) convertView
							.findViewById(R.id.RetweetedStatus);
					wh.source.setVisibility(VISIBLE);
					TextView sourceText = (TextView) wh.source
							.findViewById(R.id.sourceText);
					sourceText.setText("@" + retweetedstatus.getUserName()
							+ ":" + retweetedstatus.getContent(),
							TextView.BufferType.SPANNABLE);
					textHighlight(sourceText, "http://", " ");
					if (wb.isHaveImage()) {
						wh.wbimage = (ImageView) wh.source
								.findViewById(R.id.sourceImage);
						wh.wbimage.setVisibility(VISIBLE);
						asyncImageLoader.loadDrawable(wb.getImgPath(),
								wh.wbimage, new ImageCallback() {
									@Override
									public void imageLoaded(
											Drawable imageDrawable,
											ImageView imageView) {
										imageView
												.setImageDrawable(imageDrawable);
									}
								});
					}

				}

			}

			return convertView;
			// try {
			// String origtext = data.getString("origtext");
			//					
			// SpannableString spannable = new SpannableString(origtext);
			//					
			//					
			// /*spannable = TextUtil.decorateFaceInStr(spannable,
			// RegexUtil.getStartAndEndIndex(data.getString("origtext"),
			// Pattern.compile("\\/[\u4e00-\u9fa5a-zA-Z]{1,3}")),
			// getResources());//解析成本地表情
			// spannable = TextUtil.decorateRefersInStr(spannable,
			// RegexUtil.getStartAndEndIndex(origtext, Pattern.compile("@.*:")),
			// getResources());//高亮显示微薄转发，回复者昵称
			// spannable = TextUtil.decorateTopicInStr(spannable,
			// RegexUtil.getStartAndEndIndex(origtext, Pattern.compile("#.*#")),
			// getResources());//高亮显示话题名称
			// spannable = TextUtil.decorateTopicInStr(spannable,
			// RegexUtil.getStartAndEndIndex(origtext,
			// Pattern.compile("^http://\\w+(\\.\\w+|)+(/\\w+)*(/\\w+\\.(\\w+|))?")),
			// getResources());//高亮显示url地址
			// */
			// viewHolder.home_origtext.setText(spannable);
			// //微博内容设置结束
			//					
			//					
			// //处理引用的转播，评论的微博内容

			// SpannableString spannableSource = new
			// SpannableString(home_source_text);
			// spannableSource = TextUtil.decorateRefersInStr(spannableSource,
			// RegexUtil.getStartAndEndIndex(home_source_text,
			// Pattern.compile("@.*:")), getResources());
			// spannableSource = TextUtil.decorateTopicInStr(spannableSource,
			// RegexUtil.getStartAndEndIndex(home_source_text,
			// Pattern.compile("#.*#")), getResources());
			// spannableSource = TextUtil.decorateTopicInStr(spannableSource,
			// RegexUtil.getStartAndEndIndex(home_source_text,
			// Pattern.compile("^http://\\w+(\\.\\w+|)+(/\\w+)*(/\\w+\\.(\\w+|))?")),
			// getResources());
		}
	}

}
