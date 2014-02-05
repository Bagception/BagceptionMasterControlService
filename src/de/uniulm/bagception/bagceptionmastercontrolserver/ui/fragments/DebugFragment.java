package de.uniulm.bagception.bagceptionmastercontrolserver.ui.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import de.uniulm.bagception.bagceptionmastercontrolserver.R;
import de.uniulm.bagception.bagceptionmastercontrolserver.service.location.LocationService;
import de.uniulm.bagception.bagceptionmastercontrolserver.ui.log_fragment.LOGGER;
import de.uniulm.bagception.intentservicecommunication.MyResultReceiver;
import de.uniulm.bagception.intentservicecommunication.MyResultReceiver.Receiver;
import de.uniulm.bagception.mcs.services.MasterControlServer;
import de.uniulm.bagception.services.attributes.OurLocation;

/**
 * Part of the multi-pane view<br>
 * provides the UI for the {@link LOGGER}
 * @author phil
 *
 */
public class DebugFragment extends Fragment implements Receiver{
	
	private MyResultReceiver mResultreceiver;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.debugfragmentlayout, null, false);

		Button sendUpdate =(Button) v.findViewById(R.id.sendupdate);
		sendUpdate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				MasterControlServer.DEBUG();
				
			}
		});
		
		mResultreceiver = new MyResultReceiver(new Handler());
		mResultreceiver.setReceiver(this);

		

		Button testLocation = (Button) v.findViewById(R.id.testlocation);
		testLocation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(),LocationService.class);
				i.putExtra("receiverTag", mResultreceiver);
				i.putExtra(OurLocation.REQUEST_TYPE, OurLocation.GETLOCATION);
				getActivity().startService(i);
			}
		});
		
		return v;
	}

	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		log("answer received!");
		if(resultData.containsKey(OurLocation.RESPONSE_TYPE)){
			if(resultData.getString(OurLocation.RESPONSE_TYPE).equals(OurLocation.LOCATION)){
				log("it works..." + resultData.getString(OurLocation.REQUEST_TYPE));
			}
		}
	}
	
	private void log(String s){
		Log.d("MCS", s);
	}


	

}
