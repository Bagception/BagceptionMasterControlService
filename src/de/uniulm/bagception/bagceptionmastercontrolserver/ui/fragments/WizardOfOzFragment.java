package de.uniulm.bagception.bagceptionmastercontrolserver.ui.fragments;

import java.util.List;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import de.uniulm.bagception.bagceptionmastercontrolserver.database.DatabaseException;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.mcs.services.MasterControlServer;

/**
 * Part of the multi-pane view<br>
 * 
 * @author phil
 * 
 */
public class WizardOfOzFragment extends ListFragment {

	// private BundleMessageHelper helper;
	private MasterControlServer mcs;
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
	public void onListItemClick(ListView l, View v, int position, long id) {
		Item i = (Item)getListAdapter().getItem(position);
		//Toast.makeText(getActivity(), i.getName(),Toast.LENGTH_SHORT).show();
		
		try {
			
			mcs.itemScanned(i,i.getIds().get(0));		
			
			mcs.getItemIndexSystem().caseClosed();
		} catch (DatabaseException e) {
			e.printStackTrace();
		}

		
		updateUI();
	}

	
	private void updateUI(){
		ItemListArrayAdapter adapter = null;
		try {
			List<Item> items = mcs.getDB().getItems();
			adapter = new ItemListArrayAdapter(getActivity());
			adapter.setItemsIn(mcs.getItemIndexSystem().getCurrentItems());
			adapter.addAll(items);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		
		setListAdapter(adapter);
	}
	

}
