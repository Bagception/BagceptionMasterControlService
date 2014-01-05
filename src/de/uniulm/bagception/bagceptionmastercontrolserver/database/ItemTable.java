package de.uniulm.bagception.bagceptionmastercontrolserver.database;

import android.database.sqlite.SQLiteDatabase;

public class ItemTable {

	// SQL convention says Table name should be singular, so "Item", not "Items"
		public static final String TABLE_NAME = "Item";
		
		// Naming the id column with an underscore is good to be consistent with other Android things.
		public static final String COL_ID = "_id";
		
		// Columns of the Table
		public static final String COL_NAME = "name";
		public static final String COL_TAGID = "tagid";
		public static final String COL_VIS = "visibility";
		public static final String COL_CONTEXT = "context";
		public static final String COL_ACTIVITY = "activity";
		
		// Database projection, so order is consistent
		public static final String[] FIELDS = {COL_ID, COL_CONTEXT,COL_NAME, COL_TAGID, COL_VIS, COL_ACTIVITY};
		
		// SQL code that creates a Table for storing Items in
		public static final String CREATE_TABLE = 
				"CREATE TABLE " + TABLE_NAME + "(" 
				+ COL_ID + " INTEGER PRIMARY KEY,"
				+ COL_CONTEXT + " TEXT NOT NULL DEFAULT '',"
				+ COL_NAME + " TEXT NOT NULL DEFAULT '',"
				+ COL_TAGID + " TEXT NULL DEFAULT '',"
				+ COL_VIS + " TEXT NULL DEFAULT '',"
				+ COL_ACTIVITY + " TEXT NULL DEFAULT ''"
				+ ")";
		
		public static void onCreate(SQLiteDatabase database) {
		    database.execSQL(CREATE_TABLE);
		}

		
		public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		    
		    database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		    onCreate(database);
		}
}
