package de.uniulm.bagception.bagceptionmastercontrolserver.database;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import de.uniulm.bagception.bagceptionmastercontrolserver.R;
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
			+ NAME + " TEXT NOT NULL UNIQUE, " 
			+ CATEGORY_ID + " INTEGER," 
			+ " FOREIGN KEY(" + CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORY + "(" + _ID + ") ON UPDATE CASCADE ON DELETE SET DEFAULT);";
	
	
	private static final String CREATE_TABLE_TAGID = 
			"CREATE TABLE IF NOT EXISTS " + TABLE_TAGID
			+ "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ TAG_ID + " TEXT NOT NULL UNIQUE, " 
			+ ITEM_ID + " INTEGER NOT NULL," 
			+ " FOREIGN KEY(" + ITEM_ID + ") REFERENCES " + TABLE_ITEM + "(" + _ID + ") ON UPDATE CASCADE ON DELETE SET DEFAULT);";
	
	
	private static final String CREATE_TABLE_CATEGORY = 
			"CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORY
			+ "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ NAME + " TEXT NOT NULL UNIQUE);"; 
	

	private static final String CREATE_TABLE_INDEPENDENTITEM = 
			"CREATE TABLE IF NOT EXISTS " + TABLE_INDEPENDENTITEM
			+ "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ ITEM_ID + " INTEGER NOT NULL UNIQUE,"
			+ " FOREIGN KEY(" + ITEM_ID + ") REFERENCES " + TABLE_ITEM + "(" + _ID + ") ON UPDATE CASCADE);";
	
	
	private static final String CREATE_TABLE_CONTEXTITEM = 
			"CREATE TABLE IF NOT EXISTS " + TABLE_CONTEXTITEM
			+ "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ ITEM_ID + " INTEGER NOT NULL UNIQUE,"
			+ " FOREIGN KEY(" + ITEM_ID + ") REFERENCES " + TABLE_ITEM + "(" + _ID + ") ON UPDATE CASCADE);";
	
	
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
			+ ITEM_ID + " INTEGER NOT NULL UNIQUE, "
			+ TEMPERATURE + " TEXT, "
			+ WEATHER + " TEXT, "
			+ LIGHTNESS + " TEXT,"
			+ " FOREIGN KEY(" + ITEM_ID + ") REFERENCES " + TABLE_ITEM + "(" + _ID + ") ON UPDATE CASCADE ON DELETE SET DEFAULT);";

	
	private static final String CREATE_TABLE_ACTIVITY = 
			"CREATE TABLE IF NOT EXISTS " + TABLE_ACTIVITY 
			+ "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ NAME + " TEXT NOT NULL UNIQUE, " 
			+ LOCATION_ID + " INTEGER DEFAULT NULL,"
			+ " FOREIGN KEY(" + LOCATION_ID + ") REFERENCES " + TABLE_LOCATION + "(" + _ID + ") ON UPDATE CASCADE ON DELETE SET DEFAULT);";
	
	
    private static final String CREATE_TABLE_LOCATION = 
    		"CREATE TABLE IF NOT EXISTS " + TABLE_LOCATION
    		+ "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ NAME + " TEXT NOT NULL UNIQUE, "
			+ LON + " REAL, "
			+ LAT + " REAL, "
			+ RADIUS + " REAL, "
			+ MAC + " TEXT);";
    
    
    private static final String CREATE_TABLE_PHOTO =
    		"CREATE TABLE IF NOT EXISTS " + TABLE_TAGID
			+ "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ ITEM_ID + " INTEGER NOT NULL UNIQUE, " 
			+ _DATA + " BLOB,"
			+ " FOREIGN KEY(" + ITEM_ID + ") REFERENCES " + TABLE_ITEM + "(" + _ID + ") ON UPDATE CASCADE ON DELETE SET DEFAULT);";
	
    private final Context context;
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
		
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
		
		Category c = item.getCategory();
		long cid = 0;
		
		if(c != null){
			cid = c.getId();
		}
		
		Log.w("TEST", "getIds(): " + item.getIds());
		
//		String tag_id = null;
//		if(item.getIds() != null){
//			
//		}
		
//		if(item.getIds().isEmpty() == false){
//			tag_id = item.getIds().get(0);
//		}
		
		ContentValues values = new ContentValues();
		values.put(NAME, item.getName());
		//values.put(CATEGORY_ID, item.getCategory().getId());
		values.put(CATEGORY_ID, cid);
		
		// Insert row to Item table
		long item_id = db.insert(TABLE_ITEM, null, values);
		
		// Insert tag_id in TagId Table if tag_id not null
