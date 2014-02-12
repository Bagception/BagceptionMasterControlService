package de.uniulm.bagception.bagceptionmastercontrolserver.logic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import de.uniulm.bagception.bagceptionmastercontrolserver.database.DatabaseException;
import de.uniulm.bagception.bagceptionmastercontrolserver.database.DatabaseInterface;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;

public class ItemIndexSystem {

	private final HashSet<String> itemsInContainer = new HashSet<String>();
	private final DatabaseInterface dbHelper;
	
	
	public ItemIndexSystem(DatabaseInterface dbHelper){
		this.dbHelper = dbHelper;
	}
	
	
	/**
	 * determines if the item is put in or taken out of the bag, also puts the item in or takes it out 
	 * @param item the scanned item
	 * @return true if the item was not in the bag, that means we put the item in the bag
	 * false when the item was removed from the container, that means the item was taken out
	 */
	public boolean itemScanned(Item item){
		if (isItemInContainer(item)){
			removeItem(item);
			return false;
		}else{
			addItem(item);
			return true;
		}
	}
	
	private void addItem(Item item){
		List<String> allIDsFromItem = item.getIds();
		for (String id_:allIDsFromItem){
			itemsInContainer.add(id_);
		}
	}
	private void removeItem(Item item){
		List<String> allIDsFromItem = item.getIds();
		for (String id_:allIDsFromItem){
			itemsInContainer.remove(id_);
		}
	}
	
	private boolean isItemInContainer(Item item){
		List<String> allIDsFromItem = item.getIds();
		for (String id_:allIDsFromItem){
			if (itemsInContainer.contains(id_)){
				return true;
			}
		}
		return false;
	}
	
	public List<Item> getCurrentItems(){
		ArrayList<Item> ret = new ArrayList<Item>();
		for (String id_:itemsInContainer){
			Item i;
			
			try {
				long itemId=dbHelper.getItemId(id_);
				i = dbHelper.getItem(itemId);
				ret.add(i);
			} catch (DatabaseException e) {
				e.printStackTrace();
			}
			
			
		}
		return ret;
	}
	
	public void clearAllItems(){
		itemsInContainer.clear();
	}
}
