package de.uniulm.bagception.bagceptionmastercontrolserver.database;

import java.util.List;

import de.uniulm.bagception.bundlemessageprotocol.entities.Activity;
import de.uniulm.bagception.bundlemessageprotocol.entities.Category;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.bundlemessageprotocol.entities.ItemAttribute;
import de.uniulm.bagception.bundlemessageprotocol.entities.Location;

public interface DatabaseInterface {

	////////////////////////////////
	//	Entity CRUD operations    //
	////////////////////////////////
	
		/*
		 * Items
		 */
	
		/**
		 * creates a new Item in the database
		 * @param item the item to create
		 * @throws DatabaseException
		 */
		public void addItem(Item item) throws DatabaseException;
		
		/**
		 * deletes an Item from the database
		 * @param item the item to delete
		 * @throws DatabaseException
		 */
		public void deleteItem(Item item) throws DatabaseException;
		
		
		/**
		 * Deprecated! Use "updateItem(Item item)" instead!
		 * @param itemBefore the item to change
		 * @param after the new item values
		 * @param int the new id
		 * @throws DatabaseException
		 */
		public void editItem(Item toEdit, Item values) throws DatabaseException;
		
		
		/**
		 * Update item. Its important to set the old _id value from the item
		 * @param the new item
		 * @throws DatabaseException
		 */
		public void updateItem(Item item) throws DatabaseException;
		
		
		/**
		 * get a specific item
		 * @param id of the item
		 * @return the requested Item, null if no item is present with this id
		 * @throws DatabaseException
		 */
		public Item getItem(long id) throws DatabaseException;
		
		/**
		 * Deprecated! Get an item through his name
		 * @param name
		 * @return the requested Item, null if no item is present with this name
		 * @throws DatabaseException
		 */
		public Item getItemByName(String name) throws DatabaseException;
		
		/**
		 * Return a list of all items
		 * @return a list of all Items
		 * @throws DatabaseException
		 */
		public List<Item> getItems() throws DatabaseException;
		
		
		/**
		 * Add one Tag_ID to the table
		 * @param item_id
		 * @param tag_id
		 * @throws DatabaseException
		 */
		public void addTagId(long item_id, String tag_id) throws DatabaseException;
		
		
		/**
		 * Add Tag_IDs to the table
		 * @param item_id
		 * @param tag_ids
		 * @throws DatabaseException
		 */
		public void addTagIds(long item_id, List<String> tag_ids) throws DatabaseException;
		
		
		/**
		 * Delete a Tag_ID
		 * @param tag_id
		 * @throws DatabaseException
		 */
		public void deleteTagId(String tag_id) throws DatabaseException;
		
		
		/**
		 * Get a list with all TagIDs from a specific item
		 * @param itemId
		 * @return List<String> tagId
		 * @throws DatabaseException
		 */
		public List<String> getTagId(long itemId) throws DatabaseException;
		
		
		/**
		 * Add independent item
		 * @param item_id
		 * @throws DatabaseException
		 */
		public void addIndependentItem(long item_id) throws DatabaseException;
		
		
		/**
		 * Delete independent item
		 * @param item_id
		 * @throws DatabaseException
		 */
		public void deleteIndependentItem(long item_id) throws DatabaseException;
		
		
		/**
		 * Check if item is independent 
		 * @param id
		 * @return
		 * @throws DatabaseException
		 */
		public boolean getIndependentItem(long id) throws DatabaseException;
		
		
		/**
		 * List with all independent items
		 * @return
		 * @throws DatabaseException
		 */
		public List<Long> getIndependentItems() throws DatabaseException;
		
		
		/**
		 * Add context related item
		 * @param item_id
		 * @throws DatabaseException
		 */
		public void addContextItem(long item_id) throws DatabaseException;
		
		
		/**
		 * Delete a ContextItem
		 * @param item_id
		 * @throws DatabaseException
		 */
		public void deleteContextItem(long item_id) throws DatabaseException;
		
		
		/**
		 * Proof if a item is context dependent
		 * @param id
		 * @return
		 * @throws DatabaseException
		 */
		public boolean getContextItem(long id) throws DatabaseException;
		
		
		/**
		 * List with all ContextItems
		 * @return
		 * @throws DatabaseException
		 */
		public List<Long> getContextItems() throws DatabaseException;
		
		
		/**
		 * Add attributes 
		 * @param item_id
		 * @param item
		 * @throws DatabaseException
		 */
		public void addItemAttribute(long item_id, Item item) throws DatabaseException;
		
		
		/**
		 * Delete attributes
		 * @param item_id
		 * @throws DatabaseException
		 */
		public void deleteItemAttributes(long item_id) throws DatabaseException;
		
		
		/**
		 * Get the attributes from a specific item
		 * @param id
		 * @return
		 * @throws DatabaseException
		 */
		public ItemAttribute getItemAttribute(long id) throws DatabaseException;
		
		
		/**
		 * Get a list of all attributes
		 * @return
		 * @throws DatabaseException
		 */
		public List<ItemAttribute> getItemAttributes() throws DatabaseException;
		
		
		/**
		 * Update attributes
		 * @param attributes
		 * @throws DatabaseException
		 */
		public void updateItemAttributes(ItemAttribute attributes) throws DatabaseException;
		
		
		/*
		 * Activities
		 *
		 */
		
