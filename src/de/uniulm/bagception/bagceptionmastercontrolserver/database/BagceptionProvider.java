package de.uniulm.bagception.bagceptionmastercontrolserver.database;

import de.uniulm.bagception.bagceptionmastercontrolserver.database.BagceptionContract.ItemEntities;
import de.uniulm.bagception.bagceptionmastercontrolserver.database.BagceptionContract.Items;
import de.uniulm.bagception.bagceptionmastercontrolserver.database.BagceptionContract.Photos;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

public class BagceptionProvider extends ContentProvider {
	
	// Helper constants for use with the UriMatcher
	private static final int ITEM_LIST = 1;
	private static final int ITEM_ID = 2;
	private static final int PHOTO_LIST = 5;
	private static final int PHOTO_ID = 6;
	private static final int ENTITY_LIST = 10;
	private static final int ENTITY_ID = 11;
    private static final UriMatcher URI_MATCHER;
	
	private BagceptionOpenHelper mHelper = null;
    private final ThreadLocal<Boolean> mIsInBatchMode = new ThreadLocal<Boolean>();

    // Prepare the UriMatcher
    static {
		URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
		URI_MATCHER.addURI(BagceptionContract.AUTHORITY, "items", ITEM_LIST);
		URI_MATCHER.addURI(BagceptionContract.AUTHORITY, "items/#", ITEM_ID);
		URI_MATCHER.addURI(BagceptionContract.AUTHORITY, "photos", PHOTO_LIST);
		URI_MATCHER.addURI(BagceptionContract.AUTHORITY, "photos/#", PHOTO_ID);
		URI_MATCHER.addURI(BagceptionContract.AUTHORITY, "entities", ENTITY_LIST);
		URI_MATCHER.addURI(BagceptionContract.AUTHORITY, "entities/#", ENTITY_ID);
	}
    
    @Override
	public boolean onCreate() {
    	mHelper = new BagceptionOpenHelper(getContext());
    	return true;
	}
    
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {

		SQLiteDatabase db = mHelper.getWritableDatabase();
		int delCount = 0;
		switch (URI_MATCHER.match(uri)) {
		case ITEM_LIST:
			delCount = db.delete(DbSchema.TBL_ITEMS, selection, selectionArgs);
			break;
		case ITEM_ID:
			String idStr = uri.getLastPathSegment();
			String where = Items._ID + " = " + idStr;
			if (!TextUtils.isEmpty(selection)) {
				where += " AND " + selection;
			}
			delCount = db.delete(DbSchema.TBL_ITEMS, where, selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		// Notify all listeners of changes:
		if (delCount > 0 && !isInBatchMode()) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return delCount;
	}

	@Override
	public String getType(Uri uri) {
		switch (URI_MATCHER.match(uri)) {
		case ITEM_LIST:
			return Items.CONTENT_TYPE;
		case ITEM_ID:
			return Items.CONTENT_ITEM_TYPE;
		case PHOTO_ID:
			return Photos.CONTENT_PHOTO_TYPE;
		case PHOTO_LIST:
			return Photos.CONTENT_TYPE;
		case ENTITY_ID:
			return ItemEntities.CONTENT_ENTITY_TYPE;
		case ENTITY_LIST:
			return ItemEntities.CONTENT_TYPE;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {

		if (URI_MATCHER.match(uri) != ITEM_LIST && URI_MATCHER.match(uri) != PHOTO_LIST) {
			throw new IllegalArgumentException("Unsupported URI for insertion: " + uri);
		}
		
		SQLiteDatabase db = mHelper.getWritableDatabase();
		
		if (URI_MATCHER.match(uri) == ITEM_LIST) {
			long id = db.insert(DbSchema.TBL_ITEMS, null, values);
			return getUriForId(id, uri);
		} else {
			long id = db.insertWithOnConflict(DbSchema.TBL_PHOTOS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
			return getUriForId(id, uri);
		}
	}
	
	private Uri getUriForId(long id, Uri uri) {
	      if (id > 0) {
	         Uri itemUri = ContentUris.withAppendedId(uri, id);
	        
	         if (!isInBatchMode()) {
	        	 
	            // Notify all listeners of changes and return itemUri:
	            getContext().getContentResolver().notifyChange(itemUri, null);
	         }
	         return itemUri;
	      }
	      
	      throw new SQLException("Problem while inserting into uri: " + uri);
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

		SQLiteDatabase db = mHelper.getReadableDatabase();
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
	   boolean useAuthorityUri = false;
		switch (URI_MATCHER.match(uri)) {
		case ITEM_LIST:
	      builder.setTables(DbSchema.TBL_ITEMS);
	      if (TextUtils.isEmpty(sortOrder)) {
	         sortOrder = Items.SORT_ORDER_DEFAULT;
	      }
			break;
		case ITEM_ID:
	      builder.setTables(DbSchema.TBL_ITEMS);
			builder.appendWhere(Items._ID + " = " + uri.getLastPathSegment());
			break;
		case PHOTO_LIST:
			builder.setTables(DbSchema.TBL_PHOTOS);
			break;
		case PHOTO_ID:
			builder.setTables(DbSchema.TBL_PHOTOS);
			builder.appendWhere(Photos._ID + " = " + uri.getLastPathSegment());
			break;
      case ENTITY_LIST:
         builder.setTables(DbSchema.LEFT_OUTER_JOIN_STATEMENT);
         if (TextUtils.isEmpty(sortOrder)) {
            sortOrder = ItemEntities.SORT_ORDER_DEFAULT;
         }
         useAuthorityUri = true;
         break;
      case ENTITY_ID:
         builder.setTables(DbSchema.LEFT_OUTER_JOIN_STATEMENT);
         builder.appendWhere(DbSchema.TBL_ITEMS + "." + Items._ID + " = " + uri.getLastPathSegment());
         useAuthorityUri = true;
         break;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		
		Cursor cursor = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		
		// If we want to be notified of any changes:
		if (useAuthorityUri) {
			cursor.setNotificationUri(getContext().getContentResolver(), BagceptionContract.CONTENT_URI);
		} else {
			cursor.setNotificationUri(getContext().getContentResolver(), uri);
		  }
		   
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

		SQLiteDatabase db = mHelper.getWritableDatabase();
		int updateCount = 0;
		switch (URI_MATCHER.match(uri)) {
		case ITEM_LIST:
			updateCount = db.update(DbSchema.TBL_ITEMS, values, selection, selectionArgs);
			break;
		case ITEM_ID:
			String idStr = uri.getLastPathSegment();
			String where = Items._ID + " = " + idStr;
			if (!TextUtils.isEmpty(selection)) {
				where += " AND " + selection;
			}
			updateCount = db.update(DbSchema.TBL_ITEMS, values, where, selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		
		// Notify all listeners of changes:
		if (updateCount > 0 && !isInBatchMode()) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return updateCount;
	}
	
	private boolean isInBatchMode() {
		return mIsInBatchMode.get() != null && mIsInBatchMode.get();
	}
	
}




















