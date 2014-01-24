package de.uniulm.bagception.bagceptionmastercontrolserver.database;

import java.util.List;

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
		 * @throws DatabaseException
		 */
		public void editItem(Item itemBefore, Item after) throws DatabaseException;
		
		
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
		
		
				
				

		
}
