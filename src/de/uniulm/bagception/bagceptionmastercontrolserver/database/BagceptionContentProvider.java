package de.uniulm.bagception.bagceptionmastercontrolserver.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class BagceptionContentProvider extends ContentProvider {
	
	// All URIs share these parts
	public static final String AUTHORITY = "de.uniulm.bagceptiondatabase.database.provider";
	public static final String SCHEME = "content://";
		
	// URIs
	// Used for all items
	public static final String ITEMS = SCHEME + AUTHORITY + "/item";  
	public static final Uri URI_ITEMS = Uri.parse(ITEMS);
		
	// Used for a single item, just add the id to the end
	public static final String ITEM_BASE = ITEMS + "/";

	// Helper constants for use with the UriMatcher
	private static final int ITEM_LIST = 1;
	private static final int ITEM_ID = 2;
	
	private static final UriMatcher URI_MATCHER;
	
	static{
		URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
		URI_MATCHER.addURI(AUTHORITY, "items", ITEM_LIST);
		URI_MATCHER.addURI(AUTHORITY, "items/#", ITEM_ID);
	}
	
	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
