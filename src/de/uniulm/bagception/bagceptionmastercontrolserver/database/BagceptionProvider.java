package de.uniulm.bagception.bagceptionmastercontrolserver.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class BagceptionProvider extends ContentProvider {
	
	private BagceptionDBHandler bagceptionDB;
	
	// Constructing the Authority and Content URI
	static final String AUTHORITY = "de.uniulm.bagception.provider.BagceptionProvider";
	static final String ITEMS_TABLE = "items";
	static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + ITEMS_TABLE);
	
	
	// Implementing URI Matching in the Content Provider
	public static final int ITEMS = 1;
	public static final int ITEMS_ID = 2;
	
	private static final UriMatcher bURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	
	static {
		bURIMatcher.addURI(AUTHORITY, ITEMS_TABLE, ITEMS);
		bURIMatcher.addURI(AUTHORITY, ITEMS_TABLE + "/#", ITEMS_ID);
	}
	
	@Override
	public boolean onCreate() {

		bagceptionDB = new BagceptionDBHandler(getContext(), null, null, 1);
		return false;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {

		int uriType = bURIMatcher.match(uri);
		
		SQLiteDatabase sqlDB = bagceptionDB.getWritableDatabase();
		
		long id = 0;
		switch(uriType) {
			case ITEMS:
				id = sqlDB.insert(BagceptionDBHandler.TABLE_ITEMS, null, values);
			
			break;
			
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(ITEMS_TABLE + "/" + id);
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,	String[] selectionArgs, String sortOrder) {
		
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(BagceptionDBHandler.TABLE_ITEMS);
		
		int uriType = bURIMatcher.match(uri);
		
		switch (uriType) {
			case ITEMS_ID:
				queryBuilder.appendWhere(BagceptionDBHandler.COLUMN_ID + "=" + uri.getLastPathSegment());
				break;
			
			case ITEMS:
				break;
			
			default:
				throw new IllegalArgumentException("Unknown URI");
		}
		
		Cursor cursor = queryBuilder.query(bagceptionDB.getReadableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

		int uriType = bURIMatcher.match(uri);
		SQLiteDatabase sqlDB = bagceptionDB.getWritableDatabase();
		int rowsUpdated = 0;
		
		switch (uriType) {
			case ITEMS:
				rowsUpdated = sqlDB.update(BagceptionDBHandler.TABLE_ITEMS, values, selection, selectionArgs);
				break;
			
			case ITEMS_ID:
				String id = uri.getLastPathSegment();
				if(TextUtils.isEmpty(selection)) {
					rowsUpdated = sqlDB.update(BagceptionDBHandler.TABLE_ITEMS, values, BagceptionDBHandler.COLUMN_ID + "=" + id, null);
				} else {
					rowsUpdated = sqlDB.update(BagceptionDBHandler.TABLE_ITEMS, values, BagceptionDBHandler.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
				}
				break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		
		getContext().getContentResolver().notifyChange(uri, null);
		
		return rowsUpdated;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {

		int uriType = bURIMatcher.match(uri);
		SQLiteDatabase sqlDB = bagceptionDB.getWritableDatabase();
		int rowsDeleted = 0;
		
		switch (uriType) {
			case ITEMS:
				rowsDeleted = sqlDB.delete(BagceptionDBHandler.TABLE_ITEMS, selection, selectionArgs);
				break;
			
			case ITEMS_ID:
				String id = uri.getLastPathSegment();
				if(TextUtils.isEmpty(selection)) {
					rowsDeleted = sqlDB.delete(BagceptionDBHandler.TABLE_ITEMS, BagceptionDBHandler.COLUMN_ID + "=" + id, null);
				} else {
					rowsDeleted = sqlDB.delete(BagceptionDBHandler.TABLE_ITEMS, BagceptionDBHandler.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
				}
				break;
				
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

}




