//		if (tag_id != null) {
//			addTagId(item_id, tag_id);
//		}

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
		
		// If Photo exists, add photo to Photo table
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
		db.delete(TABLE_CONTEXTITEM, ITEM_ID + " = " + item_id, null);
		db.delete(TABLE_INDEPENDENTITEM, ITEM_ID + " = " + item_id, null);
		db.delete(TABLE_PHOTO, ITEM_ID + " = " + item_id, null);
		db.delete(TABLE_ACTIVITYITEM, ITEM_ID + " = " + item_id, null);
		db.delete(TABLE_ITEMATTRIBUTE, ITEM_ID + " = " + item_id, null);
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
	
	public void updateItem(Item item) throws DatabaseException {
		
		SQLiteDatabase db = this.getWritableDatabase();
		long id = item.getId();
		
		ContentValues values = new ContentValues();
		values.put(NAME, item.getName());
		values.put(CATEGORY_ID, item.getCategory().getId());
		
		Log.w("TEST", "UPDATE CONTENTRESOLVER: " + values);
		db.update(TABLE_ITEM, values, _ID + " = " + id, null);
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
		
		String name = itemData.getString(itemData.getColumnIndex(NAME));
		Item item = new Item(name);
		
		return item;
	}
	
	
	@Override
	public Item getItemByName(String name) throws DatabaseException {
		
		SQLiteDatabase db = this.getReadableDatabase();
		
		long category;
		
		String selectQuery = "SELECT * FROM " + TABLE_ITEM + " WHERE " + NAME + " = '" + name + "'";
		
		Log.e(LOG, selectQuery);
		
		Cursor itemData = db.rawQuery(selectQuery, null);
		itemData.moveToFirst();
		
		long test1 = itemData.getInt(0);
		String test2 = itemData.getString(1);
		int test3 = itemData.getInt(2);
		String tagID = itemData.getString(itemData.getColumnIndex(TAG_ID));
		
		Log.w("TEST", "Cursor erzeugt: ID " + test1 + " Name " + test2 + " CatID " + test3 + " TagID " + tagID);
		
		//category = 1;
		
		String getCategoryQuery = "SELECT * FROM " + TABLE_CATEGORY + " WHERE " + _ID + " = " + test3;
		
		Cursor categoryName = db.rawQuery(getCategoryQuery, null);
		
		int test4;
		String test5;
		Category cat = null;
		
		if(categoryName != null){
			categoryName.moveToFirst();
			
			test4 = categoryName.getInt(0);
			test5 = categoryName.getString(1);
			Log.w("TEST", "CatID " + test4 + " Name " + test5);
			
			cat = new Category(test4, test5);
			Log.w("TEST", "Cat: " + cat);
		} 
		
		if (itemData != null) {
			itemData.moveToFirst();
			
			Item item = new Item(itemData.getInt(itemData.getColumnIndex(_ID)), 
								name, 
								cat, 
								0, 
								false, 
								false, 
								null, 
								tagID);
			
			Log.w("TEST", "Neues Item: " + item);
			
			return item;
		} else {
			return null;
		}
		
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
						String name = c.getString(c.getColumnIndex(NAME));
						int item_id = c.getInt(c.getColumnIndex(_ID));
						int cat = c.getInt(c.getColumnIndex(CATEGORY_ID));
						
						Log.w("TEST", "GET ALL ITEMS: NAME: " + name + ", KATEGORIEID: " + cat);
						
						Category category = getCategory(cat);
						
						Log.w("TEST", "Kategorie: " + category);
						
						Item item = new Item(name, category);
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
	
	
	public List<String> getTagId(long itemId) throws DatabaseException {
		
		SQLiteDatabase db = this.getReadableDatabase();
		
		List<String> id = new ArrayList<String>();
		
		String selectQuery = "SELECT " + TAG_ID + " FROM " + TABLE_TAGID + " WHERE " + ITEM_ID + " = " + itemId;
		
		Cursor c = db.rawQuery(selectQuery, null);
		Log.w("TEST", "TAGID-Cursor: " + c);
		
		// looping through all rows and adding them to list
		if(c.moveToFirst()) {
			do {
				String tag_id = new String(c.getString(c.getColumnIndex(TAG_ID)));
				id.add(tag_id);
			} while(c.moveToNext());
		}
		
		return id;
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
	
	public boolean getIndependentItem(long id) throws DatabaseException {
		
		SQLiteDatabase db = this.getReadableDatabase();
		boolean item = false;
		
		String selectQuery = "SELECT " + ITEM_ID + " FROM " + TABLE_INDEPENDENTITEM + " WHERE " + ITEM_ID + " = " + id;
		
		Cursor c = db.rawQuery(selectQuery, null);
		
		if(c == null){
			return item;
		} else {
			item = true;
			return item;
		}
		
	}
	
	public List<Long> getIndependentItems() throws DatabaseException {
		
		SQLiteDatabase db = this.getReadableDatabase();
//		long[] items = new long[]{};
		List<Long> items = new ArrayList<Long>();
		
		String selectQuery = "SELECT " + ITEM_ID + " FROM " + TABLE_INDEPENDENTITEM;
		
		Cursor c = db.rawQuery(selectQuery, null);
		
		if(c == null) {
			return items;
		} else {
			if(c.moveToFirst()) {
				do {
					items.add(c.getLong(c.getColumnIndex(ITEM_ID)));
				} while(c.moveToNext());
			}
			return items;
		}
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
	
	
	public boolean getContextItem(long id) throws DatabaseException {
		
		SQLiteDatabase db = this.getReadableDatabase();
		boolean item = false;
		
		String selectQuery = "SELECT " + ITEM_ID + " FROM " + TABLE_CONTEXTITEM + " WHERE " + ITEM_ID + " = " + id;
		
		Cursor c = db.rawQuery(selectQuery, null);
		
		if(c == null){
			return item;
		} else {
			item = true;
			return item;
		}
		
	}
	
	public List<Long> getContextItems() throws DatabaseException {
		
		SQLiteDatabase db = this.getReadableDatabase();
		List<Long> items = new ArrayList<Long>();
		
		String selectQuery = "SELECT " + ITEM_ID + " FROM " + TABLE_CONTEXTITEM;
		
		Cursor c = db.rawQuery(selectQuery, null);
		
		if(c == null) {
			return items;
		} else {
			if(c.moveToFirst()) {
				do {
					items.add(c.getLong(c.getColumnIndex(ITEM_ID)));
				} while(c.moveToNext());
			}
			return items;
		}
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
		long activity_id = toEdit.getId();
		
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
		
		db.delete(TABLE_CATEGORY, NAME + " = '" + name + "'", null);
	}


	@Override
	public void editCategory(Category toEdit, Category after) throws DatabaseException {

		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		long cat_id = toEdit.getId();
		
		if(toEdit.getName() != after.getName()){
			
			values.put(NAME, after.getName());
		}
		
		long id = db.update(TABLE_CATEGORY, values, _ID + " = " + cat_id, null);
	}
	
	
	public void updateCategory(Category category) throws DatabaseException {

		SQLiteDatabase db = this.getWritableDatabase();
		long id = category.getId();
		
		ContentValues values = new ContentValues();
		values.put(NAME, category.getName());
		
		db.update(TABLE_CATEGORY, values, _ID + " = " + id, null);
		
	}


	public Category getCategory(String name) throws DatabaseException {
		
		String selectQuery = "SELECT * FROM " + TABLE_CATEGORY + " WHERE " + NAME + " = '" + name + "'";
		
		Log.e(LOG, selectQuery);
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		Category category = null;
		
		if(c.moveToFirst()){
				category = new Category(c.getInt(c.getColumnIndex(_ID)), c.getString(c.getColumnIndex(NAME)));
		}
		
		return category;
	}
	
	
	public Category getCategory(long id) throws DatabaseException {
		
		String selectQuery = "SELECT * FROM " + TABLE_CATEGORY + " WHERE " + _ID + " = " + id;
		
		Log.e(LOG, selectQuery);
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		Category category = null;
		
		if(c.moveToFirst()){
				category = new Category(c.getInt(c.getColumnIndex(_ID)), c.getString(c.getColumnIndex(NAME)));
		}
		
		return category;
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
//					Category category = new Category(c.getString(c.getColumnIndex(NAME)));
					Category category = new Category(c.getInt(c.getColumnIndex(_ID)), c.getString(c.getColumnIndex(NAME)));
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


	@Override
	public Item getItem(String tagId) throws DatabaseException {
		// TODO implement!
		return DatabaseConnector.getItem(tagId);
	}


	@Override
	public void putImage(Bitmap bmp) throws DatabaseException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public Bitmap getImage(int hashCode) throws DatabaseException {
		// TODO implement, this is just dummy code
		return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
	}
	
}











