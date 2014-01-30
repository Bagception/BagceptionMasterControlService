package de.uniulm.bagception.bagceptionmastercontrolserver.database;

import java.util.List;

import android.graphics.Bitmap;
import de.uniulm.bagception.bundlemessageprotocol.entities.Activity;
import de.uniulm.bagception.bundlemessageprotocol.entities.Category;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
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
		 * changes an item
		 * @param itemBefore the item to change
		 * @param after the new item values
		 * @param int the new id
		 * @throws DatabaseException
		 */
		public int editItem(Item toEdit, Item values) throws DatabaseException;
		
		
		/**
		 * get a specific item
		 * @param id
		 * @return the requested Item, null if no item is present with this id
		 * @throws DatabaseException
		 */
		public Item getItem(long id) throws DatabaseException;
		
		/**
		 * 
		 * @param name
		 * @return the requested Item, null if no item is present with this name
		 * @throws DatabaseException
		 */
		public Item getItemByName(String name) throws DatabaseException;
		
		/**
		 * 
		 * @return a list of all Items
		 * @throws DatabaseException
		 */
		public List<Item> getItems() throws DatabaseException;
		
		
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
		 * edits an activity
		 * @param toEdit the activity to edit
		 * @param after the new values
		 * @throws DatabaseException
		 */
		public void editActivity(Activity toEdit, Activity after) throws DatabaseException;
		
	
		
		/**
		 * 
		 * @return a list of all Activities (not AndroidActivities)
		 * @throws DatabaseException
		 */
		public List<Activity> getActivities() throws DatabaseException;
		
		
		
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
		 * 
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
		 * 
		 * @return a list of all Locations
		 * @throws DatabaseException
		 */
		public List<Location> getLocations() throws DatabaseException;
		
		
				
				
		/**
		 * 
		 * @param tagId the tagId for a specific Tag
		 * @return the item for the given tagId, null of no Item is found with the given Id
		 * @throws DatabaseException 
		 */
		public Item getItem(String tagId) throws DatabaseException;

		/**
		 * inserts an image into the database, the img.hashCode() method is the key
		 * Note that this implementation has one problem: When 2 hashes are identical but the image is not, the system will fail. 
		 * However this case will mostly not occur in real life
		 * @param bmp the image to save
		 * @throws DatabaseException
		 */
		public void putImage(Bitmap bmp) throws DatabaseException;
		
		/**
		 * returns an Image for a given hashcode
		 * @param hashCode
		 * @return the image for the hashcode
		 * @throws DatabaseException
		 */
		public Bitmap getImage(int hashCode) throws DatabaseException;
}

