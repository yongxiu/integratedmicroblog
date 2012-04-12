package cn.edu.nju.software.ui;

import java.io.File;
import java.net.URLEncoder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.nju.software.service.user.impl.UserServiceImpl;
import cn.edu.nju.software.utils.DialogUtils;
import cn.edu.nju.software.utils.FileUtils;
import cn.edu.nju.software.utils.InfoHelper;
import cn.edu.nju.software.utils.MediaUtils;
import cn.edu.nju.software.utils.Utils;
import cn.edu.nju.software.utils.DialogUtils.DialogCallBack;

public class WriteActivity extends Activity {
	
	private Button button = null;
	private Button imgChooseBtn=null;
	private ImageView imgView = null;
	private TextView wordCounterTextView = null;
	private EditText contentEditText = null;
	private ProgressDialog dialog = null;
	private ImageButton backBtn;
	
	private Activity instance;
	private Context mContext;
	
	private static final int REQUEST_CODE_GETIMAGE_BYSDCARD = 0;
	private static final int REQUEST_CODE_GETIMAGE_BYCAMERA = 1;
	private String thisLarge = null, theSmall = null;

	private static final int TOOLBAR0 = 0;
	private static final int TOOLBAR1 = 1;
	
	protected final String SDCARD_MNT = "/mnt/sdcard";
	protected final String SDCARD = "/sdcard";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{   
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write);
		
    	instance = this;
		mContext = getApplicationContext();
		button = (Button)findViewById( R.id.Send );
		imgChooseBtn = (Button)findViewById( R.id.add_cmamera_btn );
		imgView = (ImageView)findViewById( R.id.share_image );
		wordCounterTextView = (TextView)findViewById( R.id.remain_count );
		contentEditText = (EditText)findViewById( R.id.weibo_content );
		backBtn = (ImageButton) findViewById(R.id.Back);
		
		backBtn.setOnClickListener(new BackBtnListener());
		
    	dialog = new ProgressDialog(instance);
		dialog.setMessage("分享中...");
		dialog.setIndeterminate(false);
		dialog.setCancelable(true);

		Bundle bundle = getIntent().getExtras();
		if(bundle!=null)
		{
			thisLarge = bundle.containsKey("thisLarge")?bundle.getString("thisLarge"):"";
			
			if (bundle.containsKey("sendText")) {
				contentEditText.setText(bundle.getString("sendText"));
				textCountSet();
			}
		}
		
