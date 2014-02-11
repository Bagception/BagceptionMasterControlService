package de.uniulm.bagception.bagceptionmastercontrolserver.ui.fragments;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.hardware.Camera.Area;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import de.uniulm.bagception.bagceptionmastercontrolserver.R;
import de.uniulm.bagception.bagceptionmastercontrolserver.service.location.LocationService;
import de.uniulm.bagception.bagceptionmastercontrolserver.service.weatherforecast.WeatherForecastService;
import de.uniulm.bagception.bagceptionmastercontrolserver.ui.log_fragment.LOGGER;
import de.uniulm.bagception.bluetoothclientmessengercommunication.service.BundleMessageHelper;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage.BUNDLE_MESSAGE;
import de.uniulm.bagception.bundlemessageprotocol.entities.WifiBTDevice;
import de.uniulm.bagception.intentservicecommunication.MyResultReceiver;
import de.uniulm.bagception.intentservicecommunication.MyResultReceiver.Receiver;
import de.uniulm.bagception.mcs.services.MasterControlServer;
import de.uniulm.bagception.services.attributes.OurLocation;
import de.uniulm.bagception.services.attributes.WeatherForecast;

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
	
	private Button   resolveAddressBtn;
	private TextView resolveAddressLatTV;
	private TextView resolveAddressLngTV;
	private EditText resolveAddressTF;
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
		
		
		resolveAddressLatTV = (TextView) v.findViewById(R.id.rALatTV);
		resolveAddressLngTV = (TextView) v.findViewById(R.id.rALngTV);
		resolveAddressTF    = (EditText) v.findViewById(R.id.rATF);
		resolveAddressBtn = (Button) v.findViewById(R.id.resolveAddressBtn);
		resolveAddressBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String address = resolveAddressTF.getText().toString();
				Intent i = new Intent(getActivity(), LocationService.class);
				i.putExtra(OurLocation.REQUEST_TYPE, OurLocation.RESOLVEADDRESS);
				i.putExtra(OurLocation.ADDRESS, address);
				getActivity().startService(i);
			}
		});

		Button testLocation = (Button) v.findViewById(R.id.testlocation);
		testLocation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
//				Intent i = new Intent(getActivity(),LocationService.class);
//				i.putExtra("receiverTag", mResultreceiver);
////				i.putExtra(OurLocation.REQUEST_TYPE, OurLocation.GETLOCATION);
////				i.putExtra(OurLocation.REQUEST_TYPE, OurLocation.RESOLVEADDRESS);
////				i.putExtra(OurLocation.ADDRESS, "Weinhof 20 89083 Ulm");
//				i.putExtra(OurLocation.REQUEST_TYPE, OurLocation.RESOLVECOORDS);
////				i.putExtra(OurLocation.LATITUDE, 49);
////				i.putExtra(OurLocation.LONGITUDE, 9.8);
//				getActivity().startService(i);
				
//				Intent i = new Intent(getActivity(),LocationService.class);
//				i.putExtra("receiverTag", mResultreceiver);
//				i.putExtra(OurLocation.REQUEST_TYPE, OurLocation.GETWIFIAPS);
//				getActivity().startService(i);
				
				Intent i = new Intent(getActivity(),WeatherForecastService.class);
				i.putExtra(WeatherForecast.LATITUDE, 48d);
				i.putExtra(WeatherForecast.LONGITUDE, 9.6d);
				i.putExtra("receiverTag", mResultreceiver);
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
				Intent i = new Intent(getActivity(),LocationService.class);
				getActivity().stopService(i);
			}
			if(resultData.getString(OurLocation.RESPONSE_TYPE).equals(OurLocation.RESOLVEADDRESS)){
				resolveAddressLatTV.setText(""+resultData.getString(OurLocation.LATITUDE, ""));
				resolveAddressLngTV.setText(""+resultData.getString(OurLocation.LONGITUDE, ""));
				Intent i = new Intent(getActivity(),LocationService.class);
				getActivity().stopService(i);
			}
			if(resultData.getString(OurLocation.RESPONSE_TYPE).equals(OurLocation.GETWIFIAPS)){
				log(resultData.getString(OurLocation.NAME) + " " + 
						resultData.getString(OurLocation.MAC));
				WifiBTDevice dev = new WifiBTDevice(resultData.getString(OurLocation.NAME), resultData.getString(OurLocation.MAC));
				helper.sendMessageSendBundle(BundleMessage.getInstance().createBundle(BUNDLE_MESSAGE.WIFI_SEARCH_REQUEST, dev));
			}
			if(resultData.getString(OurLocation.RESPONSE_TYPE).equals(WeatherForecast.WEATHERFORECAST)){
				String s = resultData.getString("payload");
				try {
					JSONObject object = new JSONObject(s);
					String city = object.getString("city");
					float temp = Float.parseFloat(object.getString(WeatherForecast.TEMP));
					float wind = Float.parseFloat(object.getString(WeatherForecast.WIND));
					float clouds = Float.parseFloat(object.getString(WeatherForecast.CLOUDS));
					float temp_min = Float.parseFloat(object.getString(WeatherForecast.TEMP_MIN));
					float temp_max = Float.parseFloat(object.getString(WeatherForecast.TEMP_MAX));
					float rain = Float.parseFloat(object.getString(WeatherForecast.RAIN));
					de.uniulm.bagception.bundlemessageprotocol.entities.WeatherForecast forecast = new de.uniulm.bagception.bundlemessageprotocol.entities.WeatherForecast(city, temp, wind, clouds, temp_min, temp_max, rain);
					helper.sendMessageSendBundle(BundleMessage.getInstance().createBundle(BUNDLE_MESSAGE.WEATHERFORECAST_REPLY, forecast));
					log(" city: " + forecast.getCity() + 
						" temp: " + forecast.getTemp() + 
						" wind: " + forecast.getWind() + 
						" clouds: " + forecast.getClouds() +
						" tempMin: " + forecast.getTemp_min() + 
						" tempMax: " + forecast.getTemp_max() + 
						" rain: " + forecast.getRain());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
		}
	}
	
	private void log(String s){
		Log.d("MCS", s);
	}


	

}
