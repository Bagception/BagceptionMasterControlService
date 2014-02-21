package de.uniulm.bagception.bagceptionmastercontrolserver.logic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.util.Log;

import de.uniulm.bagception.bagceptionmastercontrolserver.database.DatabaseException;
import de.uniulm.bagception.bagceptionmastercontrolserver.database.DatabaseHelper;
import de.uniulm.bagception.bagceptionmastercontrolserver.ui.log_fragment.LOGGER;
import de.uniulm.bagception.bundlemessageprotocol.entities.Activity;
import de.uniulm.bagception.bundlemessageprotocol.entities.ActivityPriorityList;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.bundlemessageprotocol.entities.Location;



public class ActivitySystem {

	private Activity currentActivity;
	private final DatabaseHelper db;
	private boolean manuallyDetermActivity=false;
	
	public ActivitySystem(DatabaseHelper db) throws DatabaseException {
		this.db = db;
		currentActivity = Activity.NO_ACTIVITY;
	}
	
	
	public void setCurrentActivity(Activity activity) throws DatabaseException{
		this.currentActivity = activity;
		LOGGER.C(this, "activity changed: "+activity.getName());
//		String d="activty changed: "+activity.getName()+"\n"+" items: ";
//		Item last=null;
//		for(Item i:activity.getItemsForActivity()){
//			d+=i.getName()+"("+i.hashCode()+")";
//			if (last!=null){
//				d+=last.equals(i);
//			}
//			d+=", ";
//		}
//		LOGGER.C(this, d);
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
	 * @throws DatabaseException 
	 * Return the current activitiy
	 * @return Activity
	 * @throws  
	 */
	public Activity getCurrentActivity() throws DatabaseException {
		
		//TODO 
		// Add independent items
//		currentActivity.getItemsForActivity().addAll(INDEPENDENTITEMS);
//		
//		
//		currentActivity.getItemsForActivity().
		
		long id = currentActivity.getId();
		String name = currentActivity.getName();
		List<Item> ac_items = currentActivity.getItemsForActivity();
		Location loc = currentActivity.getLocation();
		
		List<Long> item_ids = db.getIndependentItems();
		List<Item> items = new ArrayList<Item>();
		
		if(item_ids != null){
			
			for(int j = 0; j < item_ids.size(); j++){
				Item item = db.getItem(item_ids.get(j));
				items.add(item);
			}
			
			ac_items.addAll(items);
			currentActivity = new Activity(id, name, ac_items, loc);
		}
		
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