		/**
		 * Add a new Activity to the database
		 * @param activity the activity to add
		 * @throws DatabaseException
		 */
		public void addActivity(Activity activity) throws DatabaseException;
		
		
		/**
		 * deletes an activity from the database
		 * @param activity the activity do delete
		 * @throws DatabaseException
		 */
		public void deleteActivity(Activity activity) throws DatabaseException;
		
		
		/**
		 * Deprecated! Edits an activity
		 * @param toEdit the activity to edit
		 * @param after the new values
		 * @throws DatabaseException
		 */
		public void editActivity(Activity toEdit, Activity after) throws DatabaseException;
		
		
		/**
		 * Update activity. Its important that the id of the activity is set
		 * @param activity
		 * @throws DatabaseException
		 */
		public void updateActivtiy(Activity activity) throws DatabaseException;	
		
		
		/**
		 * Get a specific Activity
		 * @param name
		 * @return Activity
		 * @throws DatabaseException
		 */
		public Activity getActivity(String name) throws DatabaseException;
	
		
		/**
		 * A list of all activites
		 * @return a list of all Activities (not AndroidActivities)
		 * @throws DatabaseException
		 */
		public List<Activity> getActivities() throws DatabaseException;
		
		
		/**
		 * Add an item to an activity
		 * @param activity_id
		 * @param item
		 */
		public void addActivityItem(long activity_id, Item item, Category category) throws DatabaseException;
		
		
		/**
		 * Get a list of items and categories and add them to database
		 * @param activity_id
		 * @param items
		 * @param category
		 * @throws DatabaseException
		 */
		public void addActivityItems(long activity_id, List<Item> items, List<Category> category) throws DatabaseException;
		
		/**
		 * Add a list of items to an activity
		 * @param activity_id
		 * @param itemsForActivity
		 */
		public void addActivityItem(long activity_id, List<Item> itemsForActivity);
		
		
		/**
		 * Delete an item from an activity
		 * @param item_id
		 * @throws DatabaseException
		 */
		public void deleteActivityItem(long item_id) throws DatabaseException;
		
		
		/**
		 * Delete a category from an activity
		 * @param category_id
		 * @throws DatabaseException
		 */
		public void deleteActivityCategory(long category_id) throws DatabaseException;
		
		
		/**
		 * Get a list with all items from an activity
		 * @param activity_id
		 * @return
		 * @throws DatabaseException
		 */
		public List<Long> getActivityItems(long activity_id) throws DatabaseException;
		
		
		/*
		 * Category
		 */
		
