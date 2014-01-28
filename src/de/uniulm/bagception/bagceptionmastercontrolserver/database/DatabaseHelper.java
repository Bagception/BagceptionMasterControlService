package de.uniulm.bagception.bagceptionmastercontrolserver.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import de.uniulm.bagception.bundlemessageprotocol.entities.Activity;
import de.uniulm.bagception.bundlemessageprotocol.entities.Category;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.bundlemessageprotocol.entities.Location;

public class DatabaseHelper extends SQLiteOpenHelper implements DatabaseInterface{

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
	private static final String TABLE_ACTIVITYITEM = "activityitem";
	private static final String TABLE_ITEMATTRIBUTE = "itemattribute";
	private static final String TABLE_ACTIVITY = "activity";
	private static final String TABLE_LOCATION = "location";
	private static final String TABLE_PHOTO = "photo";

	// Items die IMMER mitgenommen werden (z.B. Asthmaspray)
	private static final String TABLE_INDEPENDENTITEM = "independentitem";

	// Items die immer bei einem BESTIMMTEN KONTEXT mitgenommen werden (z.B. Regenschirm, wenns regnet)
	private static final String TABLE_CONTEXTITEM = "contextitem";
	
	
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
	
	
	

	// -------------------------------- "Item" table methods -------------------------------- //

	/**
	 * Create a new Item
	 */
	@Override
	public void addItem(Item item, String tag_id) throws DatabaseException {

		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(NAME, item.getName());
		values.put(CATEGORY_ID, item.getCategory().getId());
		
		// Insert row to Item table
		long item_id = db.insert(TABLE_ITEM, null, values);
		
		// Insert tag_id in TagId Table if tag_id not null
		if (tag_id != null) {
			addTagId(item_id, tag_id);
		}

		// If "independentItem" is selected, add item to IndependentItem table
		boolean independentItem = item.getIndependentItem();
		if (independentItem == true) {
			addIndependentItem(item_id);
		}
		
		// If "contextItem" is selected, add item to ContextItem table
		boolean contextItem = item.getContextItem();
		if (contextItem == true) {
			addContextItem(item_id);
		}
		
		// If an Photo exists, add photo to Photo table
		int image = item.getImageHash();
		if (image != 0) {
			addPhotoToItem(image, item_id);
		}
		
	}


	@Override
	public void deleteItem(Item item) throws DatabaseException {

		SQLiteDatabase db = this.getWritableDatabase();
		
		long item_id = item.getId();
		
		db.delete(TABLE_ITEM, _ID + " = " + item_id, null);
		db.delete(TABLE_TAGID, ITEM_ID + " = " + item_id, null);
		db.delete(TABLE_CONTEXTITEM, ITEM_ID + " = " + item_id, null);
		db.delete(TABLE_INDEPENDENTITEM, ITEM_ID + " = " + item_id, null);
		db.delete(TABLE_PHOTO, ITEM_ID + " = " + item_id, null);
		db.delete(TABLE_ACTIVITYITEM, ITEM_ID + " = " + item_id, null);
		db.delete(TABLE_ITEMATTRIBUTE, ITEM_ID + " = " + item_id, null);
	}


	@Override
	public int updateItem(Item item) throws DatabaseException {
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(NAME, item.getName());
		values.put(CATEGORY_ID, item.getCategory().getId());
		
		return db.update(TABLE_ITEM, values, _ID + " = ?", new String[] {String.valueOf(item.getId())});
	}
	
	
	@Override
	public Item getItem(long id) throws DatabaseException {
		
		SQLiteDatabase db = this.getReadableDatabase();
		
		int category;
		
		String selectQuery = "SELECT * FROM " + TABLE_ITEM + " WHERE " + _ID + " = " + id;
		
		
		Log.e(LOG, selectQuery);
		
		Cursor itemData = db.rawQuery(selectQuery, null);
		
		category = itemData.getInt(itemData.getColumnIndex(CATEGORY_ID));
		String getCategoryQuery = "SELECT " + NAME + " FROM " + TABLE_CATEGORY + " WHERE " + _ID + " = " + category;
		
		Cursor categoryName = db.rawQuery(getCategoryQuery, null);
		
		if (itemData != null) {
			itemData.moveToFirst();
		}
		
		Item item = new Item();
		item.setName(itemData.getString(itemData.getColumnIndex(NAME)));
		//TODO category here!!
		//item.setCategory(categoryName.getString(categoryName.getColumnIndex(NAME)));
		
		return item;
	}


