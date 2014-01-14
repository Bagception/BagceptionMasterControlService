package de.uniulm.bagception.bagceptionmastercontrolserver.logic;

import java.util.ArrayList;
import java.util.List;

import de.uniulm.bagception.bundlemessageprotocol.entities.Activity;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;



public class ActivitySystem {

	private Activity currentActivity;
	
	public ActivitySystem() {
		ArrayList<Item> items = new ArrayList<Item>();
		items.add(new Item("Jacke"));
		items.add(new Item("Hose"));
		items.add(new Item("TiSchört"));
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
