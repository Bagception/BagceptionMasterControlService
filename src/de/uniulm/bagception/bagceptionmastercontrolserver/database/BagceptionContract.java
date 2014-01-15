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
	
	// Constants for the Items table of the Bagception Provider
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
	
	// Constant for the Photos table of the Bagception Provider
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
	
	// Constants for the Categories table of the Bagception Provider
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
	
	// Constants for the Activities table of the Bagception Provider
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

}
