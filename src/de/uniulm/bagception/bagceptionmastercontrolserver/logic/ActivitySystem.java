package de.uniulm.bagception.bagceptionmastercontrolserver.logic;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import de.uniulm.bagception.bagceptionmastercontrolserver.database.DatabaseConnector;
import de.uniulm.bagception.bagceptionmastercontrolserver.database.DatabaseException;
import de.uniulm.bagception.bagceptionmastercontrolserver.database.DatabaseHelper;
import de.uniulm.bagception.bundlemessageprotocol.entities.Activity;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.bundlemessageprotocol.entities.Location;



public class ActivitySystem {

	private Activity currentActivity;
	private Location location;
	private long activity_id;
	private List<Item> items;
	private Context context;
	private DatabaseHelper db = new DatabaseHelper(context);
	
	public ActivitySystem() throws DatabaseException {
//		ArrayList<Item> items = new ArrayList<Item>();
//		items.add(DatabaseConnector.getItemByName(DatabaseConnector.ITEM_HOSE));
//		items.add(DatabaseConnector.getItemByName(DatabaseConnector.ITEM_REGENJACKE));
//		items.add(DatabaseConnector.getItemByName(DatabaseConnector.ITEM_TRINKEN));
//		currentActivity = new Activity("dummy activity",items,new Location(1,"locName",123f,456f,10,"0xaffe"));
		
//		activity_id = currentActivity.getId();
//		List<Long> item_ids = db.getActivityItems(activity_id);
//		
//		for(int j = 0; j < item_ids.size(); j++){
//			
//			items.add(db.getItem(item_ids.get(j)));
//		}
		
	}
	
	public ActivitySystem(Context context) {
		this.context = context;
	}
	
	public void setCurrentActivity(Activity activity){
		this.currentActivity = activity;
//		location = activity.getLocation();
	}
	
	public List<Activity> getAllActivities(){
		
		List<Activity> activities = new ArrayList<Activity>();
		try {
			activities = db.getActivities();
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		
		return activities;
	}
	
	public Activity getCurrentActivity(){
		
		return currentActivity;
	}
}