	@Override
	public List<Item> getItems() throws DatabaseException {

			List<Item> items = new ArrayList<Item>();
			String selectQuery = "SELECT * FROM " + TABLE_ITEM;
			
			Log.e(LOG, selectQuery);
			
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor c = db.rawQuery(selectQuery, null);
			
			// looping through all rows and adding them to list
			if(c.moveToFirst()) {
				do {
						Item item = new Item();
						item.setName(c.getString(c.getColumnIndex(NAME)));
				} while(c.moveToNext());
			}
			
		return null;
	}

	
	
	// -------------------------------- "TagId" table methods -------------------------------- //
	
	/**
	 * Add tag_id to item
	 */
	public long addTagId(long item_id, String tag_id) throws DatabaseException {
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(ITEM_ID, item_id);
		values.put(TAG_ID, tag_id);
		
		long id = db.insert(TABLE_TAGID, null, values);
		
		return id;
	}
	
	public void deleteTagId(String tag_id) throws DatabaseException {
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.delete(TABLE_TAGID, TAG_ID + " = " + tag_id, null);
		
	}
	
	
	// -------------------------------- "IndependentItem" table methods -------------------------------- //
	
	/**
	 * Add IndependentItem
	 */
	public long addIndependentItem(long item_id) throws DatabaseException{
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(ITEM_ID, item_id);
		
		long id = db.insert(TABLE_INDEPENDENTITEM, null, values);
		
		return id;
	}
	
	public void deleteIndependentItem(long item_id) throws DatabaseException {
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.delete(TABLE_INDEPENDENTITEM, ITEM_ID + " = " + item_id, null);
	}
	
	// -------------------------------- "ContextItem" table methods -------------------------------- //
	
	/**
	 * Add ContextItem
	 */
	public long addContextItem(long item_id) throws DatabaseException {
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(ITEM_ID, item_id);
		
		long id = db.insert(TABLE_CONTEXTITEM, null, values);
		
		return id;
	}
	
	
	public void deleteContextItem(long item_id) throws DatabaseException {
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.delete(TABLE_CONTEXTITEM, ITEM_ID + " = " + item_id, null);
	}
	
	
	
	// -------------------------------- "Photo" table methods -------------------------------- //
	
	/**
	 * Add Picture
	 */
	public long addPhotoToItem(int image, long item_id) {
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(ITEM_ID, item_id);
		values.put(_DATA, image);
		
		long id = db.insert(TABLE_PHOTO, null, values);
		
		return id;
		
	}
	
	
	public void deletePhoto(long item_id) throws DatabaseException {
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.delete(TABLE_PHOTO, ITEM_ID + " = " + item_id, null);
	}
	
	
	public int updatePhoto(Item item) throws DatabaseException {
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(_DATA, item.getImageHash());
		
		return db.update(TABLE_PHOTO, values, _ID + " = ?", new String[] {String.valueOf(item.getId())});
	}
	
	// -------------------------------- "Activity" table methods -------------------------------- //

	@Override
	public void addActivity(Activity activity) throws DatabaseException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void deleteActivity(Activity activity) throws DatabaseException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void editActivity(Activity toEdit, Activity after)
			throws DatabaseException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public List<Activity> getActivities() throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void addCategory(Category category) throws DatabaseException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void deleteCategory(Category category) throws DatabaseException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void editCategory(Category toEdit, Category after)
			throws DatabaseException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public List<Category> getCategories() throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void addLocation(Location location) throws DatabaseException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void deleteLocation(Location location) throws DatabaseException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void editLocation(Location toEdit, Location after)
			throws DatabaseException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public List<Location> getLocations() throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}
	
}











