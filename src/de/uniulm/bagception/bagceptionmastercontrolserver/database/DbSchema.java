package de.uniulm.bagception.bagceptionmastercontrolserver.database;

import android.provider.BaseColumns;

public interface DbSchema {
	
	// Database name
	String DB_NAME = "bagception.db";
	
	// Database tables
	String TBL_ITEMS = "items";
	String TBL_PHOTOS = "photos";
	String TBL_CATEGORIES = "categories";
	String TBL_ACTIVITIES = "activities";
	String TBL_ACTIVITYITEMS = "activityitems";
	String TBL_LOCATIONS = "locations";
	String TBL_ITEMCONTEXT = "itemcontext";
	String TBL_WEATHER = "weather";
	String TBL_TIME = "time";
	
	// Columns TBL_ITEMS
	String COL_ID_ITEM = BaseColumns._ID;
	String COL_ITEMNAME = "itemname";
	String COL_TAGID = "tag_id";
	String COL_VISIBILITY = "visibility";
	String COL_ACTIVITY_IND = "activityIndependent";
	String COL_CATEGORY = "category";

	// Columns TBL_PHOTOS
	String COL_ID_PHOTO = "_id";
	String COL_DATA = "_data";
	String COL_ITEMS = "items_id";
	
	// Columns TBL_CATEGORIES
	String COL_ID_CATEGORY = "_id";
	String COL_CATEGORYNAME = "categoryname";
	
	// Columns TBL_ACTIVITIES
	String COL_ID_ACTIVITY = "_id";
	String COL_ACTIVITYNAME = "activityname";
	String COL_ACTIVITYLOCATION = "location";
	
	// Columns TBL_ACTIVITYITEMS
	String COL_ID_ACTIVITYITEMS = "_id";
	String COL_ACTIVITY = "activity";
	String COL_ITEM = "item";
	// COL_CATEGORY
	
	// Columns TBL_LOCATION
	String COL_ID_LOC = "_id";
	String COL_LOCATIONNAME = "locationname";
	String COL_LON = "longitude";
	String COL_LAT = "latitude";
	String COL_RADIUS = "radius";
	String COL_MAC = "mac";
	
	// Columns TBL_ITEMCONTEXT
	String COL_ID_CONTEXT = "_id";
	String COL_ITEMID = "item";
	String COL_WEATHERID = "weatherid";
	String COL_TIMEID = "timeid";
	
	// Columns TBL_WEATHER
	String COL_ID_WEATHER = "_id";
	String COL_WEATHERNAME = "name";
	String COL_TEMPERATURE = "temperature";
	String COL_WEATHER = "weather";
	
	// Columns TBL_TIME
	String COL_ID_TIME = "_id";
	String COL_TIMENAME = "name";
	String COL_DATE = "date";
	String COL_TIME = "time";
	
	
	
	// Strings to create tables		
	String CREATE_TBL_ITEMS =
			"CREATE TABLE " + TBL_ITEMS + " (" +
			COL_ID_ITEM +		" INTEGER PRIMARY KEY AUTOINCREMENT," + 
			COL_ITEMNAME + 		" TEXT," +
			COL_TAGID + 		" TEXT," +
			COL_VISIBILITY +	" TEXT," +
			COL_ACTIVITY_IND + 	" TEXT," +
			COL_CATEGORY + 		" TEXT" +
			//" FOREIGN KEY (" + COL_CATEGORY + ") REFERENCES " + TBL_CATEGORIES + " (" + COL_ID_CATEGORY + ");" + 
			")";
	
	String CREATE_TBL_PHOTOS =
			"CREATE TABLE " + TBL_PHOTOS + " (" +
			COL_ID_PHOTO + 	" INTEGER PRIMARY KEY AUTOINCREMENT," + 
			COL_DATA + 		" TEXT," +
			COL_ITEMS +		" INTEGER NOT NULL" +
			//" FOREIGN KEY (" + COL_ITEMS + ") REFERENCES " + TBL_ITEMS + " (" + COL_ID_ITEM + ");" +
			")";
	
	String CREATE_TBL_CATEGORIES =
			"CREATE TABLE " + TBL_CATEGORIES + " (" +
			COL_ID_CATEGORY + " INTEGER PRIMARY KEY AUTOINCREMENT," +
			COL_CATEGORYNAME + " TEXT" +
			")";
	
	String CREATE_TBL_ACTIVITIES = 
			"CREATE TABLE " + TBL_ACTIVITIES + " (" +
			COL_ID_ACTIVITY + " INTEGER PRIMARY KEY AUTOINCREMENT," + 
			COL_ACTIVITYNAME + " TEXT," +
			COL_ACTIVITYLOCATION + " INTEGER" +
			//" FOREIGN KEY (" + COL_ACTIVITYLOCATION + ") REFERENCES " + TBL_LOCATIONS + " (" + COL_ID_LOC + ");" +
			")";
	
