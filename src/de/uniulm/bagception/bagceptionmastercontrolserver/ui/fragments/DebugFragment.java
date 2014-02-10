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
import android.widget.TextView;
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
	private TextView providerTV;
	private TextView accuracyTV;
	private TextView longitudeTV;
	private TextView latitudeTV;


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
		providerTV = (TextView) v.findViewById(R.id.providerTV);
		accuracyTV = (TextView) v.findViewById(R.id.accuracyTV);
		longitudeTV = (TextView) v.findViewById(R.id.longitudeTV);
		latitudeTV = (TextView) v.findViewById(R.id.latitudeTV);

		

		Button testLocation = (Button) v.findViewById(R.id.testlocation);
		testLocation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(),LocationService.class);
				i.putExtra("receiverTag", mResultreceiver);
//				i.putExtra(OurLocation.REQUEST_TYPE, OurLocation.GETLOCATION);
//				i.putExtra(OurLocation.REQUEST_TYPE, OurLocation.RESOLVEADDRESS);
//				i.putExtra(OurLocation.ADDRESS, "Weinhof 20 89083 Ulm");
				i.putExtra(OurLocation.REQUEST_TYPE, OurLocation.RESOLVECOORDS);
//				i.putExtra(OurLocation.LATITUDE, 49);
//				i.putExtra(OurLocation.LONGITUDE, 9.8);
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
				providerTV.setText(resultData.getString(OurLocation.PROVIDER, ""));
				accuracyTV.setText(""+resultData.getFloat(OurLocation.ACCURACY, 0));
				latitudeTV.setText(""+resultData.getDouble(OurLocation.LATITUDE, 0));
				longitudeTV.setText(""+resultData.getDouble(OurLocation.LONGITUDE, 0));
			}
		}
	}
	
	private void log(String s){
		Log.d("MCS", s);
	}


	

}
