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
			"CREATE TABLE IF NOT EXISTS " + TABLE_ITEM 
			+ "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ NAME + " TEXT UNIQUE NOT NULL, " 
			+ CATEGORY_ID + " INTEGER," 
			+ " FOREIGN KEY(" + CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORY + "(" + _ID + ") ON UPDATE CASCADE ON DELETE SET DEFAULT);";
	
	
	private static final String CREATE_TABLE_TAGID = 
			"CREATE TABLE IF NOT EXISTS " + TABLE_TAGID
			+ "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ TAG_ID + " TEXT UNIQUE NOT NULL, " 
			+ ITEM_ID + " INTEGER UNIQUE NOT NULL," 
			+ " FOREIGN KEY(" + ITEM_ID + ") REFERENCES " + TABLE_ITEM + "(" + _ID + ") ON UPDATE CASCADE ON DELETE SET DEFAULT);";
	
	
	private static final String CREATE_TABLE_CATEGORY = 
			"CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORY
			+ "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ NAME + " TEXT NOT NULL);"; 
	

	private static final String CREATE_TABLE_INDEPENDENTITEM = 
			"CREATE TABLE IF NOT EXISTS " + TABLE_INDEPENDENTITEM
			+ "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ ITEM_ID + " INTEGER NOT NULL,"
			+ " FOREIGN KEY(" + ITEM_ID + ") REFERENCES " + TABLE_ITEM + "(" + _ID + ") ON UPDATE CASCADE ON DELETE SET DEFAULT);";
	
	
	private static final String CREATE_TABLE_CONTEXTITEM = 
			"CREATE TABLE IF NOT EXISTS " + TABLE_CONTEXTITEM
			+ "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ ITEM_ID + " INTEGER NOT NULL,"
			+ " FOREIGN KEY(" + ITEM_ID + ") REFERENCES " + TABLE_ITEM + "(" + _ID + ") ON UPDATE CASCADE ON DELETE SET DEFAULT);";
	
	
	private static final String CREATE_TABLE_ACTIVITYITEM = 
			"CREATE TABLE IF NOT EXISTS " + TABLE_ACTIVITYITEM
			+ "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ ACTIVITY_ID + " INTEGER NOT NULL, "
			+ ITEM_ID + " INTEGER NOT NULL, "
			+ CATEGORY_ID + " INTEGER NOT NULL," 
			+ " FOREIGN KEY(" + ACTIVITY_ID + ") REFERENCES " + TABLE_ACTIVITY + "(" + _ID + ") ON UPDATE CASCADE ON DELETE SET DEFAULT,"
			+ " FOREIGN KEY(" + ITEM_ID + ") REFERENCES " + TABLE_ITEM + "(" + _ID + ") ON UPDATE CASCADE ON DELETE SET DEFAULT,"
			+ " FOREIGN KEY(" + CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORY + "(" + _ID + ") ON UPDATE CASCADE ON DELETE SET DEFAULT);";
	
	
	private static final String CREATE_TABLE_ITEMATTRIBUTE = 
			"CREATE TABLE IF NOT EXISTS " + TABLE_ITEMATTRIBUTE
			+ "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ ITEM_ID + " INTEGER NOT NULL, "
			+ TEMPERATURE + " TEXT, "
			+ WEATHER + " TEXT, "
			+ LIGHTNESS + " TEXT,"
			+ " FOREIGN KEY(" + ITEM_ID + ") REFERENCES " + TABLE_ITEM + "(" + _ID + ") ON UPDATE CASCADE ON DELETE SET DEFAULT);";

	
	private static final String CREATE_TABLE_ACTIVITY = 
			"CREATE TABLE IF NOT EXISTS " + TABLE_ACTIVITY 
			+ "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ NAME + " TEXT UNIQUE NOT NULL, " 
			+ LOCATION_ID + " INTEGER DEFAULT NULL,"
			+ " FOREIGN KEY(" + LOCATION_ID + ") REFERENCES " + TABLE_LOCATION + "(" + _ID + ") ON UPDATE CASCADE ON DELETE SET DEFAULT);";
	
	
    private static final String CREATE_TABLE_LOCATION = 
    		"CREATE TABLE IF NOT EXISTS " + TABLE_LOCATION
    		+ "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ NAME + " TEXT UNIQUE NOT NULL, "
			+ LON + " REAL, "
			+ LAT + " REAL, "
			+ RADIUS + " REAL, "
			+ MAC + " TEXT);";
    
    
    private static final String CREATE_TABLE_PHOTO =
    		"CREATE TABLE IF NOT EXISTS " + TABLE_TAGID
			+ "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ ITEM_ID + " INTEGER NOT NULL, " 
			+ _DATA + " BLOB,"
			+ " FOREIGN KEY(" + ITEM_ID + ") REFERENCES " + TABLE_ITEM + "(" + _ID + ") ON UPDATE CASCADE ON DELETE SET DEFAULT);";
	
	
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
	public void addItem(Item item) throws DatabaseException {

		SQLiteDatabase db = this.getWritableDatabase();
		String tag_id = item.getIds().get(0);
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
//		int image = item.getImageHash();
//		if (image != 0) {
//			addPhotoToItem(image, item_id);
//		}
		
	}


	@Override
	public void deleteItem(Item item) throws DatabaseException {

		SQLiteDatabase db = this.getWritableDatabase();
		
		long item_id = item.getId();
		db.delete(TABLE_TAGID, ITEM_ID + " = " + item_id, null);
//		db.delete(TABLE_CONTEXTITEM, ITEM_ID + " = " + item_id, null);
//		db.delete(TABLE_INDEPENDENTITEM, ITEM_ID + " = " + item_id, null);
//		db.delete(TABLE_PHOTO, ITEM_ID + " = " + item_id, null);
//		db.delete(TABLE_ACTIVITYITEM, ITEM_ID + " = " + item_id, null);
//		db.delete(TABLE_ITEMATTRIBUTE, ITEM_ID + " = " + item_id, null);
		db.delete(TABLE_ITEM, NAME + " = ?", new String[] {item.getName()});
	}


	@Override
	public int editItem(Item item,Item editValues) throws DatabaseException {
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(NAME, item.getName());
		values.put(CATEGORY_ID, item.getCategory().getId());
		db.update(TABLE_ITEM, values, _ID + " = ?", new String[] {String.valueOf(item.getId())});
		return 0;
		//TODO 
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
		
		Category cat = new Category(categoryName.getString(categoryName.getColumnIndex(NAME)));
		cat.setId(categoryName.getInt(categoryName.getColumnIndex(_ID)));
		
		item.setCategory(cat);
		
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
						items.add(item);
				} while(c.moveToNext());
			}
			
		return items;
	}

	
	
	// -------------------------------- "TagId" table methods -------------------------------- //
	
	/**
	 * Add tag_id to item
	 */
	public void addTagId(long item_id, String tag_id) throws DatabaseException {
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(ITEM_ID, item_id);
		values.put(TAG_ID, tag_id);
		
		long id = db.insert(TABLE_TAGID, null, values);
		
		//return id;
	}
	
	public void deleteTagId(String tag_id) throws DatabaseException {
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.delete(TABLE_TAGID, TAG_ID + " = " + tag_id, null);
		
	}
	
	
	// -------------------------------- "IndependentItem" table methods -------------------------------- //
	
	/**
	 * Add IndependentItem
	 */
	public void addIndependentItem(long item_id) throws DatabaseException{
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(ITEM_ID, item_id);
		
		long id = db.insert(TABLE_INDEPENDENTITEM, null, values);
		
		//return id;
	}
	
	public void deleteIndependentItem(long item_id) throws DatabaseException {
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.delete(TABLE_INDEPENDENTITEM, ITEM_ID + " = " + item_id, null);
	}
	
	// -------------------------------- "ContextItem" table methods -------------------------------- //
	
	/**
	 * Add ContextItem
	 */
	public void addContextItem(long item_id) throws DatabaseException {
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(ITEM_ID, item_id);
		
		long id = db.insert(TABLE_CONTEXTITEM, null, values);
		
		//return id;
	}
	
	
	public void deleteContextItem(long item_id) throws DatabaseException {
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.delete(TABLE_CONTEXTITEM, ITEM_ID + " = " + item_id, null);
	}
	
	
	
	// -------------------------------- "Photo" table methods -------------------------------- //
	
	/**
	 * Add Picture
	 */
	public void addPhotoToItem(int image, long item_id) {
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(ITEM_ID, item_id);
		values.put(_DATA, image);
		
		long id = db.insert(TABLE_PHOTO, null, values);
		
		//return id;
		
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

		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(NAME, activity.getName());
		values.put(LOCATION_ID, activity.getLocation().getId());
		
		long id = db.insert(TABLE_ACTIVITY, null, values);
		
		//addActivityItem(id, activity.getItemsForActivity());
	}


	@Override
	public void deleteActivity(Activity activity) throws DatabaseException {

		SQLiteDatabase db = this.getWritableDatabase();
		
		String name = activity.getName();
		
		db.delete(TABLE_ACTIVITY, NAME + " = ?", new String[] {name});
	}


	@Override
	public void editActivity(Activity toEdit, Activity after) throws DatabaseException {

		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		int activity_id = toEdit.getId();
		
		if(toEdit.getName() != after.getName()) {
			values.put(NAME, after.getName());
		}
		if(toEdit.getLocation() != after.getLocation()) {
			values.put(LOCATION_ID, after.getLocation().getId());
		}
		
		long id = db.update(TABLE_ACTIVITY, values, _ID + " = " + activity_id, null);
	}
	
	
	public void updateActivtiy(Activity activity) throws DatabaseException {
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(NAME, activity.getName());
		values.put(LOCATION_ID, activity.getLocation().getId());
		
		long id = db.update(TABLE_ACTIVITY, values, _ID + " = ?", new String[] {String.valueOf(activity.getId())});
	}


	@Override
	public List<Activity> getActivities() throws DatabaseException {

		List<Activity> activities = new ArrayList<Activity>();
		
		String selectQuery = "SELECT * FROM " + TABLE_ACTIVITY;
		
		Log.e(LOG, selectQuery);
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		
		// looping through all rows and adding them to list
		if(c.moveToFirst()) {
			do {
					Activity activity = new Activity();
					activity.setName(c.getString(c.getColumnIndex(NAME)));
					activities.add(activity);
			} while(c.moveToNext());
		}
		return activities;
	}
	
	
	
	// -------------------------------- "ActivityItem" table methods -------------------------------- //
	
	private void addActivityItem(long activity_id, List<Item> itemsForActivity) {

		SQLiteDatabase db = this.getWritableDatabase();
		
		for(int i = 0; i < itemsForActivity.size(); i++) {
			
			Item item = itemsForActivity.get(i);
			ContentValues values = new ContentValues();
			values.put(ACTIVITY_ID, activity_id);
			values.put(ITEM_ID, item.getId());
			values.put(CATEGORY_ID, item.getCategory().getId());
			
			long id = db.insert(TABLE_ACTIVITYITEM, null, values);
		}
		
	}
	
	
	
	// -------------------------------- "Category" table methods -------------------------------- //

	@Override
	public void addCategory(Category category) throws DatabaseException {

		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(NAME, category.getName());
		
		long id = db.insert(TABLE_CATEGORY, null, values);
	}


	@Override
	public void deleteCategory(Category category) throws DatabaseException {

		SQLiteDatabase db = this.getWritableDatabase();
		
		String name = category.getName();
		
		db.delete(TABLE_CATEGORY, NAME + " = " + name, null);
	}


	@Override
	public void editCategory(Category toEdit, Category after) throws DatabaseException {

		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		int cat_id = toEdit.getId();
		
		if(toEdit.getName() != after.getName()){
			
			values.put(NAME, after.getName());
		}
		
		long id = db.update(TABLE_CATEGORY, values, _ID + " = " + cat_id, null);
	}


	@Override
	public List<Category> getCategories() throws DatabaseException {

		List<Category> categories = new ArrayList<Category>();
		
		String selectQuery = "SELECT * FROM " + TABLE_CATEGORY;
		
		Log.e(LOG, selectQuery);
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		
		// looping through all rows and adding them to list
		if(c.moveToFirst()) {
			do {
					Category category = new Category();
					category.setName(c.getString(c.getColumnIndex(NAME)));
					categories.add(category);
			} while(c.moveToNext());
		}
		return categories;
	}

	
	
	// -------------------------------- "Location" table methods -------------------------------- //

	@Override
	public void addLocation(Location location) throws DatabaseException {

		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(NAME, location.getName());
		values.put(LON, location.getLng());
		values.put(LAT, location.getLat());
		values.put(RADIUS, location.getRadius());
		values.put(MAC, location.getRadius());
		
		long id = db.insert(TABLE_LOCATION, null, values);
	}


	@Override
	public void deleteLocation(Location location) throws DatabaseException {

		SQLiteDatabase db = this.getWritableDatabase();
		
		String name = location.getName();
		
		db.delete(TABLE_LOCATION, NAME + " = " + name, null);
	}


	@Override
	public void editLocation(Location toEdit, Location after) throws DatabaseException {

		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		
		if(toEdit.getName() != after.getName()) {
			values.put(NAME, after.getName());
		}
		
		if(toEdit.getLng() != after.getLng()) {
			values.put(LON, after.getLng());
		}
		
		if(toEdit.getLat() != after.getLat()) {
			values.put(LAT, after.getLat());
		}
		
		if(toEdit.getRadius() != after.getRadius()) {
			values.put(RADIUS, after.getRadius());
		}
		
		if(toEdit.getMac() != after.getMac()) {
			values.put(MAC, after.getMac());
		}
		
		long id = db.update(TABLE_LOCATION, values, _ID + " = " + toEdit.getId(), null);
	}


	@Override
	public List<Location> getLocations() throws DatabaseException {

		List<Location> locations = new ArrayList<Location>();
		
		String selectQuery = "SELECT * FROM " + TABLE_LOCATION;
		
		Log.e(LOG, selectQuery);
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		
		// looping through all rows and adding them to list
		if(c.moveToFirst()) {
			do {
					Location location = new Location();
					location.setName(c.getString(c.getColumnIndex(NAME)));
					locations.add(location);
			} while(c.moveToNext());
		}
		return locations;
	}
	
}











