package de.uniulm.bagception.bagceptionmastercontrolserver.database;

import java.io.ByteArrayOutputStream;
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
import de.uniulm.bagception.bundlemessageprotocol.entities.Activity;
import de.uniulm.bagception.bundlemessageprotocol.entities.Category;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.bundlemessageprotocol.entities.ItemAttribute;
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
	private static final String IMAGE = "_data";
	private static final String IMAGE_HASH = "imgHash";
	
	
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
			+ CATEGORY_ID + " INTEGER," 
			+ " FOREIGN KEY(" + ACTIVITY_ID + ") REFERENCES " + TABLE_ACTIVITY + "(" + _ID + ") ON UPDATE CASCADE,"
			+ " FOREIGN KEY(" + ITEM_ID + ") REFERENCES " + TABLE_ITEM + "(" + _ID + ") ON UPDATE CASCADE ON DELETE SET DEFAULT,"
			+ " FOREIGN KEY(" + CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORY + "(" + _ID + ") ON UPDATE CASCADE ON DELETE SET DEFAULT);";
	
	
	private static final String CREATE_TABLE_ITEMATTRIBUTE = 
			"CREATE TABLE IF NOT EXISTS " + TABLE_ITEMATTRIBUTE
			+ "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ ITEM_ID + " INTEGER NOT NULL UNIQUE, "
			+ TEMPERATURE + " TEXT DEFAULT NULL, "
			+ WEATHER + " TEXT DEFAULT NULL, "
			+ LIGHTNESS + " TEXT DEFAULT NULL,"
			+ " FOREIGN KEY(" + ITEM_ID + ") REFERENCES " + TABLE_ITEM + "(" + _ID + ") ON UPDATE CASCADE);";

	
	private static final String CREATE_TABLE_ACTIVITY = 
			"CREATE TABLE IF NOT EXISTS " + TABLE_ACTIVITY 
			+ "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ NAME + " TEXT NOT NULL UNIQUE, " 
			+ LOCATION_ID + " INTEGER DEFAULT -1,"
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
    		"CREATE TABLE IF NOT EXISTS " + TABLE_PHOTO
			+ "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ ITEM_ID + " INTEGER NOT NULL, " 
			+ IMAGE_HASH + " INTEGER, "
			+ IMAGE + " BLOB,"
			+ " FOREIGN KEY(" + ITEM_ID + ") REFERENCES " + TABLE_ITEM + "(" + _ID + ") ON UPDATE CASCADE);";
	
    
    @SuppressWarnings("unused")
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
		
		List<String> tag_ids = item.getIds();
		
		ContentValues values = new ContentValues();
		values.put(NAME, item.getName());
		values.put(CATEGORY_ID, cid);

		
		// Insert row to Item table
		long item_id = db.insert(TABLE_ITEM, null, values);
		Log.w("TEST", "ItemID beim einfügen: " + item_id);
		
		// Insert "TagID" in the correct table		
		if(tag_ids != null){
			addTagIds(item_id, tag_ids);
		}
		
		// If "independentItem" is selected, add item to IndependentItem table
		boolean independentItem = item.getIndependentItem();
		if (independentItem == true) {
			Log.w("TEST", "Ich füge jetzt ein IndependentItem hinzu: " + independentItem);
			addIndependentItem(item_id);
		}
		
		// If "contextItem" is selected, add item to ContextItem table
		boolean contextItem = item.getContextItem();
		if (contextItem == true) {
			Log.w("TEST", "Ich füge jetzt ein ContextItem hinzu: " + contextItem);
			addContextItem(item_id);
		}
		
		// If attributes != null
		ItemAttribute iA = item.getAttribute();
		Log.w("TEST", "Attr.: " + iA);
		if(iA != null) {
			Log.w("TEST", "Attribute einfügen!");
			addItemAttribute(item_id, item);
		}
		
		// If Photo exists, add photo to Photo table
