package de.uniulm.bagception.bagceptionmastercontrolserver.ui.fragments;

import java.util.HashSet;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
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
	
	private final String weatherDataSwimming = ""; //TODO weatherData for swimming here
	private final String weatherDataFootball = ""; //TODO weatherData for football here
	public static String weatherData=""; 
	
	private View view;
	
	private HashSet<Long> currentSet;
	{
		//XXX item IDs 
		swimmingItemIds = new HashSet<Long>();
		swimmingItemIds.add(15l);
		swimmingItemIds.add(13l);
		swimmingItemIds.add(10l);
		
		footballItemIds = new HashSet<Long>();
		footballItemIds.add(14l);
		footballItemIds.add(8l);
		footballItemIds.add(5l);
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
	
	private void onListItemClick( View v, int position, long id) {
		
		Item i = adapter.getItem(position);
		Toast.makeText(getActivity(), i.getId()+"",Toast.LENGTH_SHORT).show();
		
		try {
			
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
