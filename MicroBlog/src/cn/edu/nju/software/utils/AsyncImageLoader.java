package cn.edu.nju.software.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public class AsyncImageLoader {

	private ConcurrentHashMap<String, SoftReference<Drawable>> imageCache;

	public AsyncImageLoader() {
		imageCache = new ConcurrentHashMap<String, SoftReference<Drawable>>();
	}

	public void loadDrawable(final String imageUrl, final ImageView imageView,
			final ImageCallback imageCallback) {
		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Drawable> softReference = imageCache.get(imageUrl);
			Drawable drawable = softReference.get();
			if (drawable != null) {
				imageCallback.imageLoaded(drawable, imageView);
			}
		}
		final Handler handler = new Handler() {
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((Drawable) message.obj, imageView);
			}
		};
		new Thread() {
			@Override
			public void run() {
				Drawable drawable = loadImageFromUrl(imageUrl);
				imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
				Message message = handler.obtainMessage(0, drawable);
				handler.sendMessage(message);
			}
		}.start();
	}

	public static Drawable loadImageFromUrl(String url) {
		URL m;
		InputStream i = null;
		try {
			m = new URL(url);
			i = (InputStream) m.getContent();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Drawable d = Drawable.createFromStream(i, "src");
		return d;
	}

	public interface ImageCallback {
		public void imageLoaded(Drawable imageDrawable, ImageView imageView);
	}
}