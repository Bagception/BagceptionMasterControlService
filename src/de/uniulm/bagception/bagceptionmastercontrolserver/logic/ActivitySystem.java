package de.uniulm.bagception.bagceptionmastercontrolserver.logic;

import java.util.ArrayList;
import java.util.List;

import de.uniulm.bagception.bagceptionmastercontrolserver.database.DatabaseConnector;
import de.uniulm.bagception.bundlemessageprotocol.entities.Activity;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.bundlemessageprotocol.entities.Location;



public class ActivitySystem {

	private Activity currentActivity;
	
	public ActivitySystem() {
		ArrayList<Item> items = new ArrayList<Item>();
		items.add(DatabaseConnector.getItemByName(DatabaseConnector.ITEM_HOSE));
		items.add(DatabaseConnector.getItemByName(DatabaseConnector.ITEM_REGENJACKE));
		items.add(DatabaseConnector.getItemByName(DatabaseConnector.ITEM_TRINKEN));
		currentActivity = new Activity("dummy activity",items,new Location(1,"locName",123f,456f,10,"0xaffe"));
	}
	
	public void setCurrentActivity(Activity activity){
		this.currentActivity = activity;
	}
	
	public List<Activity> getAllActivities(){
		return null;
	}
	
	public Activity getCurrentActivity(){
		
		return currentActivity;
	}
}
