package de.uniulm.bagception.bagceptionmastercontrolserver.ui.fragments;

import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
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

	// private BundleMessageHelper helper;
	private MasterControlServer mcs;
	private ItemListArrayAdapter adapter;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// helper = new BundleMessageHelper(getActivity());
		mcs=MasterControlServer.debuginstance;
		updateUI();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View ret = inflater.inflate(R.layout.itemgrid, null);
		GridView gv = (GridView) ret.findViewById(R.id.itemGrid);
		adapter = new ItemListArrayAdapter(getActivity());
		
		gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				onListItemClick(arg1, arg2, arg3);
			}
			
		});
		gv.setAdapter(adapter);
		updateUI();

		return ret;
	}
	
	private void onListItemClick( View v, int position, long id) {
		
		Item i = adapter.getItem(position);
		//Toast.makeText(getActivity(), i.getName(),Toast.LENGTH_SHORT).show();
		
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
		adapter.clear();
		try {
			List<Item> items = mcs.getDB().getItems();
			adapter.setItemsIn(mcs.getItemIndexSystem().getCurrentItems());
			adapter.addAll(items);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		
		//setListAdapter(adapter);
	}
	

}
