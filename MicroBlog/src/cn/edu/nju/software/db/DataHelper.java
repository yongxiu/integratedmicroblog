package cn.edu.nju.software.db;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import cn.edu.nju.software.bean.UserInfo;
import cn.edu.nju.software.utils.MicroBlogType;


public class DataHelper {

	// 数据库名称

	private static String DB_NAME = "microblog.db";

	// 数据库版本

	private static int DB_VERSION = 3;

	private SQLiteDatabase db;

	private SqliteHelper dbHelper;

	public DataHelper(Context context) {

		dbHelper = new SqliteHelper(context, DB_NAME, null, DB_VERSION);

		db = dbHelper.getWritableDatabase();

	}

	public void Close()

	{

		db.close();

		dbHelper.close();

	}

	// 获取users表中的UserID、Access Token、Access Secret的记录

	public List<UserInfo> GetUserList(MicroBlogType mbType, Boolean isSimple)

	{

		List<UserInfo> userList = new ArrayList<UserInfo>();

		Cursor cursor = db.query(SqliteHelper.TB_NAME, null, 
				UserInfo.MBTYPE + "='" + mbType.toString() + "'", null, null,
				null, UserInfo.ID + " DESC");

		cursor.moveToFirst();

		while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {

			UserInfo user = new UserInfo();

			user.setId(cursor.getString(0));

			user.setMbType(MicroBlogType.valueOf(cursor.getString(1)));
			
			user.setUserId(cursor.getString(2));

			user.setToken(cursor.getString(3));

			user.setTokenSecret(cursor.getString(4));

			if (!isSimple) {

				user.setUserName(cursor.getString(5));

				ByteArrayInputStream stream = new ByteArrayInputStream(cursor
						.getBlob(6));

				Drawable icon = Drawable.createFromStream(stream, "image");

				user.setUserIcon(icon);

			}

			userList.add(user);

			cursor.moveToNext();

		}

		cursor.close();

		return userList;

	}

	// 判断users表中的是否包含某个UserID的记录

	public Boolean HaveUserInfo(MicroBlogType mbType, String UserId)

	{

		Boolean b = false;

		Cursor cursor = db.query(SqliteHelper.TB_NAME, null, UserInfo.USERID
				+ "=" + UserId + " AND " + UserInfo.MBTYPE + "='" + mbType.toString() + "'",
				null, null, null, null);

		b = cursor.moveToFirst();

		Log.e("HaveUserInfo", b.toString());

		cursor.close();

		return b;

	}

	// 更新users表的记录，根据UserId更新用户昵称和用户图标

	public int UpdateUserInfo(MicroBlogType mbType, String userName, Bitmap userIcon, String UserId)

	{

		ContentValues values = new ContentValues();
		
		values.put(UserInfo.USERNAME, userName);

		// BLOB类型

		final ByteArrayOutputStream os = new ByteArrayOutputStream();

		// 将Bitmap压缩成PNG编码，质量为100%存储

		userIcon.compress(Bitmap.CompressFormat.PNG, 100, os);

		// 构造SQLite的Content对象，这里也可以使用raw

		values.put(UserInfo.USERICON, os.toByteArray());

		int id = db.update(SqliteHelper.TB_NAME, values, UserInfo.USERID + "="
				+ UserId + " AND " + UserInfo.MBTYPE + "='" + mbType.toString() + "'", null);

		Log.e("UpdateUserInfo2", id + "");

		return id;

	}

	// 更新users表的记录

	public int UpdateUserInfo(MicroBlogType mbType, UserInfo user)

	{

		ContentValues values = new ContentValues();
		
		values.put(UserInfo.MBTYPE, user.getMbType().toString());
		
		values.put(UserInfo.USERID, user.getUserId());

		values.put(UserInfo.TOKEN, user.getToken());

		values.put(UserInfo.TOKENSECRET, user.getTokenSecret());

		int id = db.update(SqliteHelper.TB_NAME, values, UserInfo.USERID + "="
				+ user.getUserId() + " AND " + 
				UserInfo.MBTYPE + "='" + mbType.toString() + "'", null);

		Log.e("UpdateUserInfo", id + "");

		return id;

	}

	// 添加users表的记录

	public Long SaveUserInfo(MicroBlogType mbType, UserInfo user)

	{

		ContentValues values = new ContentValues();

		values.put(UserInfo.USERID, user.getUserId());
		
		values.put(UserInfo.MBTYPE, user.getMbType().toString());
		
		values.put(UserInfo.TOKEN, user.getToken());

		values.put(UserInfo.TOKENSECRET, user.getTokenSecret());

		Long uid = db.insert(SqliteHelper.TB_NAME, UserInfo.ID, values);

		Log.e("SaveUserInfo", uid + "");

		return uid;

	}

	// 删除users表的记录

	public int DelUserInfo(MicroBlogType mbType, String UserId) {

		int id = db.delete(SqliteHelper.TB_NAME,
				UserInfo.USERID + "=" + UserId + " AND " + 
				UserInfo.MBTYPE + "='" + mbType.toString() + "'", null);

		Log.e("DelUserInfo", id + "");

		return id;

	}
}