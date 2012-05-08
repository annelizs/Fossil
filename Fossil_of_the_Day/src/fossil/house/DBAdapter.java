package fossil.house;


import java.sql.Blob;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter 
{

	public static final String KEY_ROWID = "_id";
	public static final String KEY_COMMON = "common";
	public static final String KEY_LATIN = "latin";
	public static final String KEY_LOCATION = "location";
	public static final String KEY_IMAGE = null;
	private static final String TAG = "DBAdapter";
	
	private static final String DATABASE_NAME = "MyDB";
	private static final String DATABASE_TABLE = "fossils";
	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_CREATE = 
			"create table fossils (_id integer primary key autoincrement, " + "common text not null, " +
					"latin text not null, location text not null, " 
					+ "image not null);";
	
	private final Context context;
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;
	
	public DBAdapter(Context ctx)
	{
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}
	
	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		DatabaseHelper(Context context)
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db)
		{
			try 
			{
				db.execSQL(DATABASE_CREATE);
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS fossils");
			onCreate(db);
		}
	}
		//Opens Database
		public DBAdapter open() throws SQLException
		{
			db = DBHelper.getWritableDatabase();
			return this;
		}
		
		public void close()
		{
			DBHelper.close();
		}
		
		public long insertFossil(String common, String latin, String location, String image)
		{
			ContentValues initialValues = new ContentValues();
			initialValues.put(KEY_COMMON, common);
			initialValues.put(KEY_LATIN, latin);
			initialValues.put(KEY_LOCATION, location);
			initialValues.put(KEY_IMAGE, image);
			return db.insert(DATABASE_TABLE, null, initialValues);
		}
		
		public boolean deleteFossil(long rowID)
		{
			return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowID, null) > 0;
		}
		
		public Cursor getAllFossils()
		{
			return db.query(DATABASE_TABLE, new String[] 
					{
						KEY_ROWID,
						KEY_COMMON,
						KEY_LATIN,
						KEY_LOCATION,
						KEY_IMAGE
					},
					null, null, null, null, null);
		}
		
		public Cursor getFossil(long rowID) throws SQLException
		{
			
			
			
			Cursor mCursor = 
					db.query(true, DATABASE_TABLE, new String[]
							{
								KEY_ROWID,
								KEY_COMMON,
								KEY_LATIN,
								KEY_LOCATION,
								KEY_IMAGE
							},
							KEY_ROWID + "=" + rowID, null, null, null, null, null);
			if (mCursor != null)
			{
				mCursor.moveToFirst();
			}
			return mCursor;
		}
		
		public boolean updateContact(long rowID, String common, String latin, String location, String image)
		{
			ContentValues args = new ContentValues();
			args.put(KEY_COMMON, common);
			args.put(KEY_LATIN, latin);
			args.put(KEY_LOCATION, location);
			args.put(KEY_IMAGE, image);
			return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowID, null) > 0;
		}
	}
								
	

