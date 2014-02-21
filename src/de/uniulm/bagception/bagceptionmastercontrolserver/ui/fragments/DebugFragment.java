package de.uniulm.bagception.bagceptionmastercontrolserver.ui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import de.uniulm.bagception.bagceptionmastercontrolserver.R;
import de.uniulm.bagception.bagceptionmastercontrolserver.ui.log_fragment.LOGGER;
import de.uniulm.bagception.bluetoothclientmessengercommunication.service.BundleMessageHelper;
import de.uniulm.bagception.mcs.services.MasterControlServer;

/**
 * Part of the multi-pane view<br>
 * provides the UI for the {@link LOGGER}
 * @author phil, xaffe
 *
 */
public class DebugFragment extends Fragment{
	


	//DO NOT DELETE THIS
	private Button importDB;
	private Button exportDB;
	
	private BundleMessageHelper helper;

	
	

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		helper = new BundleMessageHelper(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.debugfragmentlayout, null, false);
		
		
		//DO NOT DELETE THIS
		importDB = (Button) v.findViewById(R.id.importDB);
		exportDB = (Button) v.findViewById(R.id.exportDB);
		importDB.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				MasterControlServer.debuginstance.getDB().importDatabase();
			}
		});
		exportDB.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				MasterControlServer.debuginstance.getDB().exportDatabase();
			}
		});

	
		return v;
	}

	
	private void log(String s){
		Log.d("MCS", s);
	}
}
