package de.uniulm.bagception.bagceptionmastercontrolserver.logic;

import java.util.List;
import de.uniulm.bagception.bagceptionmastercontrolserver.database.DatabaseException;
import de.uniulm.bagception.bagceptionmastercontrolserver.database.DatabaseHelper;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import android.content.Context;

public class ActivityItemSystem {
	
	private Context context;
	private List<Item> items;
	private DatabaseHelper db = new DatabaseHelper(context);
	
	public ActivityItemSystem(Context context){
		this.context = context;
	}
	
	/**
	 * Gets all Items that belong to an Activity
	 * @param activity_id
	 * @return List<Item>
	 * @throws DatabaseException
	 */
	public List<Item> getAllActivityItems(long activity_id) throws DatabaseException{
		
		List<Long> item_ids = db.getActivityItems(activity_id);
		
		if(item_ids != null){
			int size = item_ids.size();
			for(int j = 0; j < size; j++){
				items.add(db.getItem(item_ids.get(j)));
			}
		} else {
			items = null;
		}
		
		return items;
	}

	/**
	 * Return a List<Item> of the missing items, after an Item is put into Bag
	 * @param itemList
	 * @param item
	 * @return List<Item>
	 */
	public List<Item> addItemToBag(List<Item> itemList, Item item){
		
		items = itemList;
		if(items.contains(item)){
			items.remove(items.indexOf(item));
		}
		
		return items;
	}
	
	/**
	 * Return a List<Item> of the missing items, after an Item is taken from the Bag
	 * @param itemList
	 * @param item
	 * @return List<Item>
	 */
	public List<Item> removeItemFromBag(List<Item> itemList, Item item){
		
		items = itemList;
		items.add(item);
		
		return items;
	}
	
}

















