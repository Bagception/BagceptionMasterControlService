package de.uniulm.bagception.bagceptionmastercontrolserver.database;

import android.content.ContentResolver;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BagceptionDBHandler extends SQLiteOpenHelper{

	private ContentResolver bagceptionCR;
	
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "bagceptionDB.db";
	public static final String TABLE_ITEMS = "items";
	
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_ITEMNAME = "itemname";
	public static final String COLUMN_TAGID = "tag_id";
	public static final String COLUMN_VISIBILITY = "visibility";
	public static final String COLUMN_PICTURE = "picture";
	
	public BagceptionDBHandler(Context context, String name, CursorFactory factory, int version) {
		super(context, DATABASE_NAME, factory, DATABASE_VERSION);
		bagceptionCR = context.getContentResolver();
	}

	public void addItem(Item item) {
		
	}
}
