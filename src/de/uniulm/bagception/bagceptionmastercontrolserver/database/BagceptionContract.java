package de.uniulm.bagception.bagceptionmastercontrolserver.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class BagceptionContract {
	
	// The authority of the Bagception Provider
	public static final String AUTHORITY = "de.uniulm.bagception.bagceptionmastercontrolserver.database.BagceptionProvider";
	
	// The content URI for the top level authority
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
	
	// A selection clause for ID based queries
	public static final String SELECTION_ID_BASED = BaseColumns._ID + " = ? ";
	
	
	/**
	 * Constants for the Items table of the Bagception Provider
	 */
	public static final class Items implements ItemColumns {
		
		// The content URI for this table
		public static final Uri CONTENT_URI = Uri.withAppendedPath(BagceptionContract.CONTENT_URI, "items");
		
		// The MIME Type of a directory of items
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.de.uniulm.bagception_items";
		
		// The MIME Type of a single item
		public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.de.uniulm.bagception_items";
		
		// A projection of all columns in the items table
		public static final String[] PROJECTION_ALL = {_ID, NAME, TAGID, VISIBILITY, ACTIVITY_IND, CATEGORY};
		
		// The default sort order for queries containing NAME fields
		public static final String SORT_ORDER_DEFAULT = " ASC";

	}
	
	
	/**
	 * Constant for the Photos table of the Bagception Provider
	 */
	public static final class Photos implements BaseColumns {
		
		// The content URI for this table
		public static final Uri CONTENT_URI = Uri.withAppendedPath(BagceptionContract.CONTENT_URI, "photos");
			
		// The MIME Type of a directory of photos 
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/bagception_photos";
				
		// The MIME Type of a single photo
		public static final String CONTENT_PHOTO_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/bagception_photos";
				
		// The data column of this photo
		public static final String _DATA = "_data";
				
		// The id of the item this photo belongs to
		public static final String ITEMS_ID = "items_id";
				
		// A projection of all columns in the photos table
		public static final String[] PROJECTION_ALL = {_ID, _DATA, ITEMS_ID};
	}
	
	
	/**
	 * Constants for the Categories table of the Bagception Provider
	 */
	public static final class Categories implements CategoryColumns {
		
		// The content URI for this table
		public static final Uri CONTENT_URI = Uri.withAppendedPath(BagceptionContract.CONTENT_URI, "categories");
		
		// The MIME Type of a directory of categories 
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/bagception_categories";
		
		// The MIME Type of a single category
		public static final String CONTENT_CATEGORY_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/bagception_categories";
		
		// A projection of all columns in the categories table
		public static final String[] PROJECTION_ALL = {_ID, NAME};
		
		// The default sort order for queries containing NAME fields
		public static final String SORT_ORDER_DEFAULT = " ASC";
		
	}
	
	
	/** 
	 * Constants for the Activities table of the Bagception Provider
	 */
	public static final class Activities implements ActivitiyColumns {
		
		// The content URI for this table
		public static final Uri CONTENT_URI = Uri.withAppendedPath(BagceptionContract.CONTENT_URI, "activities");
		
		// The MIME Type of a directory of activities 
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/bagception_activities";
				
		// The MIME Type of a single activity
		public static final String CONTENT_ACTIVITY_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/bagception_activities";
			
		// A projection of all columns in the activities table
		public static final String[] PROJECTION_ALL = {_ID, NAME, LOCATION};
			
		// The default sort order for queries containing NAME fields
		public static final String SORT_ORDER_DEFAULT = " ASC"; 
	}
	
	
	/**
	 * Constants for the ActivityItems table of the Bagception Provider
	 */
	public static final class ActivityItems implements ActivityItemsColumns {
		
		// The content URI for this table
		public static final Uri CONTENT_URI = Uri.withAppendedPath(BagceptionContract.CONTENT_URI, "activityItems");
				
		// The MIME Type of a directory of activityItems
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/bagception_activityItems";
						
		// The MIME Type of a single activityItem
		public static final String CONTENT_ACTIVITYITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/bagception_activityItems";
				
		// A projection of all columns in the activityItems table
		public static final String[] PROJECTION_ALL = {_ID, ACTIVITY, ITEM, CATEGORY};
					
		// The default sort order for queries containing NAME fields
		public static final String SORT_ORDER_DEFAULT = " ASC"; 
	}
	
	
	/**
	 * Constants for the Locations table of the Bagception Provider
	 */
	public static final class Locations implements LocationColumns {
		
		// The content URI for this table
		public static final Uri CONTENT_URI = Uri.withAppendedPath(BagceptionContract.CONTENT_URI, "locations");
				
		// The MIME Type of a directory of locations
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/bagception_locations";
						
		// The MIME Type of a single locations
		public static final String CONTENT_LOCATIONS_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/bagception_locations";
				
		// A projection of all columns in the locations table
		public static final String[] PROJECTION_ALL = {_ID, NAME, LON, LAT, RADIUS, MAC};
					
		// The default sort order for queries containing NAME fields
		public static final String SORT_ORDER_DEFAULT = " ASC"; 
	}
	
	
	/**
	 * Constants for the ItemContext table of the Bagception Provider
	 */
	public static final class ItemContext implements ItemContextColumns {
		
		// The content URI for this table
		public static final Uri CONTENT_URI = Uri.withAppendedPath(BagceptionContract.CONTENT_URI, "itemContext");
				
		// The MIME Type of a directory of ItemContext
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/bagception_itemContext";
						
		// The MIME Type of a single ItemContext
		public static final String CONTENT_ITEMCONTEXT_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/bagception_itemContext";
				
		// A projection of all columns in the ItemContext table
		public static final String[] PROJECTION_ALL = {_ID, ITEMID, WEATHERID, TIMEID};
					
		// The default sort order for queries containing NAME fields
		public static final String SORT_ORDER_DEFAULT = " ASC"; 
	}
	
	
	/**
	 * Constants for the Weather table of the Bagception Provider
	 */
	public static final class Weather implements WeatherColumns {
		
		// The content URI for this table
		public static final Uri CONTENT_URI = Uri.withAppendedPath(BagceptionContract.CONTENT_URI, "weather");
				
		// The MIME Type of a directory of Weather
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/bagception_weather";
						
		// The MIME Type of a single Weather
		public static final String CONTENT_WEATHER_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/bagception_weather";
				
		// A projection of all columns in the Weather table
		public static final String[] PROJECTION_ALL = {_ID, NAME, TEMPERATURE, WEATHER};
					
		// The default sort order for queries containing NAME fields
		public static final String SORT_ORDER_DEFAULT = " ASC"; 
	}
	
	
	/**
	 * Constants for the time table of the Bagception Provider
	 */
	public static final class Time implements TimeColumns {
		
		// The content URI for this table
		public static final Uri CONTENT_URI = Uri.withAppendedPath(BagceptionContract.CONTENT_URI, "time");
				
		// The MIME Type of a directory of time
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/bagception_time";
						
		// The MIME Type of a single time
		public static final String CONTENT_TIME_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/bagception_time";
				
		// A projection of all columns in the time table
		public static final String[] PROJECTION_ALL = {_ID, NAME, DATE, TIME};
					
		// The default sort order for queries containing NAME fields
		public static final String SORT_ORDER_DEFAULT = " ASC"; 
	}
	
	
	/**
	 * Constants for a joined view of Items and Photos. 
	 * The _id of this joined view is the _id of the Items table
	 */
	public static final class ItemEntities implements ItemColumns {
		
		// The content URI for this table
		public static final Uri CONTENT_URI = Uri.withAppendedPath(BagceptionContract.CONTENT_URI, "entities");
		
		// The MIME Type of a directory of joined entities
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/bagception_entities";
		
		// The MIME Type of a single joined entitiy 
		public static final String CONTENT_ENTITY_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/bagception_entities";
		
		// The data column of this joined entitiy
		public static final String _DATA = "_data";
		
		// A projection of all columns in the photos table
		public static final String[] PROJECTION_ALL = {DbSchema.TBL_ITEMS + "." + _ID, NAME, TAGID, VISIBILITY, ACTIVITY_IND, CATEGORY, _DATA};
		
		// The default sort order for queries containing NAME fields
		public static final String SORT_ORDER_DEFAULT = NAME + " ASC";
	}
	
	public static interface ItemColumns extends BaseColumns {
		
		// The constants of the item
		public static final String NAME = "itemname";
		public static final String TAGID = "tag_id";
		public static final String VISIBILITY = "visibility";
		public static final String ACTIVITY_IND = "activityIndependent";
		public static final String CATEGORY = "category";
		
	}
	
	public static interface CategoryColumns extends BaseColumns {
		
		// The constants of the category
		public static final String NAME = "categoryname";
	}
	
	public static interface ActivitiyColumns extends BaseColumns {
		
		// The constants of the category
		public static final String NAME = "activityname";
		public static final String LOCATION = "location";
	}
	
	public static interface ActivityItemsColumns extends BaseColumns {
		
		public static final String ACTIVITY = "activity";
		public static final String ITEM = "item";
		public static final String CATEGORY = "category";
	}

	public static interface LocationColumns extends BaseColumns {
		
		public static final String NAME = "name";
		public static final String LON = "longitude";
		public static final String LAT = "latitude";
		public static final String RADIUS = "radius";
		public static final String MAC = "mac";
	}
	
	public static interface ItemContextColumns extends BaseColumns {
		
		public static final String ITEMID = "itemid";
		public static final String WEATHERID = "weatherid";
		public static final String TIMEID = "timeid";
	}
	
	public static interface WeatherColumns extends BaseColumns {
		
		public static final String NAME = "name";
		public static final String TEMPERATURE = "temperature";
		public static final String WEATHER = "weather";
	}
	
	public static interface TimeColumns extends BaseColumns {
		
		public static final String NAME = "name";
		public static final String DATE = "date";
		public static final String TIME = "time";
	}
}
