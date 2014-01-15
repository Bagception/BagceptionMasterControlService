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
	
	// Columns TBL_ITEMS
	String COL_ID = BaseColumns._ID;
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
	
	// Strings to create tables	
	String CREATE_TBL_ITEMS =
			"CREATE TABLE " + TBL_ITEMS + "(" +
			COL_ID +			"INTEGER PRIMARY KEY AUTOINCREMENT, \n" + 
			COL_ITEMNAME + 		"TEXT,\n" +
			COL_TAGID + 		"TEXT,\n" +
			COL_VISIBILITY +	"TEXT,\n" +
			COL_ACTIVITY_IND + 	"TEXT,\n" +
			")";
	
	String CREATE_TBL_PHOTOS =
			"CREATE TABLE " + TBL_PHOTOS + "(" +
			COL_ID_PHOTO + 	"INTEGER PRIMARY KEY AUTOINCREMENT, \n" + 
			COL_DATA + 		"TEXT,\n" +
			COL_ITEMS +		"INTEGER NOT NULL,\n" +
			")";
	
	String CREATE_TBL_CATEGORIES =
			"CREATE TABLE " + TBL_CATEGORIES + "(" +
			COL_ID_CATEGORY + "INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
			COL_CATEGORYNAME + "TEXT,\n" +
			")";
	
	String CREATE_TBL_ACTIVITIES = 
			"CREATE TABLE " + TBL_ACTIVITIES + "(" +
			COL_ID_ACTIVITY + "INTEGER PRIMARY KEY AUTOINCREMENT, \n" + 
			COL_ACTIVITYNAME + "TEXT,\n" +
			COL_ACTIVITYLOCATION + "TEXT,\n" +
			")";
			
	
	// Create trigger to achieve referential integrity
	String CREATE_TRIGGER_DEL_ITEMS =
			"CREATE TRIGGER delete_items DELETE ON items \n" +
			"begin\n" +
			"	delete from photos where items_id = old._id\n" +
			"end\n";
	
	String DROP_TBL_ITEMS = "DROP TABLE IF EXISTS " + TBL_ITEMS;
	String DROP_TBL_PHOTOS = "DROP TABLE IF EXISTS " + TBL_PHOTOS;
	String DROP_TBL_CATEGORIES = "DROP TABLE IF EXISTS " + TBL_CATEGORIES;
	String DROP_TBL_ACTIVITIES = "DROP TABLE IF EXISTS " + TBL_ACTIVITIES;
	
	String DROP_TRIGGER_DEL_ITEMS = "DROP TRIGGER IF EXISTS delete_items";
	
	String DML_WHERE_ID_CLAUSE = "_id = ?";
	
	String DEFAULT_TBL_ITEMS_SORT_ORDER = "name ASC";
	
	String LEFT_OUTER_JOIN_STATEMENT = TBL_ITEMS + " LEFT OUTER JOIN " + TBL_PHOTOS + " ON(items._id = photos.items_id";
}
