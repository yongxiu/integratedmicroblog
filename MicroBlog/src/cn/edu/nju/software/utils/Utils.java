package cn.edu.nju.software.utils;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

public class Utils {
	public static Bitmap DownloadImg(String url) {
		URL uri;
		Bitmap bm = null;
		try {
			uri = new URL(url);
			// 获取图片流数据
			InputStream is = uri.openStream();
			// 生成图片
			bm = BitmapFactory.decodeStream(is);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bm;
	}

	public static String ConvertTime(Date date) {
		Date now = new Date();
		long timeSpan = now.getTime() - date.getTime();

		timeSpan /= 1000;
		if (timeSpan < 60) {
			return String.format("%d秒前", timeSpan);
		}

		timeSpan /= 60;
		if (timeSpan < 60) {
			return String.format("%d分前", timeSpan);
		}

		timeSpan /= 60;
		if (timeSpan < 24) {
			return String.format("%d小时前", timeSpan);
		}

		timeSpan /= 24;
		return String.format("%d天前", timeSpan);
	}

	public static boolean isBlank(String input) {
		if (input == null || "".equals(input))
			return true;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}

	public static String intToIp(int i) {
		return ((i >> 24) & 0xFF) + "." + ((i >> 16) & 0xFF) + "."
				+ ((i >> 8) & 0xFF) + "." + (i & 0xFF);
	}

	public static void textHighlight(TextView textView, String start, String end) {
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
}
