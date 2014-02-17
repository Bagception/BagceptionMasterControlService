package de.uniulm.bagception.bagceptionmastercontrolserver.logic;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.uniulm.bagception.bundlemessageprotocol.entities.Activity;


public class WeightedActivityList{
	private final HashMap<Activity, Integer> counter = new HashMap<Activity, Integer>();
	private final ActivityWeightComparator comparator =  new ActivityWeightComparator(counter);
	private final TreeMap<Activity, Integer> sorter = new TreeMap<Activity, Integer>(comparator);

	public WeightedActivityList() {
		
	}
	
	public void put(List<Activity> as){
		for (Activity a:as){
			Integer cnt = counter.get(a);
			if (cnt == null){
				counter.put(a, 1);
			}else{
				counter.put(a, (cnt+1));
			}
		}
		
	}

	public List<Activity> getSorted(){
		sorter.clear();
		sorter.putAll(counter);
		ArrayList<Activity> ret = new ArrayList<Activity>(sorter.keySet());
		return ret;
	}
	public int[] getWeight(){
		int[] ret = new int[sorter.size()];
		int i=0;
		
		for(Map.Entry<Activity,Integer> entry : sorter.entrySet()) {
			  Integer value = entry.getValue();
			  ret[i++]=value;
			  
			}
		return ret;
	}
	
	public class ActivityWeightComparator implements Comparator<Activity>{
		private final Map<Activity, Integer> base;
		public ActivityWeightComparator(Map<Activity, Integer> base) {
		    this.base = base;
		}
		
		@Override
	    public int compare(Activity a, Activity b) {
	        if (base.get(a) >= base.get(b)) {
	            return -1;
	        } else {
	            return 1;
	        } 
	    }
	    
		
	}
	

}

