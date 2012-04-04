package cn.edu.nju.software.ui;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.edu.nju.software.service.impl.TencentMicroBlogService;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


public class WebViewActivity extends Activity {
	
	private WebView webView;
	private String verifier = "";
	private int index = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_PROGRESS);    //设置Activity显示进度条
		setContentView(R.layout.webview);
		webView = (WebView)this.findViewById(R.id.webView);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setBuiltInZoomControls(true);
		
		webView.setWebChromeClient(new WebChromeClient(){
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				WebViewActivity.this.setProgress(newProgress * 100);
			}
		});
		
		Bundle extras = getIntent().getExtras();
		String url = "";
		if(extras != null){
			if(extras.containsKey("url")){
				url = extras.getString("url");
				webView.loadUrl(url);
			}
		}
		webView.addJavascriptInterface(new JavaScriptInterface(), "Methods"); 
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				Log.i(WebViewActivity.class.getCanonicalName(), url);
				Pattern p = Pattern.compile("^" + TencentMicroBlogService.CALLBACKURL + ".*oauth_verifier=(\\d+)");
				Matcher m = p.matcher(url);
				if (m.find() && index == 0) {
					index++;
					verifier = m.group(1);
					Intent intent = new Intent();
					intent.setAction("ACTION_VERFIER");
					Bundle extras = new Bundle();
					extras.putString("verifier", verifier);
					intent.putExtras(extras);
					sendBroadcast(intent);
					WebViewActivity.this.finish();
				}
				
				/*if (url.indexOf("http://api.t.163.com/oauth/authorize") != -1 
						&& url.indexOf("authorize=1") != -1) {
					SystemConfig.ISACCESS = true;
					Toast.makeText(WebViewActivity.this, "请重新返回“绑定界面”进行最后授权！", Toast.LENGTH_LONG).show();
				}*/
			}
			
			public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {   
				handler.proceed();
			}   

		});
	}
	
	class JavaScriptInterface  
    {  
        public void getHTML(String html)  
        {  
            Log.i(WebViewActivity.class.getCanonicalName(), html);
        }  
    }  
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (webView.canGoBack()) {
				webView.goBack(); 
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
