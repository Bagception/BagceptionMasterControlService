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
	
	public List<Item> getAllActivityItems(long activity_id) throws DatabaseException{
		
		List<Long> item_ids = db.getActivityItems(activity_id);
		
		if(item_ids != null){
			
			for(int j = 0; j < item_ids.size(); j++){
				items.add(db.getItem(item_ids.get(j)));
			}
		} else {
			items = null;
		}
		
		return items;
	}

	
	public List<Item> addItemToBag(List<Item> itemList, Item item){
		
		items = itemList;
		if(items.contains(item)){
			items.remove(items.indexOf(item));
		}
		
		return items;
	}
	
	
	public List<Item> removeItemFromBag(List<Item> itemList, Item item){
		
		items = itemList;
		items.add(item);
		
		return items;
	}
	
}

















