package de.uniulm.bagception.bagceptionmastercontrolserver.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	// Logcat Tag
	private static final String LOG = "DatabaseHelper";
	
	// Database Version
	private static final int DATABASE_VERSION = 1;
	
	// Database Name
	private static final String DATABASE_NAME = "BagceptionDatabase";
	
	
	// Table Names
	private static final String TABLE_ITEM = "item";
	private static final String TABLE_TAGID = "tag_id";
	private static final String TABLE_CATEGORY = "category";
	private static final String TABLE_INDEPENDENTITEM = "independentitem";
	private static final String TABLE_CONTEXTITEM = "contextitem";
	private static final String TABLE_ACTIVITYITEM = "activityitem";
	private static final String TABLE_ITEMATTRIBUTE = "itemattribute";
	private static final String TABLE_ACTIVITY = "activity";
	private static final String TABLE_LOCATION = "location";
	private static final String TABLE_PHOTO = "photo";
	
	
	// Commom columns
	private static final String _ID = "_id";
	private static final String NAME = "name";
	private static final String ITEM_ID = "item_id";
	private static final String CATEGORY_ID = "category_id";
	
	// Column TABLE_TAGID
	private static final String TAG_ID = "tag_id";
	
	// Column TABLE_ACTIVITYITEM
	private static final String ACTIVITY_ID = "activity_id";
	
	// Columns TABLE_ITEMATTRIBUTE
	private static final String TEMPERATURE = "temperature";
	private static final String WEATHER = "weather";
	private static final String LIGHTNESS = "lightness";
	
	// Cloumn TABLE_ACTIVITY
	private static final String LOCATION_ID = "location_id";
	
	// Columns TABLE_LOCATION
	private static final String LON = "lon";
	private static final String LAT = "lat";
	private static final String RADIUS = "radius";
	private static final String MAC = "mac";
	
	// Column TABLE_PHOTO
	private static final String _DATA = "_data";
	
	
	// Table create statements
	private static final String CREATE_TABLE_ITEM = 
			"CREATE TABLE " + TABLE_ITEM 
			+ "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ NAME + " TEXT NOT NULL, " 
			+ CATEGORY_ID + " INTEGER DEFAULT -1);";
	
	
	private static final String CREATE_TABLE_TAGID = 
			"CREATE TABLE " + TABLE_TAGID
			+ "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ TAG_ID + " TEXT NOT NULL, " 
			+ ITEM_ID + " INTEGER NOT NULL);";
	
	
	private static final String CREATE_TABLE_CATEGORY = 
			"CREATE TABLE " + TABLE_CATEGORY
			+ "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ NAME + " TEXT NOT NULL);"; 
	
	
	private static final String CREATE_TABLE_INDEPENDENTITEM = 
			"CREATE TABLE " + TABLE_INDEPENDENTITEM
			+ "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ ITEM_ID + " INTEGER NOT NULL);"; 
	
	
	private static final String CREATE_TABLE_CONTEXTITEM = 
			"CREATE TABLE " + TABLE_CONTEXTITEM
			+ "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ ITEM_ID + " INTEGER NOT NULL);";
	
	
	private static final String CREATE_TABLE_ACTIVITYITEM = 
			"CREATE TABLE " + TABLE_ACTIVITYITEM
			+ "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ ACTIVITY_ID + " INTEGER NOT NULL, "
			+ ITEM_ID + " INTEGER NOT NULL, "
			+ CATEGORY_ID + " INTEGER NOT NULL);";
	
	
	private static final String CREATE_TABLE_ITEMATTRIBUTE = 
			"CREATE TABLE " + TABLE_ITEMATTRIBUTE
			+ "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ ITEM_ID + " INTEGER NOT NULL, "
			+ TEMPERATURE + " TEXT, "
			+ WEATHER + " TEXT, "
			+ LIGHTNESS + " TEXT);";

	
	private static final String CREATE_TABLE_ACTIVITY = 
			"CREATE TABLE " + TABLE_ACTIVITY 
			+ "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ NAME + " TEXT NOT NULL, " 
			+ LOCATION_ID + " INTEGER);";
	
	
    private static final String CREATE_TABLE_LOCATION = 
    		"CREATE TABLE " + TABLE_LOCATION
    		+ "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ NAME + " TEXT NOT NULL, "
			+ LON + " REAL, "
			+ LAT + " REAL, "
			+ RADIUS + " REAL, "
			+ MAC + " TEXT);";
    
    
    private static final String CREATE_TABLE_PHOTO =
    		"CREATE TABLE " + TABLE_TAGID
			+ "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ ITEM_ID + " INTEGER NOT NULL, " 
			+ _DATA + " BLOB);";
	
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	
	@Override
	public void onCreate(SQLiteDatabase db) {

		// Creating required tables
		db.execSQL(CREATE_TABLE_ITEM);
		db.execSQL(CREATE_TABLE_TAGID);
		db.execSQL(CREATE_TABLE_CATEGORY);
		db.execSQL(CREATE_TABLE_INDEPENDENTITEM);
		db.execSQL(CREATE_TABLE_CONTEXTITEM);
		db.execSQL(CREATE_TABLE_ACTIVITYITEM);
		db.execSQL(CREATE_TABLE_ITEMATTRIBUTE);
		db.execSQL(CREATE_TABLE_ACTIVITY);
		db.execSQL(CREATE_TABLE_LOCATION);
		db.execSQL(CREATE_TABLE_PHOTO);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		// onUpgrade drop old tables
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAGID);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_INDEPENDENTITEM);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTEXTITEM);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVITYITEM);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMATTRIBUTE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVITY);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTO);
		
		// create new tables
		onCreate(db);
	}

}











