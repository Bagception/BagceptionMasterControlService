package de.uniulm.bagception.bagceptionmastercontrolserver.database;

import de.uniulm.bagception.bagceptionmastercontrolserver.database.BagceptionContract.ItemEntities;
import de.uniulm.bagception.bagceptionmastercontrolserver.database.BagceptionContract.Items;
import de.uniulm.bagception.bagceptionmastercontrolserver.database.BagceptionContract.Categories;
import de.uniulm.bagception.bagceptionmastercontrolserver.database.BagceptionContract.*;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
//import android.os.Build;
import android.text.TextUtils;

public class BagceptionProvider extends ContentProvider {
	
	// Helper constants for use with the UriMatcher
	private static final int ITEM_LIST = 1;
	private static final int ITEM_ID = 2;
	private static final int CATEGORY_LIST = 3;
	private static final int CATEGORY_ID = 4;
	private static final int PHOTO_LIST = 5;
	private static final int PHOTO_ID = 6;
	private static final int ACTIVITY_LIST = 7;
	private static final int ACTIVITY_ID = 8;
	private static final int ACTIVITYITEMS_LIST = 9;
	private static final int ACTIVITYITEMS_ID = 10;
	private static final int ENTITY_LIST = 11;
	private static final int ENTITY_ID = 12;
	private static final int LOCATION_LIST = 13;
	private static final int LOCATION_ID = 14;
	private static final int ITEMCONTEXT_LIST = 15;
	private static final int ITEMCONTEXT_ID = 16;
	private static final int WEATHER_LIST = 17;
	private static final int WEATHER_ID = 18;
	private static final int TIME_LIST = 19;
	private static final int TIME_ID = 20;
    private static final UriMatcher URI_MATCHER;
	
	private BagceptionOpenHelper mHelper = null;
    private final ThreadLocal<Boolean> mIsInBatchMode = new ThreadLocal<Boolean>();

