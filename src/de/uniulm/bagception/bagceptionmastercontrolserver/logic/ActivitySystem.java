package de.uniulm.bagception.bagceptionmastercontrolserver.logic;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import de.uniulm.bagception.bagceptionmastercontrolserver.database.DatabaseException;
import de.uniulm.bagception.bagceptionmastercontrolserver.database.DatabaseHelper;
import de.uniulm.bagception.bundlemessageprotocol.entities.Activity;
import de.uniulm.bagception.bundlemessageprotocol.entities.ActivityPriorityList;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.bundlemessageprotocol.entities.Location;



public class ActivitySystem {

	private Activity currentActivity;
	private Context context;
	private DatabaseHelper db = new DatabaseHelper(context);
	private boolean manuallyDetermActivity=false;
	
	public ActivitySystem() throws DatabaseException {
		currentActivity = Activity.NO_ACTIVITY;
		
	}
	
	public ActivitySystem(Context context) {
		this.context = context;
	}
	
	public void setCurrentActivity(Activity activity) throws DatabaseException{
		this.currentActivity = activity;
//		location = activity.getLocation();

	}
	
	
	
	public boolean isManuallyDetermActivity() {
		return manuallyDetermActivity;
	}

	public void setManuallyDetermActivity(boolean manuallyDetermActivity) {
		this.manuallyDetermActivity = manuallyDetermActivity;
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
	 * Tries to recognize an activity by the given items
	 * @param itemsInBag the given items
	 * @return a tuple(a,b) with a: a List of Activities that are possible, b: the matching item count  
	 * @throws DatabaseException
	 */
	public ActivityPriorityList activityRecognition(List<Item> itemsInBag) throws DatabaseException{
		WeightedActivityList wl = new WeightedActivityList();
		for(Item i:itemsInBag){
			if (i.getIndependentItem()){
				continue;
			}
			
			List<Activity> as = db.getActivitesByItem(i.getId());

			if (as!=null){
				wl.put(as);
			}
				
		}
		
		return new ActivityPriorityList(wl.getSorted(),wl.getWeight());
		
	}

	public class Tuple<X, Y> { 
		  public final X x; 
		  public final Y y; 
		  public Tuple(X x, Y y) { 
		    this.x = x; 
		    this.y = y; 
		  } 
		} 
	
}
