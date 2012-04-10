package cn.edu.nju.software.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.edu.nju.software.model.CommentItem;
import cn.edu.nju.software.model.StatusItem;
import cn.edu.nju.software.utils.AsyncImageLoader;
import cn.edu.nju.software.utils.AtCommentHolder;
import cn.edu.nju.software.utils.Utils;

public class AtCommentAdapter extends BaseAdapter {

	private List<CommentItem> commentList = new ArrayList<CommentItem>();
	private AsyncImageLoader asyncImageLoader;
	private LayoutInflater inflater;

	public AtCommentAdapter(Activity activity) {
		this.asyncImageLoader = new AsyncImageLoader();
		this.inflater = LayoutInflater.from(activity);
	}

	public AtCommentAdapter(List<CommentItem> weibolist, Activity activity) {
		this(activity);
		this.commentList.addAll(weibolist);

	}

	public void refresh(List<CommentItem> list) {
		commentList.clear();
		commentList.addAll(list);
	}

	public void add(List<CommentItem> list) {
		commentList.addAll(list);
	}

	@Override
	public int getCount() {
		return commentList.size();
	}

	@Override
	public Object getItem(int position) {
		return commentList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.atcomment, null);
		AtCommentHolder ch = new AtCommentHolder();
		ch.commenticon = (ImageView) convertView.findViewById(R.id.commenticon);
		ch.commentuser = (TextView) convertView.findViewById(R.id.commentuser);
		ch.commenttime = (TextView) convertView.findViewById(R.id.commenttime);
		ch.commentcontent = (TextView) convertView
				.findViewById(R.id.commenttext);

		CommentItem comment = commentList.get(position);
		if (comment != null) {
			convertView.setTag(comment);
			ch.commentuser.setText(comment.getUserName());
			ch.commenttime.setText(comment.getCreatedTime());
			ch.commentcontent.setText(comment.getContent(),
					TextView.BufferType.SPANNABLE);
			Utils.textHighlight(ch.commentcontent, "http://", " ");
			// 头像
			asyncImageLoader
					.loadDrawable(comment.getUserIcon(), ch.commenticon);
			// 回复评论
			CommentItem reply_comment = comment.getReply();
			if (reply_comment != null) {
				convertView.findViewById(R.id.commentsource).setVisibility(
						View.VISIBLE);
				ch.source = (TextView) convertView
						.findViewById(R.id.commentsourcetext);
				ch.source.setText("@" + reply_comment.getUserName() + ":"
						+ reply_comment.getContent(),
						TextView.BufferType.SPANNABLE);
				Utils.textHighlight(ch.source, "http://", " ");
			}else if(comment.getStatus()!=null){
				StatusItem status = comment.getStatus();
				convertView.findViewById(R.id.commentsource).setVisibility(
						View.VISIBLE);
				ch.source = (TextView) convertView
						.findViewById(R.id.commentsourcetext);
				ch.source.setText("@" + status.getUserName() + ":"
						+ status.getContent(),
						TextView.BufferType.SPANNABLE);
				Utils.textHighlight(ch.source, "http://", " ");
			}
		}

		return convertView;
	}

}