		/**
		 * Add a new Category to the database
		 * @param Category the Category to add
		 * @throws DatabaseException
		 */
		public void addCategory(Category category) throws DatabaseException;
		
		
		/**
		 * deletes an Category from the database
		 * @param Category the Category do delete
		 * @throws DatabaseException
		 */
		public void deleteCategory(Category category) throws DatabaseException;
		
		
		/**
		 * edits an Category
		 * @param toEdit the Category to edit
		 * @param after the new values
		 * @throws DatabaseException
		 */
		public void editCategory(Category toEdit, Category after) throws DatabaseException;
		
		
		/**
		 * Update an Category
		 * @param category
		 * @throws DatabaseException
		 */
		public void updateCategory(Category category) throws DatabaseException;
		
	
		/**
		 * Get category 
		 * @param name
		 * @return Category
		 * @throws DatabaseException
		 */
		public Category getCategory(String name) throws DatabaseException;
		
		
		/**
		 * Get category
		 * @param id
		 * @return
		 * @throws DatabaseException
		 */
		public Category getCategory(long id) throws DatabaseException;
		
		
		/**
		 * Return a list of all categories
		 * @return a list of all Categories
		 * @throws DatabaseException
		 */
		public List<Category> getCategories() throws DatabaseException;
		
		
				
		
		
		/*
		 * Location
		 */
		
		/**
		 * Add a new Location to the database
		 * @param Location the Category to add
		 * @throws DatabaseException
		 */
		public void addLocation(Location location) throws DatabaseException;
		
		
		/**
		 * deletes an Location from the database
		 * @param location the Location do delete
		 * @throws DatabaseException
		 */
		public void deleteLocation(Location location) throws DatabaseException;
		
		
		/**
		 * edits an Location
		 * @param toEdit the Location to edit
		 * @param after the new values
		 * @throws DatabaseException
		 */
		public void editLocation(Location toEdit, Location after) throws DatabaseException;
		
		
		/**
		 * Update location
		 * @param location
		 * @throws DatabaseException
		 */
		public void updateLocation(Location location) throws DatabaseException;		
	
		
		/**
		 * Get location
		 * @param name
		 * @return location
		 * @throws DatabaseException
		 */
		public Location getLocation(String name) throws DatabaseException;
		
		
		/**
		 * Get location
		 * @param loc_id
		 * @return location
		 * @throws DatabaseException
		 */
		public Location getLocation(long loc_id) throws DatabaseException;
		
		/**
		 * Return all locations
		 * @return a list of all Locations
		 * @throws DatabaseException
		 */
		public List<Location> getLocations() throws DatabaseException;
		
		
				
				
		/**
		 * Get a tag_id and return the corresponding item_id
		 * @param tagId the tagId for a specific Tag
		 * @return the item for the given tagId, null of no Item is found with the given Id
		 * @throws DatabaseException 
		 */
		public Long getItemId(String tagId) throws DatabaseException;

		/**
		 * inserts an image into the database, the img.hashCode() method is the key
		 * Note that this implementation has one problem: When 2 hashes are identical but the image is not, the system will fail. 
		 * However this case will mostly not occur in real life
		 * @param bmp the image to save
		 * @throws DatabaseException
		 */
		public void addImage(long item_id, Item item) throws DatabaseException;
		
		/**
		 * Return the the corresponding ImageHash
		 * @param item_id
		 * @return
		 * @throws DatabaseException
		 */
		public int getImageHash(long item_id) throws DatabaseException;
		
		/**
		 * returns an Image for a given hashcode
		 * @param hashCode
		 * @return the image for the hashcode
		 * @throws DatabaseException
		 */
		public String getImageString(long item_id) throws DatabaseException;
		
		
		/**
		 * Get image without an item_id
		 * @param hashCode
		 * @return
		 * @throws DatabaseException
		 */
		public String getImageString(int hashCode) throws DatabaseException;
		
		
		/**
		 * Update photo
		 * @param item
		 * @return
		 * @throws DatabaseException
		 */
		public int updatePhoto(Item item) throws DatabaseException;
		
		
		/**
		 * Delete photo
		 * @param item_id
		 * @throws DatabaseException
		 */
		public void deletePhoto(long item_id) throws DatabaseException;

}