	String CREATE_TBL_ACTIVITYITEMS =
			"CREATE TABLE " + TBL_ACTIVITYITEMS + " (" +
			COL_ID_ACTIVITYITEMS + " INTEGER PRIMARY KEY AUTOINCREMENT," + 
			COL_ACTIVITY + " TEXT," +
			COL_ITEM + " TEXT" +
			COL_CATEGORY + " TEXT" +
//			" FOREIGN KEY (" + COL_ACTIVITY + ") REFERENCES " + TBL_ACTIVITIES + " (" + COL_ID_ACTIVITY + ");" +
//			" FOREIGN KEY (" + COL_ITEM + ") REFERENCES " + TBL_ITEMS + " (" + COL_ID_ITEM + ");" +
//			" FOREIGN KEY (" + COL_CATEGORY + ") REFERENCES " + TBL_LOCATIONS + " (" + COL_ID_CATEGORY + ");" +
			")";
	
	String CREATE_TBL_LOCATIONS =
			"CREATE TABLE " + TBL_LOCATIONS + " (" +
			COL_ID_LOC + " INTEGER PRIMARY KEY AUTOINCREMENT," + 
			COL_LOCATIONNAME + " TEXT," +
			COL_LON + " TEXT," +
			COL_LAT + " TEXT," +
			COL_RADIUS + " TEXT," +
			COL_MAC + " TEXT" +
			")";
	
	String CREATE_TBL_ITEMCONTEXT =
			"CREATE TABLE " + TBL_ITEMCONTEXT + " (" +
			COL_ID_CONTEXT + " INTEGER PRIMARY KEY AUTOINCREMENT," + 
			COL_ITEMID + " TEXT," +
			COL_WEATHERID + " TEXT," +
			COL_TIMEID + " TEXT" +
//			" FOREIGN KEY (" + COL_WEATHERID + ") REFERENCES " + TBL_WEATHER + " (" + COL_ID_WEATHER + ");" +
//			" FOREIGN KEY (" + COL_ITEMID + ") REFERENCES " + TBL_ITEMS + " (" + COL_ID_ITEM + ");" +
//			" FOREIGN KEY (" + COL_TIMEID + ") REFERENCES " + TBL_TIME + " (" + COL_ID_TIME + ");" +
			")";
	
	String CREATE_TBL_WEATHER =
			"CREATE TABLE " + TBL_WEATHER + " (" +
			COL_ID_WEATHER + " INTEGER PRIMARY KEY AUTOINCREMENT," + 
			COL_WEATHERNAME + " TEXT," +
			COL_TEMPERATURE + " TEXT," +
			COL_WEATHER + " TEXT" +
			")";
	
	String CREATE_TBL_TIME =
			"CREATE TABLE " + TBL_TIME + " (" +
			COL_ID_TIME + " INTEGER PRIMARY KEY AUTOINCREMENT," + 
			COL_TIMENAME + " TEXT," +
			COL_DATE + " TEXT," +
			COL_TIME + " TEXT" +
			")";
			
	
	// Create trigger to achieve referential integrity
	String CREATE_TRIGGER_DEL_ITEMS =
			"CREATE TRIGGER delete_items DELETE ON items " +
			"begin " +
			"delete from photos where items_id = old._id " +
			"end";
	
	String DROP_TBL_ITEMS = "DROP TABLE IF EXISTS " + TBL_ITEMS;
	String DROP_TBL_PHOTOS = "DROP TABLE IF EXISTS " + TBL_PHOTOS;
	String DROP_TBL_CATEGORIES = "DROP TABLE IF EXISTS " + TBL_CATEGORIES;
	String DROP_TBL_ACTIVITIES = "DROP TABLE IF EXISTS " + TBL_ACTIVITIES;
	String DROP_TBL_ACTIVITYITEMS = "DROP TABLE IF EXISTS " + TBL_ACTIVITYITEMS;
//	
//	// TODO
	// Drop-Strings for Location, ItemContext, Weather and Time
	
	String DROP_TRIGGER_DEL_ITEMS = "DROP TRIGGER IF EXISTS delete_items";
	
	String DML_WHERE_ID_CLAUSE = "_id = ?";
	
	String DEFAULT_TBL_ITEMS_SORT_ORDER = "name ASC";
//	
	String LEFT_OUTER_JOIN_STATEMENT = TBL_ITEMS + " LEFT OUTER JOIN " + TBL_PHOTOS + " ON(items._id = photos.items_id";
}
