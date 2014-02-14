package de.uniulm.bagception.bagceptionmastercontrolserver.logic;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
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
	private List<Long> item_ids;
	private Context context;
	private DatabaseHelper db = new DatabaseHelper(context);
	
	public ActivitySystem() throws DatabaseException {
		currentActivity = new Activity("dummy activity",items,new Location(1,"locName",123f,456f,10,"0xaffe"));
		
	}
	
	public ActivitySystem(Context context) {
		this.context = context;
	}
	
	public void setCurrentActivity(Activity activity){
		this.currentActivity = activity;
//		location = activity.getLocation();
	}
	
	/**
	 * Return a List of all Activites
	 * @return List<Activtiy>
	 */
	public List<Activity> getAllActivities(){
		
		List<Activity> activities = new ArrayList<Activity>();
		try {
			activities = db.getActivities();
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		
		return activities;
	}
	
	/**
	 * Return the current activtiy
	 * @return Activity
	 */
	public Activity getCurrentActivity(){
		
		return currentActivity;
	}
	
	/**
	 * Method to guess the activity the user want to start. The method return a List of all possible activites
	 * @param item_ids
	 * @return List<Activity>
	 * @throws DatabaseException
	 */
	public List<Activity> activityRecognition(List<Long> item_ids) throws DatabaseException{
		
		List<Activity> activityList = new ArrayList<Activity>();
		
		if(item_ids != null){
			
			int size = item_ids.size();
			for(int j = 0; j < size; j++){
				List<Activity> alist = db.getActivitesByItem(item_ids.get(j));
				
				if(alist != null){
					for(Activity act : alist){
						activityList.add(act);
					}
				}
			}
		}
		
		return activityList;
	}
	
}