//		int image = item.getImageHash();
		Bitmap bmp = item.getImage();
		
		if(bmp != null){
			addImage(item_id, bmp);
		}
		
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
	}
	
	public void updateItem(Item item) throws DatabaseException {
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		long id = item.getId();
		List<String> tag_ids = item.getIds();
		
		ContentValues values = new ContentValues();
		values.put(NAME, item.getName());
		values.put(CATEGORY_ID, item.getCategory().getId());
		
		
		// Insert "TagID" in the correct table		
		if(tag_ids != null){
			addTagIds(id, tag_ids);
		}
				
		// If "independentItem" is selected, add item to IndependentItem table
		boolean independentItem = item.getIndependentItem();
		if (independentItem == true) {
			addIndependentItem(id);
		} else {
			List<Long> ids = getIndependentItems();
			
			for(int j = 0; j < ids.size(); j++){
				if(ids.get(j) == id){
					deleteIndependentItem(id);
				}
			}
		}
				
		// If "contextItem" is selected, add item to ContextItem table
		boolean contextItem = item.getContextItem();
		if (contextItem == true) {
			addContextItem(id);
		} else {
			List<Long> ids = getContextItems();
			
			for(int j = 0; j < ids.size(); j++){
				if(ids.get(j) == id){
					deleteContextItem(id);
				}
			}
		}
				
		// If attributes != null
		ItemAttribute iA = item.getAttribute();
		Log.w("TEST", "Attr.: " + iA);
		if(iA != null) {
			Log.w("TEST", "Attribute einfügen!");
			addItemAttribute(id, item);
		}
				
		// If Photo exists, add photo to Photo table
//		int image = item.getImageHash();
		Bitmap bmp = item.getImage();
			
		if(bmp != null){
			addImage(id, bmp);
		}
				
		
//		Log.w("TEST", "UPDATE CONTENTRESOLVER: " + values);
		db.update(TABLE_ITEM, values, _ID + " = " + id, null);
	}
	
	
	@SuppressWarnings("unused")
	@Override
	public Item getItem(long id) throws DatabaseException {
		
		SQLiteDatabase db = this.getReadableDatabase();
		
		String selectQuery = "SELECT * FROM " + TABLE_ITEM + " WHERE " + _ID + " = " + id;
		
		Log.e(LOG, selectQuery);
		
		Cursor itemData = db.rawQuery(selectQuery, null);
		itemData.moveToFirst();
		
		int category_id = itemData.getInt(2);

		
		// Get category
		String getCategoryQuery = "SELECT * FROM " + TABLE_CATEGORY + " WHERE " + _ID + " = " + category_id;
	
		Cursor categoryName = db.rawQuery(getCategoryQuery, null);
		
		int catID;
		String catName;
		Category cat = null;
		
		if(categoryName != null){
			categoryName.moveToFirst();
			
			catID = categoryName.getInt(0);
			catName = categoryName.getString(1);
			
			cat = new Category(catID, catName);
		} 
		
		
		boolean isIndependent = getIndependentItem(id);
		boolean isContextItem = getContextItem(id);
		ItemAttribute attributes = getItemAttribute(id);
		List<String> tids = getTagId(id);
		String[] tagids = new String[tids.size()];
		
		if(tids.size() > 0){
			for(int j = 0; j < tids.size(); j++){
				
				tagids[j] = tids.get(j);
			}
		}

		Item item = null;
		
		if (itemData.getCount() > 0) {
			itemData.moveToFirst();
			
			item = new Item(	id, 
								itemData.getString(itemData.getColumnIndex(NAME)), 
								cat, 
								0, 
								isContextItem, 
								isIndependent, 
								attributes, 
								tagids);
			
//			Log.w("TEST", "Neues Item: " + item);
			
			return item;
		} else {
			return item;
		}
	}
	
	
	@SuppressWarnings("unused")
	@Override
	public Item getItemByName(String name) throws DatabaseException {
		
		SQLiteDatabase db = this.getReadableDatabase();
		
		String selectQuery = "SELECT * FROM " + TABLE_ITEM + " WHERE " + NAME + " = '" + name + "'";
		
		Log.e(LOG, selectQuery);
		
		Cursor itemData = db.rawQuery(selectQuery, null);
		itemData.moveToFirst();
		
		long item_id = itemData.getLong(itemData.getColumnIndex(_ID));
		String item_name = itemData.getString(itemData.getColumnIndex(NAME));
		int category_id = itemData.getInt(itemData.getColumnIndex(CATEGORY_ID));

		
		// Get category
		String getCategoryQuery = "SELECT * FROM " + TABLE_CATEGORY + " WHERE " + _ID + " = " + category_id;
	
		Cursor categoryName = db.rawQuery(getCategoryQuery, null);
		
		int catID;
		String catName;
		Category cat = null;
		
		if(categoryName != null){
			categoryName.moveToFirst();
			
			catID = categoryName.getInt(0);
			catName = categoryName.getString(1);
			
			cat = new Category(catID, catName);
		} 
		
		
				
		Item item = null;
		
		if (itemData != null) {
			itemData.moveToFirst();
			
			item = new Item(itemData.getInt(itemData.getColumnIndex(_ID)), 
								name, 
								cat, 
								0, 
								false, 
								false, 
								null, 
								new ArrayList<String>());
			
			Log.w("TEST", "Neues Item: " + item);
			
			return item;
		} else {
			return item;
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
			if(c.moveToFirst() && c.getCount() > 0) {
				do {
						String name = c.getString(c.getColumnIndex(NAME));
						int item_id = c.getInt(c.getColumnIndex(_ID));
						int cat = c.getInt(c.getColumnIndex(CATEGORY_ID));
						
						Log.w("TEST", "GET ALL ITEMS: NAME: " + name + ", KATEGORIEID: " + cat);
						
						Category category = getCategory(cat);
						
						Log.w("TEST", "Kategorie: " + category);
						
						Item item = new Item(item_id, name, category);
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
		
		db.insert(TABLE_TAGID, null, values);
		
		//return id;
	}
	
	
	public void addTagIds(long item_id, List<String> tag_ids) throws DatabaseException {
		
		for(int j = 0; j < tag_ids.size(); j++){
			
			String tag_id = tag_ids.get(j);
			addTagId(item_id, tag_id);
		}
	}
	
	
	public void deleteTagId(String tag_id) throws DatabaseException {
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.delete(TABLE_TAGID, TAG_ID + " = ?" , new String[] {tag_id});
		
	}
	
	
	@SuppressWarnings("unused")
	public Long getItemId(String tag_id) throws DatabaseException {
		
		SQLiteDatabase db = this.getReadableDatabase();
		
		long item_id = -1;
		
		String selectQuery = "SELECT " + ITEM_ID + " FROM " + TABLE_TAGID + " WHERE " + TAG_ID + " = '" + tag_id + "'";
		
		Cursor c = db.rawQuery(selectQuery, null);
		
		if(c.getCount() > 0){
			c.moveToFirst();
			item_id = c.getLong(c.getColumnIndex(ITEM_ID));
			return item_id;
		} else{
			return item_id;
		}
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
		
		db.insert(TABLE_INDEPENDENTITEM, null, values);
		
		//return id;
	}
	
	public void deleteIndependentItem(long item_id) throws DatabaseException {
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.delete(TABLE_INDEPENDENTITEM, ITEM_ID + " = " + item_id, null);
	}
	
	public boolean getIndependentItem(long id) throws DatabaseException {
		
		SQLiteDatabase db = this.getReadableDatabase();
		boolean item = false;
		
		String selectQuery = "SELECT * FROM " + TABLE_INDEPENDENTITEM + " WHERE " + ITEM_ID + " = " + id;
		
		Cursor c = db.rawQuery(selectQuery, null);
		
		if(!(c.moveToFirst()) || c.getCount() == 0){
			item = false;
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
		
		if(!(c.moveToFirst()) || c.getCount() == 0) {
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
		
		db.insert(TABLE_CONTEXTITEM, null, values);
		
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
		Log.w("TEST", "Cursor ContextItem: " + c.getCount());
		
		if(!(c.moveToFirst()) || c.getCount() == 0){
			item = false;
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
		
		if(!(c.moveToFirst()) || c.getCount() == 0) {
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
	
	
	// -------------------------------- "ItemAttribute" table methods -------------------------------- //
	
	public void addItemAttribute(long item_id, Item item) throws DatabaseException {
		
		Log.w("TEST", "In der addAttribute Methode");
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		ItemAttribute iA = item.getAttribute();
		Log.w("TEST", "Attribute: " + iA);
		
//		long item_id = item.getId();
		Log.w("TEST", "Item ID: " + item_id);
		
		String temp = iA.getTemperature();
		String weather = iA.getWeather();
		String light = iA.getLightness();
		Log.w("TEST", "Attribute: " + temp + weather + light);
		
		ContentValues values = new ContentValues();
		values.put(ITEM_ID, item_id);
		values.put(TEMPERATURE, temp);
		values.put(WEATHER, weather);
		values.put(LIGHTNESS, light);
		
		long id = db.insert(TABLE_ITEMATTRIBUTE, null, values);
		Log.w("TEST", "AttributeID: " + id);
	}
	
	
	public void deleteItemAttributes(long item_id) throws DatabaseException {
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.delete(TABLE_ITEMATTRIBUTE, ITEM_ID + " = " + item_id, null);
	}
	
	
	public ItemAttribute getItemAttribute(long id) throws DatabaseException {
		
		SQLiteDatabase db = this.getReadableDatabase();
		ItemAttribute attributes = null;
		
		String selectQuery = "SELECT * FROM " + TABLE_ITEMATTRIBUTE + " WHERE " + ITEM_ID + " = " + id;
		
		Cursor c = db.rawQuery(selectQuery, null);
		
		if(c == null){
			Log.w("TEST", "Cursor is null");
			return attributes;
		} else {
			
			long row_id = -1;
			long item_id = -1;
			String temperature = null;
			String weather = null;
			String lightness = null;

			c.moveToFirst();
			
			if(c.getLong(c.getColumnIndex(_ID)) != -1){
				row_id = c.getLong(c.getColumnIndex(_ID));
			}
			
			if(c.getLong(c.getColumnIndex(ITEM_ID)) != -1){
				item_id = c.getLong(c.getColumnIndex(ITEM_ID));
			}
			
			if(c.getString(c.getColumnIndex(TEMPERATURE)) != null){
				temperature = c.getString(c.getColumnIndex(TEMPERATURE));
				//Log.w("TEST", "Temp: " + temperature);
			}
				
			if(c.getString(c.getColumnIndex(WEATHER)) != null){
				weather = c.getString(c.getColumnIndex(WEATHER));
				//Log.w("TEST", "Weather: " + weather);
			}
				
			if(c.getString(c.getColumnIndex(LIGHTNESS)) != null){
				lightness = c.getString(c.getColumnIndex(LIGHTNESS));
				//Log.w("TEST", "Light: " + lightness);
			}
			
			attributes = new ItemAttribute(row_id, item_id, temperature, weather, lightness);
			
			return attributes;
		}
		
	}
	
	
	public List<ItemAttribute> getItemAttributes() throws DatabaseException {
		
		SQLiteDatabase db = this.getReadableDatabase();
		
		String selectQuery = "SELECT * FROM " + TABLE_ITEMATTRIBUTE;
		List<ItemAttribute> attributes = new ArrayList<ItemAttribute>();
		
		Cursor c = db.rawQuery(selectQuery, null);
		Log.w("TEST", "Cursor: " + c);
		
		if(c == null) {
			return null;
		} else {
			if(c.moveToFirst()) {
				do {
					long id = c.getLong(c.getColumnIndex(_ID));
					long item_id = c.getLong(c.getColumnIndex(ITEM_ID));
					String temp = c.getString(c.getColumnIndex(TEMPERATURE));
					String weather = c.getString(c.getColumnIndex(WEATHER));
					String light = c.getString(c.getColumnIndex(LIGHTNESS));
					
					ItemAttribute a = new ItemAttribute(id, item_id, temp, weather, light);
					attributes.add(a);
				} while(c.moveToNext());
			}
			return attributes;
		}
	}
	
	
	public void updateItemAttributes(ItemAttribute attributes) throws DatabaseException {
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		long id = attributes.getId();
		Log.w("TEST", "ID: " + id);
		
		long item_id = -1;
		String temperature = null;
		String weather = null;
		String lightness = null;
		
		if(attributes.getItemId() != -1){
			item_id = attributes.getItemId();
		}
		
		if(attributes.getTemperature() != null){
			temperature = attributes.getTemperature();
		}
		
		if(attributes.getWeather() != null){
			weather = attributes.getWeather();
		}
		
		if(attributes.getLightness() != null){
			lightness = attributes.getLightness();
		}
		
		
		ContentValues values = new ContentValues();
		values.put(ITEM_ID, item_id);
		values.put(TEMPERATURE, temperature);
		values.put(WEATHER, weather);
		values.put(LIGHTNESS, lightness);
		
		Log.w("TEST", "UPDATE CONTENTRESOLVER: " + values);
		db.update(TABLE_ITEMATTRIBUTE, values, _ID + " = " + id, null);
	}
	
	
	// -------------------------------- "Photo" table methods -------------------------------- //
	
	/**
	 * Add Picture
	 */
	public void addPhotoToItem(int image, long item_id) throws DatabaseException {

		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(ITEM_ID, item_id);
		values.put(IMAGE, image); 
		
		db.insert(TABLE_PHOTO, null, values);
		
		//return id;
		
	}
	
	
	public void deletePhoto(long item_id) throws DatabaseException {
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.delete(TABLE_PHOTO, ITEM_ID + " = " + item_id, null);
	}
	
	
	public int updatePhoto(Item item) throws DatabaseException {
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(IMAGE, item.getImageHash());
		
		return db.update(TABLE_PHOTO, values, _ID + " = ?", new String[] {String.valueOf(item.getId())});
	}
	
	
	@Override
	public int getImageHash(long item_id) throws DatabaseException {

		SQLiteDatabase db = this.getReadableDatabase();
		
		int imgHash = 0;
		
		String selectQuery = "SELECT " + IMAGE_HASH + " FROM " + TABLE_PHOTO + " WHERE " + ITEM_ID + " = " + item_id;
		
		Cursor c = db.rawQuery(selectQuery, null);
		
		if(!(c.moveToFirst()) || c.getCount() == 0){
			return imgHash;
		} else {
			c.moveToFirst();
			
			imgHash = c.getInt(c.getColumnIndex(IMAGE_HASH));
			
			return imgHash;
		}
		
		
	}
	
	
	@Override
	public void addImage(long item_id, Bitmap bmp) throws DatabaseException {

		SQLiteDatabase db = this.getWritableDatabase();
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 100, bos);
		
		byte[] bArray = bos.toByteArray();
		Log.w("TEST", "Item_ID: " + item_id);
		Log.w("TEST", "Image: " + bArray);
		
		ContentValues values = new ContentValues();
		values.put(ITEM_ID, item_id);
		values.put(IMAGE, bArray);
		
		long id = db.insert(TABLE_PHOTO, null, values);
		Log.w("TEST", "ID: " + id);
	}


	@Override
	public Bitmap getImage(long item_id) throws DatabaseException {
		
		SQLiteDatabase db = this.getReadableDatabase();

		Log.w("TEST", "Erstelle Cursor");
		String selectQuery = "SELECT " + IMAGE + " FROM " + TABLE_PHOTO + " WHERE " + ITEM_ID + " = " + item_id;
		
		Cursor c = db.rawQuery(selectQuery, null);
		Log.w("TEST", "Photo-Cursor: " + c);
		
		byte[] bimg = new byte[1];
		Bitmap bmp;
		
		if(c != null){
			c.moveToFirst();
			bimg = c.getBlob(c.getColumnIndex(IMAGE));
		}
		
		bmp = BitmapFactory.decodeByteArray(bimg, 0, bimg.length);
	
		return bmp;
	}

	
	@Override
	public Bitmap getImage(int hashCode) throws DatabaseException {

		SQLiteDatabase db = this.getReadableDatabase();
		
		Bitmap bmp = null;
		
		String selectQuery = "SELECT " + IMAGE + " FROM " + TABLE_PHOTO + " WHERE " + IMAGE_HASH + " = " + hashCode;
		
		Cursor c = db.rawQuery(selectQuery, null);
		
		if(!(c.moveToFirst()) || c.getCount() == 0){
			return bmp;
		} else {
			c.moveToFirst();
			
			byte[] bimg = new byte[1];
			bimg = c.getBlob(c.getColumnIndex(IMAGE));
			
			bmp = BitmapFactory.decodeByteArray(bimg, 0, bimg.length);
			
			return bmp;
		}
	}
	

	// -------------------------------- "Activity" table methods -------------------------------- //

	
	@Override
	public void addActivity(Activity activity) throws DatabaseException {

		SQLiteDatabase db = this.getWritableDatabase();
		
		long loc_id = -1;
		
		if(activity.getLocation() != null){
			loc_id = activity.getLocation().getId();
		}
		
		ContentValues values = new ContentValues();
		values.put(NAME, activity.getName());
		values.put(LOCATION_ID, loc_id);
			
		db.insert(TABLE_ACTIVITY, null, values);
		
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
		
		db.update(TABLE_ACTIVITY, values, _ID + " = " + activity_id, null);
	}
	
	
	public void updateActivtiy(Activity activity) throws DatabaseException {
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(NAME, activity.getName());
		values.put(LOCATION_ID, activity.getLocation().getId());
		
//		Log.w("TEST", "LOCID: " + activity.getLocation().getId());
		
		long aID = activity.getId();
		Log.w("TEST", "" + aID);
		
		db.update(TABLE_ACTIVITY, values, _ID + " = ?", new String[] {String.valueOf(activity.getId())});
	}

	
	public Activity getActivity(String name) throws DatabaseException {
		
		SQLiteDatabase db = this.getReadableDatabase();
		Activity activity = null;
		
		String selectQuery = "SELECT * FROM " + TABLE_ACTIVITY + " WHERE " + NAME + " = '" + name + "'";
		
		Cursor c = db.rawQuery(selectQuery, null);
		c.moveToFirst();
		
		long id = c.getLong(c.getColumnIndex(_ID));
		String ac_name = c.getString(c.getColumnIndex(NAME));
		long locid = c.getLong(c.getColumnIndex(LOCATION_ID));
		
		Log.w("TEST", "LocID in Ac " + locid);
		
		Location location = null;
		
		if(locid != -1){
			location = getLocation(locid);
		}
		
		activity = new Activity(id, ac_name, new ArrayList<Item>(), location);
		
		return activity;
	}

	@Override
	public List<Activity> getActivities() throws DatabaseException {

		List<Activity> activities = new ArrayList<Activity>();
		
		String selectQuery = "SELECT * FROM " + TABLE_ACTIVITY;
		
		Log.e(LOG, selectQuery);
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		
		// looping through all rows and adding them to list
		if(c.moveToFirst() && c.getCount() > 0) {
			do {
					long id = c.getLong(c.getColumnIndex(_ID));
					String name = c.getString(c.getColumnIndex(NAME));
					long loc_id = c.getLong(c.getColumnIndex(LOCATION_ID));
					Log.w("TEST", "" + id + name + loc_id);
					
					Location location = null;
					if(loc_id != -1){
						location = getLocation(loc_id);
					}
						
					Activity activity = new Activity(id, name, new ArrayList<Item>(), location);
					activities.add(activity);
			} while(c.moveToNext());
		}
		return activities;
	}
	
	
	
	// -------------------------------- "ActivityItem" table methods -------------------------------- //
	
	@Override
	public void addActivityItem(long activity_id, Item item, Category category) throws DatabaseException {
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(ACTIVITY_ID, activity_id);
		values.put(ITEM_ID, item.getId());
		values.put(CATEGORY_ID, category.getId());
			
		db.insert(TABLE_ACTIVITYITEM, null, values);

	}
	
	public void addActivityItems(long activity_id, List<Item> items, List<Category> categoriesForActivity) throws DatabaseException {

		Item item = null;
		Category category = null;
		
		for(int i = 0; i < items.size(); i++) {
			
			item = items.get(i);
			
			addActivityItem(activity_id, item, null);
		}
		
		for(int c = 0; c < categoriesForActivity.size(); c++) {
			
			category = categoriesForActivity.get(c);
			
			addActivityItem(activity_id, null, category);
		}
		
	}
	
	
	public void deleteActivityItem(long item_id) throws DatabaseException {
		
		SQLiteDatabase db = this.getWritableDatabase();

		db.delete(TABLE_ACTIVITYITEM, ITEM_ID + " = ?", new String[] {String.valueOf(item_id)});
	}
	
	
	public void deleteActivityCategory(long category_id) throws DatabaseException {
		
		SQLiteDatabase db = this.getWritableDatabase();

		db.delete(TABLE_ACTIVITYITEM, CATEGORY_ID + " = ?", new String[] {String.valueOf(category_id)});
	}
	
	
	public List<Long> getActivityItems(long activity_id) throws DatabaseException {
		
		SQLiteDatabase db = this.getReadableDatabase();
		List<Long> items = new ArrayList<Long>();
		
		String selectQuery = "SELECT * FROM " + TABLE_ACTIVITYITEM + " WHERE " + ACTIVITY_ID + " = " + activity_id;
		
		Cursor c = db.rawQuery(selectQuery, null);
		
		if (c != null){
			c.moveToFirst();
			
			while(c.isAfterLast() == false){
				items.add(c.getLong(c.getColumnIndex(ITEM_ID)));
				items.add(c.getLong(c.getColumnIndex(CATEGORY_ID)));
				c.moveToNext();
			}
			
			return items;
		} else {		
			return items;
		}
	}
		
	
	
	
	// -------------------------------- "Category" table methods -------------------------------- //

	@Override
	public void addCategory(Category category) throws DatabaseException {

		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(NAME, category.getName());
		
		db.insert(TABLE_CATEGORY, null, values);
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
		
		db.update(TABLE_CATEGORY, values, _ID + " = " + cat_id, null);
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
		
		db.insert(TABLE_LOCATION, null, values);
	}


	@Override
	public void deleteLocation(Location location) throws DatabaseException {

		SQLiteDatabase db = this.getWritableDatabase();
		
		String name = location.getName();
		
		db.delete(TABLE_LOCATION, NAME + " = '" + name + "'", null);
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
		
		db.update(TABLE_LOCATION, values, _ID + " = " + toEdit.getId(), null);
	}
	
	
	public void updateLocation(Location location) throws DatabaseException {
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
//		values.put(_ID, location.getId());
		values.put(NAME, location.getName());
		values.put(LAT, location.getLat());
		values.put(LON, location.getLng());
		values.put(RADIUS, location.getRadius());
		values.put(MAC, location.getMac());
		
		db.update(TABLE_LOCATION, values, _ID + " = ?", new String[] {String.valueOf(location.getId())});
		
	}
	
	public Location getLocation(String name) throws DatabaseException {
		
		Location location = null;
		SQLiteDatabase db = this.getReadableDatabase();
		
		String selectQuery = "SELECT * FROM " + TABLE_LOCATION + " WHERE " + NAME + " = '" + name + "'";
		
		Cursor c = db.rawQuery(selectQuery, null);
		c.moveToFirst();
		
		long id = c.getLong(c.getColumnIndex(_ID));
		String locname = c.getString(c.getColumnIndex(NAME));
		float lat = c.getFloat(c.getColumnIndex(LAT));
		float lng = c.getFloat(c.getColumnIndex(LON));
		int radius = c.getInt(c.getColumnIndex(RADIUS));
		String mac = c.getString(c.getColumnIndex(MAC));
		
		location = new Location(id, locname, lat, lng, radius, mac);
		Log.w("TEST", "Location: " + location);
		
		
		return location;
	}
	
	
	public Location getLocation(long loc_id) throws DatabaseException {
		
		Location location = null;
		SQLiteDatabase db = this.getReadableDatabase();
		
		String selectQuery = "SELECT * FROM " + TABLE_LOCATION + " WHERE " + _ID + " = '" + loc_id + "'";
		
		Cursor c = db.rawQuery(selectQuery, null);
		c.moveToFirst();
		
		long id = c.getLong(c.getColumnIndex(_ID));
		String locname = c.getString(c.getColumnIndex(NAME));
		float lat = c.getFloat(c.getColumnIndex(LAT));
		float lng = c.getFloat(c.getColumnIndex(LON));
		int radius = c.getInt(c.getColumnIndex(RADIUS));
		String mac = c.getString(c.getColumnIndex(MAC));
		
		location = new Location(id, locname, lat, lng, radius, mac);
		Log.w("TEST", "Location: " + location);
		
		
		return location;
	}


	@Override
	public List<Location> getLocations() throws DatabaseException {

		List<Location> locations = new ArrayList<Location>();
		
		String selectQuery = "SELECT * FROM " + TABLE_LOCATION;
		
		Log.e(LOG, selectQuery);
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		
		long id = -1;
		String name = null;
		float lat = -1;
		float lng = -1;
		int radius = -1;
		String mac = null;
		
		// looping through all rows and adding them to list
		if(c.moveToFirst()) {
			do {
					Log.w("TEST", "Variablen werden initilisiert");
				
					id = c.getLong(c.getColumnIndex(_ID));
					name = c.getString(c.getColumnIndex(NAME));
					lat = c.getFloat(c.getColumnIndex(LAT));
					lng = c.getFloat(c.getColumnIndex(LON));
					radius = c.getInt(c.getColumnIndex(RADIUS));
					mac = c.getString(c.getColumnIndex(MAC));
					
					Location location = new Location(id, name, lat, lng, radius, mac);
					locations.add(location);
					
			} while(c.moveToNext());
		}
		return locations;
	}


	@Override
	public void addActivityItem(long activity_id, List<Item> itemsForActivity) {
		// TODO Auto-generated method stub
		
	}
	
}