    // Prepare the UriMatcher
    static {
		URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
		URI_MATCHER.addURI(BagceptionContract.AUTHORITY, "items", ITEM_LIST);
		URI_MATCHER.addURI(BagceptionContract.AUTHORITY, "items/#", ITEM_ID);
		URI_MATCHER.addURI(BagceptionContract.AUTHORITY, "categories", CATEGORY_LIST);
		URI_MATCHER.addURI(BagceptionContract.AUTHORITY, "categories/#", CATEGORY_ID);
		URI_MATCHER.addURI(BagceptionContract.AUTHORITY, "photos", PHOTO_LIST);
		URI_MATCHER.addURI(BagceptionContract.AUTHORITY, "photos/#", PHOTO_ID);
		URI_MATCHER.addURI(BagceptionContract.AUTHORITY, "activities", ACTIVITY_LIST);
		URI_MATCHER.addURI(BagceptionContract.AUTHORITY, "activities/#", ACTIVITY_ID);
		URI_MATCHER.addURI(BagceptionContract.AUTHORITY, "activityItems", ACTIVITYITEMS_LIST);
		URI_MATCHER.addURI(BagceptionContract.AUTHORITY, "activityItems/#", ACTIVITYITEMS_ID);
		URI_MATCHER.addURI(BagceptionContract.AUTHORITY, "locations", LOCATION_LIST);
		URI_MATCHER.addURI(BagceptionContract.AUTHORITY, "locations/#", LOCATION_ID);
		URI_MATCHER.addURI(BagceptionContract.AUTHORITY, "itemContext", ITEMCONTEXT_LIST);
		URI_MATCHER.addURI(BagceptionContract.AUTHORITY, "itemContext/#", ITEMCONTEXT_ID);
		URI_MATCHER.addURI(BagceptionContract.AUTHORITY, "weather", WEATHER_LIST);
		URI_MATCHER.addURI(BagceptionContract.AUTHORITY, "weather/#", WEATHER_ID);
		URI_MATCHER.addURI(BagceptionContract.AUTHORITY, "time", TIME_LIST);
		URI_MATCHER.addURI(BagceptionContract.AUTHORITY, "time/#", TIME_ID);
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
		String where = null;
		String idStr = null;
		
		switch (URI_MATCHER.match(uri)) {
		case ITEM_LIST:
			delCount = db.delete(DbSchema.TBL_ITEMS, selection, selectionArgs);
			break;
		case ITEM_ID:
			idStr = uri.getLastPathSegment();
			where = Items._ID + " = " + idStr;
			if (!TextUtils.isEmpty(selection)) {
				where += " AND " + selection;
			}
			delCount = db.delete(DbSchema.TBL_ITEMS, where, selectionArgs);
			break;
			
		case CATEGORY_LIST:
			delCount = db.delete(DbSchema.TBL_CATEGORIES, selection, selectionArgs);
			break;
		case CATEGORY_ID:
			idStr = uri.getLastPathSegment();
			where = Categories._ID + " = " + idStr;
			if(!TextUtils.isEmpty(selection)) {
				where += " AND " + selection;
			}
			delCount = db.delete(DbSchema.TBL_CATEGORIES, where, selectionArgs);
			break;
			
		case ACTIVITY_LIST:
			delCount = db.delete(DbSchema.TBL_ACTIVITIES, selection, selectionArgs);
			break;
		case ACTIVITY_ID:
			idStr = uri.getLastPathSegment();
			where = Activities._ID + " = " + idStr;
			if(!TextUtils.isEmpty(selection)) {
				where += " AND " + selection;
			}
			delCount = db.delete(DbSchema.TBL_ACTIVITIES, where, selectionArgs);
			break;
			
		case ACTIVITYITEMS_LIST:
			delCount = db.delete(DbSchema.TBL_ACTIVITYITEMS, selection, selectionArgs);
			break;
		case ACTIVITYITEMS_ID:
			idStr = uri.getLastPathSegment();
			where = ActivityItems._ID + " = " + idStr;
			if(!TextUtils.isEmpty(selection)) {
				where += " AND " + selection;
			}
			delCount = db.delete(DbSchema.TBL_ACTIVITYITEMS, where, selectionArgs);
			break;
			
		case LOCATION_LIST:
			delCount = db.delete(DbSchema.TBL_LOCATIONS, selection, selectionArgs);
			break;
		case LOCATION_ID:
			idStr = uri.getLastPathSegment();
			where = Locations._ID + " = " + idStr;
			if(!TextUtils.isEmpty(selection)) {
				where += " AND " + selection;
			}
			delCount = db.delete(DbSchema.TBL_LOCATIONS, where, selectionArgs);
			break;
			
		case ITEMCONTEXT_LIST:
			delCount = db.delete(DbSchema.TBL_ITEMCONTEXT, selection, selectionArgs);
			break;
		case ITEMCONTEXT_ID:
			idStr = uri.getLastPathSegment();
			where = ItemContext._ID + " = " + idStr;
			if(!TextUtils.isEmpty(selection)) {
				where += " AND " + selection;
			}
			delCount = db.delete(DbSchema.TBL_ITEMCONTEXT, where, selectionArgs);
			break;
			
		case WEATHER_LIST:
			delCount = db.delete(DbSchema.TBL_WEATHER, selection, selectionArgs);
			break;
		case WEATHER_ID:
			idStr = uri.getLastPathSegment();
			where = Weather._ID + " = " + idStr;
			if(!TextUtils.isEmpty(selection)) {
				where += " AND " + selection;
			}
			delCount = db.delete(DbSchema.TBL_WEATHER, where, selectionArgs);
			break;
			
		case TIME_LIST:
			delCount = db.delete(DbSchema.TBL_TIME, selection, selectionArgs);
			break;
		case TIME_ID:
			idStr = uri.getLastPathSegment();
			where = Time._ID + " = " + idStr;
			if(!TextUtils.isEmpty(selection)) {
				where += " AND " + selection;
			}
			delCount = db.delete(DbSchema.TBL_TIME, where, selectionArgs);
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
		case ITEM_ID:
			return Items.CONTENT_ITEM_TYPE;
		case ITEM_LIST:
			return Items.CONTENT_TYPE;
			
		case CATEGORY_ID:
			return Categories.CONTENT_CATEGORY_TYPE;
		case CATEGORY_LIST:
			return Categories.CONTENT_TYPE;
			
		case PHOTO_ID:
			return Photos.CONTENT_PHOTO_TYPE;
		case PHOTO_LIST:
			return Photos.CONTENT_TYPE;
			
		case ACTIVITY_ID:
			return Activities.CONTENT_ACTIVITY_TYPE;
		case ACTIVITY_LIST:
			return Activities.CONTENT_TYPE;
			
		case ACTIVITYITEMS_ID:
			return ActivityItems.CONTENT_ACTIVITYITEM_TYPE;
		case ACTIVITYITEMS_LIST:
			return ActivityItems.CONTENT_TYPE;
			
		case LOCATION_ID:
			return Locations.CONTENT_LOCATIONS_TYPE;
		case LOCATION_LIST:
			return Locations.CONTENT_TYPE;
			
		case ITEMCONTEXT_ID:
			return ItemContext.CONTENT_ITEMCONTEXT_TYPE;
		case ITEMCONTEXT_LIST:
			return ItemContext.CONTENT_TYPE;
			
		case WEATHER_ID:
			return Weather.CONTENT_WEATHER_TYPE;
		case WEATHER_LIST:
			return Weather.CONTENT_TYPE;
			
		case TIME_ID:
			return Time.CONTENT_TIME_TYPE;
		case TIME_LIST:
			return Time.CONTENT_TYPE;
			
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

		if (URI_MATCHER.match(uri) != ITEM_LIST && URI_MATCHER.match(uri) != CATEGORY_LIST && URI_MATCHER.match(uri) != PHOTO_LIST 
				&& URI_MATCHER.match(uri) != ACTIVITY_LIST) {
			throw new IllegalArgumentException("Unsupported URI for insertion: " + uri);
		}
		
		SQLiteDatabase db = mHelper.getWritableDatabase();
		
		if (URI_MATCHER.match(uri) == ITEM_LIST) {
			long id = db.insert(DbSchema.TBL_ITEMS, null, values);
			return getUriForId(id, uri);
		} 

		else if (URI_MATCHER.match(uri) == CATEGORY_LIST) {
			long id = db.insert(DbSchema.TBL_CATEGORIES, null, values);
			return getUriForId(id, uri);
		}
		
		else if (URI_MATCHER.match(uri) == ACTIVITY_LIST) {
			long id = db.insert(DbSchema.TBL_ACTIVITIES, null, values);
			return getUriForId(id, uri);
		}
		
		else if (URI_MATCHER.match(uri) == ACTIVITYITEMS_LIST) {
			long id = db.insert(DbSchema.TBL_ACTIVITYITEMS, null, values);
			return getUriForId(id, uri);
		}
		
		else if (URI_MATCHER.match(uri) == LOCATION_LIST) {
			long id = db.insert(DbSchema.TBL_LOCATIONS, null, values);
			return getUriForId(id, uri);
		}
		
		else if (URI_MATCHER.match(uri) == ITEMCONTEXT_LIST) {
			long id = db.insert(DbSchema.TBL_ITEMCONTEXT, null, values);
			return getUriForId(id, uri);
		}
		
		else if (URI_MATCHER.match(uri) == WEATHER_LIST) {
			long id = db.insert(DbSchema.TBL_WEATHER, null, values);
			return getUriForId(id, uri);
		}
		
		else if (URI_MATCHER.match(uri) == TIME_LIST) {
			long id = db.insert(DbSchema.TBL_TIME, null, values);
			return getUriForId(id, uri);
		}
		
		else {
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
			
		case CATEGORY_LIST:
			builder.setTables(DbSchema.TBL_CATEGORIES);
			if (TextUtils.isEmpty(sortOrder)) {
		         sortOrder = Categories.SORT_ORDER_DEFAULT;
		    }
			break;
		case CATEGORY_ID:
			builder.setTables(DbSchema.TBL_CATEGORIES);
			builder.appendWhere(Categories._ID + " = " + uri.getLastPathSegment());
			break;
			
		case ACTIVITY_LIST:
			builder.setTables(DbSchema.TBL_ACTIVITIES);
			if (TextUtils.isEmpty(sortOrder)) {
				sortOrder = Activities.SORT_ORDER_DEFAULT;
			}
			break;
		case ACTIVITY_ID:
			builder.setTables(DbSchema.TBL_ACTIVITIES);
			builder.appendWhere(Activities._ID + " = " + uri.getLastPathSegment());
			break;
			
		case ACTIVITYITEMS_LIST:
			builder.setTables(DbSchema.TBL_ACTIVITYITEMS);
			if (TextUtils.isEmpty(sortOrder)) {
				sortOrder = ActivityItems.SORT_ORDER_DEFAULT;
			}
			break;
		case ACTIVITYITEMS_ID:
			builder.setTables(DbSchema.TBL_ACTIVITYITEMS);
			builder.appendWhere(ActivityItems._ID + " = " + uri.getLastPathSegment());
			break;
			
		case LOCATION_LIST:
			builder.setTables(DbSchema.TBL_LOCATIONS);
			if (TextUtils.isEmpty(sortOrder)) {
				sortOrder = Locations.SORT_ORDER_DEFAULT;
			}
			break;
		case LOCATION_ID:
			builder.setTables(DbSchema.TBL_LOCATIONS);
			builder.appendWhere(Locations._ID + " = " + uri.getLastPathSegment());
			break;
			
		case ITEMCONTEXT_LIST:
			builder.setTables(DbSchema.TBL_ITEMCONTEXT);
			if (TextUtils.isEmpty(sortOrder)) {
				sortOrder = ItemContext.SORT_ORDER_DEFAULT;
			}
			break;
		case ITEMCONTEXT_ID:
			builder.setTables(DbSchema.TBL_ITEMCONTEXT);
			builder.appendWhere(ItemContext._ID + " = " + uri.getLastPathSegment());
			break;
			
		case WEATHER_LIST:
			builder.setTables(DbSchema.TBL_WEATHER);
			if (TextUtils.isEmpty(sortOrder)) {
				sortOrder = Weather.SORT_ORDER_DEFAULT;
			}
			break;
		case WEATHER_ID:
			builder.setTables(DbSchema.TBL_WEATHER);
			builder.appendWhere(Weather._ID + " = " + uri.getLastPathSegment());
			break;
			
		case TIME_LIST:
			builder.setTables(DbSchema.TBL_TIME);
			if (TextUtils.isEmpty(sortOrder)) {
				sortOrder = Time.SORT_ORDER_DEFAULT;
			}
			break;
		case TIME_ID:
			builder.setTables(DbSchema.TBL_TIME);
			builder.appendWhere(Time._ID + " = " + uri.getLastPathSegment());
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
		String idStr = null;
		String where = null;
		
		switch (URI_MATCHER.match(uri)) {
		case ITEM_LIST:
			updateCount = db.update(DbSchema.TBL_ITEMS, values, selection, selectionArgs);
			break;
		case ITEM_ID:
			idStr = uri.getLastPathSegment();
			where = Items._ID + " = " + idStr;
			if (!TextUtils.isEmpty(selection)) {
				where += " AND " + selection;
			}
			updateCount = db.update(DbSchema.TBL_ITEMS, values, where, selectionArgs);
			break;
			
		case CATEGORY_LIST:
			updateCount = db.update(DbSchema.TBL_CATEGORIES, values, selection, selectionArgs);
			break;
		case CATEGORY_ID:
			idStr = uri.getLastPathSegment();
			where = Categories._ID + " = " + idStr;
			if (!TextUtils.isEmpty(selection)) {
				where += " AND " + selection;
			}
			updateCount = db.update(DbSchema.TBL_CATEGORIES, values, where, selectionArgs);
			break;
			
		case ACTIVITY_LIST:
			updateCount = db.update(DbSchema.TBL_ACTIVITIES, values, selection, selectionArgs);
			break;
		case ACTIVITY_ID:
			idStr = uri.getLastPathSegment();
			where = Activities._ID + " = " + idStr;
			if (!TextUtils.isEmpty(selection)) {
				where += " AND " + selection;
			}
			updateCount = db.update(DbSchema.TBL_ACTIVITIES, values, where, selectionArgs);
			break;
			
		case ACTIVITYITEMS_LIST:
			updateCount = db.update(DbSchema.TBL_ACTIVITYITEMS, values, selection, selectionArgs);
			break;
		case ACTIVITYITEMS_ID:
			idStr = uri.getLastPathSegment();
			where = ActivityItems._ID + " = " + idStr;
			if (!TextUtils.isEmpty(selection)) {
				where += " AND " + selection;
			}
			updateCount = db.update(DbSchema.TBL_ACTIVITYITEMS, values, where, selectionArgs);
			break;
			
		case LOCATION_LIST:
			updateCount = db.update(DbSchema.TBL_LOCATIONS, values, selection, selectionArgs);
			break;
		case LOCATION_ID:
			idStr = uri.getLastPathSegment();
			where = Locations._ID + " = " + idStr;
			if (!TextUtils.isEmpty(selection)) {
				where += " AND " + selection;
			}
			updateCount = db.update(DbSchema.TBL_LOCATIONS, values, where, selectionArgs);
			break;
			
		case ITEMCONTEXT_LIST:
			updateCount = db.update(DbSchema.TBL_ITEMCONTEXT, values, selection, selectionArgs);
			break;
		case ITEMCONTEXT_ID:
			idStr = uri.getLastPathSegment();
			where = ItemContext._ID + " = " + idStr;
			if (!TextUtils.isEmpty(selection)) {
				where += " AND " + selection;
			}
			updateCount = db.update(DbSchema.TBL_ITEMCONTEXT, values, where, selectionArgs);
			break;
			
		case WEATHER_LIST:
			updateCount = db.update(DbSchema.TBL_WEATHER, values, selection, selectionArgs);
			break;
		case WEATHER_ID:
			idStr = uri.getLastPathSegment();
			where = Weather._ID + " = " + idStr;
			if (!TextUtils.isEmpty(selection)) {
				where += " AND " + selection;
			}
			updateCount = db.update(DbSchema.TBL_WEATHER, values, where, selectionArgs);
			break;
			
		case TIME_LIST:
			updateCount = db.update(DbSchema.TBL_TIME, values, selection, selectionArgs);
			break;
		case TIME_ID:
			idStr = uri.getLastPathSegment();
			where = Time._ID + " = " + idStr;
			if (!TextUtils.isEmpty(selection)) {
				where += " AND " + selection;
			}
			updateCount = db.update(DbSchema.TBL_TIME, values, where, selectionArgs);
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




















