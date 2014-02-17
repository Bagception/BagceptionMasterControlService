package de.uniulm.bagception.bagceptionmastercontrolserver.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
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
	private ItemIndexSystem iiS = new ItemIndexSystem(db);
	
	public ActivitySystem() throws DatabaseException {
		currentActivity = new Activity("dummy activity",items,new Location(1,"locName",123f,456f,10,"0xaffe"));
		
	}
	
	public ActivitySystem(Context context) {
		this.context = context;
	}
	
	public void setCurrentActivity(Activity activity) throws DatabaseException{
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
	 * Return the current activitiy
	 * @return Activity
	 * @throws  
	 */
	public Activity getCurrentActivity() {
		
		//TODO 
		// Add independent items
		
		return currentActivity;
	}
	
	
	/**
	 * Method to guess the activity the user want to start. The method returns a List of all possible activites
	 * @param item_ids
	 * @return List<Activity>
	 * @throws DatabaseException
	 */
	public List<Activity> activityRecognition(List<Item> itemsInBag) throws DatabaseException{
		WeightedActivityList wl = new WeightedActivityList();
		for(Item i:itemsInBag){
			if (i.getIndependentItem()){
				continue;
			}
			List<Activity> as = db.getActivitesByItem(i.getId());
			wl.put(as);
		}
		
		return wl.getSorted();
		
	}

	
}
