package de.uniulm.bagception.bagceptionmastercontrolserver.ui.fragments;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.json.simple.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import de.uniulm.bagception.bagceptionmastercontrolserver.R;
import de.uniulm.bagception.bagceptionmastercontrolserver.database.DatabaseException;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.bundlemessageprotocol.entities.WeatherForecast;
import de.uniulm.bagception.mcs.services.MasterControlServer;

/**
 * Part of the multi-pane view<br>
 * 
 * @author phil
 * 
 */
public class WizardOfOzFragment extends Fragment {
	private final HashSet<Long> swimmingItemIds;
	private final HashSet<Long> footballItemIds;
	public static int activityActive = 0;
	public static final int ACTIVITY_FOOTBALL= 1;
	public static final int ACTIVITY_SWIMMING = 2;
	private final WeatherForecast weatherDataSwimming = new WeatherForecast("Ulm",32,1,1,32,32,0); 
	{
		//weatherDataFootball
	}
	private final WeatherForecast weatherDataFootball = new WeatherForecast("Ulm",5,1,1,5,5,80);
	public static WeatherForecast weatherData=null; 
	public static Boolean isDark=null;
	
	private View view;
	
	public static boolean correctActivitySelected(){
		if (MasterControlServer.debuginstance == null) return false;
		try {
			if (activityActive == ACTIVITY_FOOTBALL && MasterControlServer.debuginstance.getActivitySystem().getCurrentActivity().getName().equals("Fußball")){
				return true;
			}
			if (activityActive == ACTIVITY_SWIMMING && MasterControlServer.debuginstance.getActivitySystem().getCurrentActivity().getName().equals("Schwimmen")){
				return true;
			}
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private HashSet<Long> currentSet;
	{
		//XXX item IDs 
		swimmingItemIds = new HashSet<Long>();
		swimmingItemIds.add(3l);
		swimmingItemIds.add(10l);
		swimmingItemIds.add(13l);
		swimmingItemIds.add(29l);
		swimmingItemIds.add(35l);
		swimmingItemIds.add(36l);
		swimmingItemIds.add(37l);
		swimmingItemIds.add(41l);
		
		
		
		
		footballItemIds = new HashSet<Long>();
		footballItemIds.add(4l);
		footballItemIds.add(5l);
		footballItemIds.add(6l);
		footballItemIds.add(10l);
		footballItemIds.add(12l);
		footballItemIds.add(13l);
		footballItemIds.add(19l);
		footballItemIds.add(15l);
		footballItemIds.add(24l);
		
		
		
	}
	
	private MasterControlServer mcs;
	private ItemListArrayAdapter adapter;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mcs=MasterControlServer.debuginstance;
		updateUI();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	}

	@Override
	public synchronized View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		
		//activity select for sorting
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Aktivität?")
				.setCancelable(true)
				.setPositiveButton("fußball",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								currentSet = footballItemIds;
								weatherData = weatherDataFootball; 
								isDark = true;
								
								activityActive = ACTIVITY_FOOTBALL;
								
								initArray();
							}
						})
				.setNegativeButton("schwimmen",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								currentSet = swimmingItemIds;
								weatherData = weatherDataSwimming;
								isDark = false;
								
								activityActive = ACTIVITY_SWIMMING;
								
								initArray();
							}
						});
		
		AlertDialog alert = builder.create();
		alert.show();
		
	
		
		View ret = inflater.inflate(R.layout.itemgrid, null);

		this.view = ret;
		return ret;
	}
	@Override
	public void onResume() {
		initArray();
		super.onResume();
	}
	private void initArray(){
		GridView gv = (GridView) view.findViewById(R.id.itemGrid);
		adapter = new ItemListArrayAdapter(getActivity());
		
		try {
			List<Item> items = mcs.getDB().getItems();
			if (currentSet == null){
				adapter.addAll(items);
			}else{
				adapter.addAll(items,currentSet);
			}
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		
		gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				onListItemClick(arg1, arg2, arg3);
			}
			
		});
		gv.setAdapter(adapter);
		updateUI();
	}
	
	private synchronized void onListItemClick( View v, int position, long id) {
		
		Item i = adapter.getItem(position);
		Toast.makeText(getActivity(), i.getId()+"",Toast.LENGTH_SHORT).show();
		
		try {
			if (i.getIds().size() < 1){
				
				UUID uid = UUID.randomUUID();
				i.getIds().add(uid.toString());
				mcs.getDB().editItem(i, i);
			}
			mcs.itemScanned(i,i.getIds().get(0));		
			mcs.getItemIndexSystem().caseClosed();
		} catch (DatabaseException e) {
			e.printStackTrace();
		}

		
		updateUI();
	}

	
	private synchronized void updateUI(){
		if (adapter == null){
			return;
		}
		adapter.setItemsIn(mcs.getItemIndexSystem().getCurrentItems());
	}
	

}
