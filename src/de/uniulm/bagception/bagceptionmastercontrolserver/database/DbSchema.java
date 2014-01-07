package de.uniulm.bagception.bagceptionmastercontrolserver.database;

import android.provider.BaseColumns;

public interface DbSchema {
	
	// Database name
	String DB_NAME = "bagception.db";
	
	// Database tables
	String TBL_ITEMS = "items";
	String TBL_PHOTOS = "photos";
	
	// Columns TBL_ITEMS
	String COL_ID = BaseColumns._ID;
	String COL_ITEMNAME = "itemname";
	String COL_TAGID = "tag_id";
	String COL_VISIBILITY = "visibility";
	String COL_ACTIVITY = "activity";

	// Columns TBL_PHOTOS
	String COL_DATA = "_data";
	String COL_ITEMS = "items_id";
	
	String CREATE_TBL_ITEMS =
			"CREATE TABLE items (" +
			"_id			INTEGER PRIMARY KEY AUTOINCREMENT, \n" + 
			"itemname		TEXT,\n" +
			"tag_id			TEXT,\n" +
			"visibility		TEXT,\n" +
			"activity		TEXT,\n" +
			")";
	
	String CREATE_TBL_PHOTOS =
			"CREATE TABLE photos (" +
			"_id			INTEGER PRIMARY KEY AUTOINCREMENT, \n" + 
			"_data			TEXT,\n" +
			"items_id		INTEGER NOT NULL,\n" +
			")";
	
	// Create trigger to achieve referential integrity
	String CREATE_TRIGGER_DEL_ITEMS =
			"CREATE TRIGGER delete_items DELETE ON items \n" +
			"begin\n" +
			"	delete from photos where items_id = old._id\n" +
			"end\n";
	
	String DROP_TBL_ITEMS = "DROP TABLE IF EXISTS items";
	String DROP_TBL_PHOTOS = "DROP TABLE IF EXISTS photos";
	String DROP_TRIGGER_DEL_ITEMS = "DROP TRIGGER IF EXISTS delete_items";
	
	String DML_WHERE_ID_CLAUSE = "_id = ?";
	
	String DEFAULT_TBL_ITEMS_SORT_ORDER = "name ASC";
	
	String LEFT_OUTER_JOIN_STATEMENT = TBL_ITEMS + " LEFT OUTER JOIN " + TBL_PHOTOS + " ON(items._id = photos.items_id";
}
