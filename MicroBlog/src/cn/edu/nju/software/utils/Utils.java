package cn.edu.nju.software.utils;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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

}
