package cn.edu.nju.software.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.edu.nju.software.model.StatusItem;
import cn.edu.nju.software.utils.AsyncImageLoader;
import cn.edu.nju.software.utils.Utils;
import cn.edu.nju.software.utils.WeiBoHolder;

public class WeiBoAdapter extends BaseAdapter {

	private boolean hasMore;
	private List<StatusItem> weiboList = new ArrayList<StatusItem>();
	private AsyncImageLoader asyncImageLoader;
	private LayoutInflater inflater;

	public WeiBoAdapter(Activity activity) {
		this.asyncImageLoader = new AsyncImageLoader();
		this.inflater = LayoutInflater.from(activity);
	}

	public WeiBoAdapter(List<StatusItem> weibolist, Activity activity) {
		this(activity);
		this.weiboList.addAll(weibolist);

	}

	public void refresh(List<StatusItem> list) {
		weiboList.clear();
		weiboList.addAll(list);
		hasMore = list.size() != 0;
	}

	public void add(List<StatusItem> list) {
		hasMore = list.size() != 0;
		weiboList.addAll(list);
	}

	@Override
	public int getCount() {
		return hasMore ? weiboList.size() + 1 : weiboList.size();
	}

	@Override
	public Object getItem(int position) {
		return position < weiboList.size() ? weiboList.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (position < weiboList.size()) {
			convertView = inflater.inflate(R.layout.status, null);
			WeiBoHolder wh = new WeiBoHolder();
			wh.wbicon = (ImageView) convertView.findViewById(R.id.wbicon);
			wh.wbuser = (TextView) convertView.findViewById(R.id.wbuser);
			wh.wbtime = (TextView) convertView.findViewById(R.id.wbtime);
			wh.wbcontent = (TextView) convertView.findViewById(R.id.wbtext);
			wh.fromView = (TextView) convertView.findViewById(R.id.status_from);

			StatusItem wb = weiboList.get(position);
			convertView.setTag(wb);
			wh.wbuser.setText(wb.getUserName());
			wh.wbtime.setText(Utils.ConvertTime(wb.getDate()));
			wh.wbcontent
					.setText(wb.getContent(), TextView.BufferType.SPANNABLE);
			Utils.textHighlight(wh.wbcontent, "http://", " ");
			// 头像
			asyncImageLoader.loadDrawable(wb.getUserIcon(), wh.wbicon);
			// 转发微博的原微博
			StatusItem retweetedstatus = wb.getRetweetedStatus();
			String path = null;
			if (retweetedstatus != null) {
				wh.source = (LinearLayout) convertView
						.findViewById(R.id.RetweetedStatus);
				wh.source.setVisibility(View.VISIBLE);
				TextView sourceText = (TextView) wh.source
						.findViewById(R.id.sourceText);
				sourceText.setText("@" + retweetedstatus.getUserName() + ":"
						+ retweetedstatus.getContent(),
						TextView.BufferType.SPANNABLE);
				Utils.textHighlight(sourceText, "http://", " ");
				if ((path = retweetedstatus.getImgPath()) != null)
					wh.wbimage = (ImageView) wh.source
							.findViewById(R.id.sourceImage);
			}
			if (path == null && (path = wb.getImgPath()) != null
					&& !path.equals(""))
				wh.wbimage = (ImageView) convertView.findViewById(R.id.wbimage);
			if (path != null && !path.equals("")) {
				wh.wbimage.setVisibility(View.VISIBLE);
				asyncImageLoader.loadDrawable(path, wh.wbimage);
			}
			wh.fromView.setText("来自：" + wb.getMicroBlogType());
		} else {
			convertView = inflater.inflate(R.layout.more, null);
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