		button.setOnClickListener( new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				
				if( !InfoHelper.checkNetWork(mContext) )
				{
					Toast.makeText( mContext, "网络连接失败，请检查网络设置！", Toast.LENGTH_LONG ).show();
				}
				else
				{
					if( isChecked() )
					{
						dialog.show();
						
						Thread thread = new Thread( new UpdateStatusThread() );
						thread.start();	
					}
				}	
			}
		});
		
		imgChooseBtn.setOnClickListener( new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				CharSequence[] items = {"手机相册", "手机拍照", "清除照片"};
				imageChooseItem(items);
			}
		});
		
		//侦听EditText字数改变
		TextWatcher watcher = new TextWatcher() 
		{
		    public void onTextChanged(CharSequence s, int start, int before, int count) 
		    {
		    	textCountSet();
		    }
		    
		    public void beforeTextChanged(CharSequence s, int start, int count,
		            int after)
		    {
		    	textCountSet();
		    }
		    
			@Override
			public void afterTextChanged(Editable s) {
				textCountSet();
			}
		};
		
		contentEditText.addTextChangedListener(watcher);
		
		if(!Utils.isBlank(thisLarge))
		{
			String imgName = FileUtils.getFileName(thisLarge);
			
	    	Bitmap bitmap = loadImgThumbnail(imgName, MediaStore.Images.Thumbnails.MICRO_KIND );
			if(bitmap!=null)
			{
				imgView.setBackgroundDrawable(new BitmapDrawable(bitmap));
				imgView.setOnClickListener( new OnClickListener(){
					@Override
					public void onClick(View v) {
		    			Intent intent = new Intent();
						intent.setAction(android.content.Intent.ACTION_VIEW);
						intent.setDataAndType(Uri.fromFile(new File(thisLarge)),"image/*");
						startActivity(intent);
					}
		        });
			} 
		}
	}

	/**
	 * 设置稿件字数
	 */
	private void textCountSet()
	{   
    	String textContent = contentEditText.getText().toString();
    	int currentLength = textContent.length();
    	if( currentLength <= 140 )
    	{
    		wordCounterTextView.setTextColor( Color.BLACK );
    		wordCounterTextView.setText( String.valueOf(140 - textContent.length()) );
    	}
    	else
    	{
    		wordCounterTextView.setTextColor( Color.RED );
    		wordCounterTextView.setText( String.valueOf(140-currentLength) );
    	}
	}
	
	/**
	 * 操作选择
	 * @param items
	 */
	public void imageChooseItem(CharSequence[] items )
	{
		AlertDialog imageDialog = new AlertDialog.Builder(instance).setTitle("增加图片").setItems(items,
			new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int item)
				{
					//手机选图
					if( item == 0 )
					{
						Intent intent = new Intent(Intent.ACTION_GET_CONTENT); 
						intent.setType("image/*"); 
						startActivityForResult(intent, REQUEST_CODE_GETIMAGE_BYSDCARD); 
					}
					//拍照
					else if( item == 1 )
					{	  
						Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");	
						
						String camerName = InfoHelper.getFileName();
						String fileName = "Share" + camerName + ".tmp";	
						
						File camerFile = new File( InfoHelper.getCamerPath(), fileName );
								
						theSmall = InfoHelper.getCamerPath() + fileName;
						thisLarge = getLatestImage();
						
						Uri originalUri = Uri.fromFile( camerFile );
					    intent.putExtra(MediaStore.EXTRA_OUTPUT, originalUri); 	
						startActivityForResult(intent, REQUEST_CODE_GETIMAGE_BYCAMERA);
					}   
					else if( item == 2 )
					{
						thisLarge = null;
						imgView.setBackgroundDrawable(null);
					}
				}}).create();
		
		 imageDialog.show();
	}
	
	@Override 
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{ 
        if ( requestCode == REQUEST_CODE_GETIMAGE_BYSDCARD ) 
        { 
        	if (resultCode != RESULT_OK) 
    		{   
    	        return;   
    	    }
        	
        	if(data == null)    return;
        	
        	Uri thisUri = data.getData();
        	String thePath = InfoHelper.getAbsolutePathFromNoStandardUri(thisUri);
        	
        	//如果是标准Uri
        	if( Utils.isBlank(thePath) )
        	{
        		thisLarge = getAbsoluteImagePath(thisUri);
        	}
        	else
        	{
        		thisLarge=thePath;
        	}
        	
        	String attFormat = FileUtils.getFileFormat(thisLarge);
        	if( !"photo".equals(MediaUtils.getContentType(attFormat)) )
        	{
        		Toast.makeText(mContext, "请选择图片文件！", Toast.LENGTH_SHORT).show();
        		return;
        	}
        	String imgName = FileUtils.getFileName(thisLarge);
    		
        	Bitmap bitmap = loadImgThumbnail(imgName, MediaStore.Images.Thumbnails.MICRO_KIND );
    		if(bitmap!=null)
    		{
    			imgView.setBackgroundDrawable(new BitmapDrawable(bitmap));
    		}
        }
        //拍摄图片
        else if(requestCode ==REQUEST_CODE_GETIMAGE_BYCAMERA )
        {	
        	if (resultCode != RESULT_OK) 
    		{   
    	        return;   
    	    }
        	
        	super.onActivityResult(requestCode, resultCode, data);
        	
        	Bitmap bitmap = InfoHelper.getScaleBitmap(mContext, theSmall);
        	
    		if(bitmap!=null)
    		{
    			imgView.setBackgroundDrawable(new BitmapDrawable(bitmap));
    		}
        }
        
        imgView.setOnClickListener( new OnClickListener(){
			@Override
			public void onClick(View v) {
    			Intent intent = new Intent();
				intent.setAction(android.content.Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(new File(thisLarge)),"image/*");
				startActivity(intent);
			}
        });
    }
	
	/**
	 * 数据合法性判断
	 * @return
	 */
	private boolean isChecked()
	{
		boolean ret = true;
		if( Utils.isBlank(contentEditText.getText().toString()) )
		{
			Toast.makeText(mContext, "说点什么吧", Toast.LENGTH_SHORT ).show();
			ret = false;
		}
		else if( contentEditText.getText().toString().length() > 140 )
		{
			int currentLength = contentEditText.getText().toString().length();
			
			Toast.makeText(mContext, "已超出"+(currentLength-140)+"字", Toast.LENGTH_SHORT ).show();
			ret = false;
		}
		return ret;
	}
	
    Handler handle = new Handler()
	{
		@Override
		public void handleMessage(Message msg) 
		{	
			if(dialog!=null)
			{
				dialog.dismiss();
			}
			
			thisLarge = null;
			contentEditText.setText("");
			imgView.setBackgroundDrawable(null);

			if( msg.what>0 )
			{
				Toast.makeText(mContext, "微博分享成功", Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(mContext, "微博分享失败", Toast.LENGTH_SHORT).show();
			}
		}   
	};
	
    Handler endSessionHandle = new Handler()
	{
		@Override
		public void handleMessage(Message msg) 
		{	
			finish();
		}   
	};
	
	//分享线程
    class UpdateStatusThread implements Runnable
    {	
		public void run() 
		{ 
			int what = -1;
			try 
            {
            	String msg = contentEditText.getText().toString();
            	if(msg.getBytes().length != msg.length())
            	{
            		msg = URLEncoder.encode(msg, "UTF-8");
            	}
            	
            	if( Utils.isBlank(thisLarge) )
            	{
            		UserServiceImpl.getService().share2weibo(WriteActivity.this, 
            				msg, "");
            		//status = weibo.updateStatus(msg);
            	}
            	else
            	{
            		File file = new File(thisLarge);
            		//status = weibo.uploadStatus(msg, file);
            	}
				
				what=1;
			} 
            catch (Exception e) 
            {
            	e.printStackTrace();
            	Log.e("WeiboPub", e.getMessage());
			}	
			handle.sendEmptyMessage(what);
		}
	}
    
    //用户注销线程
    class EndSessionThread implements Runnable
    {	
		public void run() 
		{
			/*AccessInfoHelper accessDBHelper = new AccessInfoHelper(mContext);
			accessDBHelper.open();
			accessDBHelper.delete();
			accessDBHelper.close();   
			Weibo weibo = OAuthConstant.getInstance().getWeibo();
			try 
			{
				weibo.endSession();
			} catch (WeiboException e) {
				e.printStackTrace();
			}
			endSessionHandle.sendEmptyMessage(201);*/
		}
	}
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		menu.add(0, TOOLBAR0, 1, "退出程序" ).setIcon( android.R.drawable.ic_menu_revert );
		menu.add(0, TOOLBAR1, 2, "注销登录" ).setIcon( android.R.drawable.ic_menu_delete );
		return super.onCreateOptionsMenu(menu);
	}

    @Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
    	if( item.getItemId() == 0 )
    	{
    		finish();
    	}
    	else
    	{	
    		DialogUtils.dialogBuilder(instance, "提示","确定要注销登录并退出？", new DialogCallBack(){
				@Override
				public void callBack() {
					dialog.show();
					dialog.setMessage("注销登录中...");
					Thread thread = new Thread( new EndSessionThread() );
					thread.start();	
				}
    		});
    	}
    	return super.onOptionsItemSelected(item);
	}
    
    /**
     * 通过uri获取文件的绝对路径
     * @param uri
     * @return
     */
	protected String getAbsoluteImagePath(Uri uri) 
    {
		String imagePath = "";
        String [] proj={MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery( uri,
                        proj, 		// Which columns to return
                        null,       // WHERE clause; which rows to return (all rows)
                        null,       // WHERE clause selection arguments (none)
                        null); 		// Order-by clause (ascending by name)
        
        if(cursor!=null)
        {
        	int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        	if(  cursor.getCount()>0 && cursor.moveToFirst() )
            {
            	imagePath = cursor.getString(column_index);
            }
        }
        
        return imagePath;
    }
	
	/**
	 * 获取图片缩略图
	 * 只有Android2.1以上版本支持
	 * @param imgName
	 * @param kind   MediaStore.Images.Thumbnails.MICRO_KIND
	 * @return
	 */
	protected Bitmap loadImgThumbnail( String imgName, int kind ) 
	{
		Bitmap bitmap = null;
		
        String[] proj = { MediaStore.Images.Media._ID,
                        MediaStore.Images.Media.DISPLAY_NAME };
        
        Cursor cursor = managedQuery(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, proj,
                        MediaStore.Images.Media.DISPLAY_NAME + "='" + imgName +"'", null, null);
       
        if ( cursor!=null && cursor.getCount()>0 && cursor.moveToFirst() ) 
        {
        	ContentResolver crThumb = getContentResolver();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            bitmap = MediaStore.Images.Thumbnails.getThumbnail(crThumb,
            		cursor.getInt(0),
                	kind, options);
        } 
        return bitmap;
	}
	
	/**
	 * 获取SD卡中最新图片路径
	 * @return
	 */
	protected String getLatestImage()
	{
		String latestImage = null;
		String[] items = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA }; 
		Cursor cursor = managedQuery(
		                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, 
		                                items, 
		                                null,
		                                null, 
		                                MediaStore.Images.Media._ID + " desc");
		
		if( cursor != null && cursor.getCount()>0 )
		{
			cursor.moveToFirst();
			for( cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext() )
			{
				latestImage = cursor.getString(1);
				break;
			}
		}
		
	    return latestImage;
	}
	
	private class BackBtnListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			WriteActivity.this.finish();
		}
		
	}
}
