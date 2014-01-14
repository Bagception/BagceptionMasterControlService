package de.uniulm.bagception.bagceptionmastercontrolserver.logic;

import java.util.ArrayList;
import java.util.List;

import de.uniulm.bagception.bagceptionmastercontrolserver.database.DatabaseConnector;
import de.uniulm.bagception.bundlemessageprotocol.entities.Activity;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;



public class ActivitySystem {

	private Activity currentActivity;
	
	public ActivitySystem() {
		ArrayList<Item> items = new ArrayList<Item>();
		items.add(DatabaseConnector.getItemByName(DatabaseConnector.ITEM_HOSE));
		items.add(DatabaseConnector.getItemByName(DatabaseConnector.ITEM_REGENJACKE));
		items.add(DatabaseConnector.getItemByName(DatabaseConnector.ITEM_TRINKEN));
		currentActivity = new Activity("dummy activity",items);
	}
	
	public void setCurrentActivity(Activity activity){
		this.currentActivity = activity;
	}
	
	public List<Activity> getAllActivities(){
		//TODO implement
		return null;
	}
	
	public Activity getCurrentActivity(){
		
		return currentActivity;
	}
}
