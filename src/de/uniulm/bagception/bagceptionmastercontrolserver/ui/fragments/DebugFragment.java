package de.uniulm.bagception.bagceptionmastercontrolserver.ui.fragments;

import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import android.app.Activity;
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
import de.uniulm.bagception.bagceptionmastercontrolserver.service.calendar.CalendarService;
import de.uniulm.bagception.bagceptionmastercontrolserver.service.location.LocationService;
import de.uniulm.bagception.bagceptionmastercontrolserver.service.weatherforecast.WeatherForecastService;
import de.uniulm.bagception.bagceptionmastercontrolserver.ui.log_fragment.LOGGER;
import de.uniulm.bagception.bluetoothclientmessengercommunication.service.BundleMessageHelper;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage.BUNDLE_MESSAGE;
import de.uniulm.bagception.bundlemessageprotocol.entities.CalendarEvent;
import de.uniulm.bagception.bundlemessageprotocol.entities.Position;
import de.uniulm.bagception.bundlemessageprotocol.entities.WifiBTDevice;
import de.uniulm.bagception.intentservicecommunication.MyResultReceiver;
import de.uniulm.bagception.intentservicecommunication.MyResultReceiver.Receiver;
import de.uniulm.bagception.mcs.services.MasterControlServer;
import de.uniulm.bagception.services.ServiceNames;
import de.uniulm.bagception.services.attributes.Calendar;
import de.uniulm.bagception.services.attributes.OurLocation;
import de.uniulm.bagception.services.attributes.WeatherForecast;

/**
 * Part of the multi-pane view<br>
 * provides the UI for the {@link LOGGER}
 * @author phil, xaffe
 *
 */
public class DebugFragment extends Fragment implements Receiver{
	
	private MyResultReceiver mResultreceiver;
	
	private Button getLocationBtn;
	private Button resolveAddressBtn;
	private Button resolveCoordsBtn;
	private Button getWifiAPBtn;
	private Button getBTDevicesBtn;
	private Button getWeatherForecastBtn;
	private Button calendarNamesBtn;
	private Button calendarEventsBtn;

	
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
		
		getLocationBtn = (Button) v.findViewById(R.id.getLocationBtn);
		resolveAddressBtn = (Button) v.findViewById(R.id.resolveAddressBtn);
		resolveCoordsBtn = (Button) v.findViewById(R.id.resolveCoordsBtn);
		getWifiAPBtn = (Button) v.findViewById(R.id.getWifiAPBtn);
		getBTDevicesBtn = (Button) v.findViewById(R.id.getBTDevicesBtn);
		getWeatherForecastBtn = (Button) v.findViewById(R.id.weatherBtn);
		calendarNamesBtn = (Button) v.findViewById(R.id.calendarNamesBtn);
		calendarEventsBtn = (Button) v.findViewById(R.id.calendarEventsBtn);
		
		
		getLocationBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// GET CURRENT LOCATION
				Intent i = new Intent(getActivity(),LocationService.class);
				i.putExtra("receiverTag", mResultreceiver);
				i.putExtra(OurLocation.REQUEST_TYPE, OurLocation.GETLOCATION);
				getActivity().startService(i);
			}
		});
		resolveAddressBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// RESOLVE ADDRESS TO COORDS
				Intent i = new Intent(getActivity(),LocationService.class);
				i.putExtra("receiverTag", mResultreceiver);
				i.putExtra(OurLocation.REQUEST_TYPE, OurLocation.RESOLVEADDRESS);
				i.putExtra(OurLocation.ADDRESS, "Weinhof 20 89083 Ulm");
				getActivity().startService(i);
			}
		});
		resolveCoordsBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// RESOLVE COORDS TO ADDRESS
				Intent i = new Intent(getActivity(),LocationService.class);
				i.putExtra("receiverTag", mResultreceiver);
				i.putExtra(OurLocation.REQUEST_TYPE, OurLocation.RESOLVECOORDS);
				i.putExtra(OurLocation.LATITUDE, 48.399014);
				i.putExtra(OurLocation.LONGITUDE, 9.99275);
				getActivity().startService(i);
			}
		});
		getWifiAPBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// GET WIFI ACCESS POINTS
				Intent i = new Intent(getActivity(),LocationService.class);
				i.putExtra("receiverTag", mResultreceiver);
				i.putExtra(OurLocation.REQUEST_TYPE, OurLocation.GETWIFIAPS);
				getActivity().startService(i);
			}
		});
		getBTDevicesBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				log("button pressed");
				// GET ALL BLUETOOTH DEVICES
				Intent i = new Intent(getActivity(),LocationService.class);
				i.putExtra("receiverTag", mResultreceiver);
				i.putExtra(OurLocation.REQUEST_TYPE, OurLocation.GETBLUETOOTHDEVICES);
				getActivity().startService(i);
			}
		});
		getWeatherForecastBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// WEATHER FORECAST
				Intent i = new Intent(getActivity(),WeatherForecastService.class);
				i.putExtra(WeatherForecast.LATITUDE, 48.399014);
				i.putExtra(WeatherForecast.LONGITUDE, 9.99275);
				i.putExtra("receiverTag", mResultreceiver);
				getActivity().startService(i);
			}
		});
		calendarNamesBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String serviceString = ServiceNames.CALENDAR_SERVICE;
				Intent i = new Intent(getActivity(),CalendarService.class);
				i.putExtra("receiverTag", mResultreceiver);
				i.putExtra(Calendar.REQUEST_TYPE, Calendar.CALENDAR_NAMES);
				getActivity().startService(i);
			}
		});
		calendarEventsBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), CalendarService.class);
				i.putExtra("receiverTag", mResultreceiver);
				// limit the amount of requesting calendar events
//				i.putExtra(Calendar.NUMBER_OF_EVENTS, 3);
				// adding optional calendar ids or names
//				i.putExtra("calendarIDs", calendarIDs);
//				i.putExtra(Calendar.CALENDAR_NAMES, calendarNames);
				i.putExtra(Calendar.REQUEST_TYPE, Calendar.CALENDAR_EVENTS);
				getActivity().startService(i);
			}
		});
		
		Button sendUpdate =(Button) v.findViewById(R.id.sendupdate);
		sendUpdate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
					MasterControlServer.DEBUG();
				
			}
		});
		
		
		mResultreceiver = new MyResultReceiver(new Handler());
		mResultreceiver.setReceiver(this);

		return v;
	}

	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		log("answer received!");
		
		
		if(resultData.containsKey(OurLocation.RESPONSE_TYPE)){
			
			// LOCATIONSERVICE LOCATION REPLY
			if(resultData.getString(OurLocation.RESPONSE_TYPE).equals(OurLocation.LOCATION)){
//				providerTV.setText(resultData.getString(OurLocation.PROVIDER, ""));
//				accuracyTV.setText(""+resultData.getFloat(OurLocation.ACCURACY, 0));
//				latitudeTV.setText(""+resultData.getDouble(OurLocation.LATITUDE, 0));
//				longitudeTV.setText(""+resultData.getDouble(OurLocation.LONGITUDE, 0));
				log("------- LOCATION REPLY------");
				log("provider: " + resultData.getString(OurLocation.PROVIDER, ""));
				log("accuracy: " +resultData.getFloat(OurLocation.ACCURACY, 0));
				log("latitude: " +resultData.getDouble(OurLocation.LATITUDE, 0));
				log("longitude: " +resultData.getDouble(OurLocation.LONGITUDE, 0));
				// stop service
				Intent i = new Intent(getActivity(),LocationService.class);
				getActivity().stopService(i);
				
				// sending current position to client
				Position currentPosition = new Position(resultData.getString(OurLocation.PROVIDER, ""),
														resultData.getFloat(OurLocation.LATITUDE, 0),
														resultData.getFloat(OurLocation.LONGITUDE, 0),
														resultData.getFloat(OurLocation.ACCURACY, 0));
				helper.sendMessageSendBundle(BundleMessage.getInstance().createBundle(BUNDLE_MESSAGE.LOCATION_REPLY, currentPosition));
			}
			
			// LOCATIONSERVICE RESOLVE ADDRESS REPLY
			if(resultData.getString(OurLocation.RESPONSE_TYPE).equals(OurLocation.RESOLVEADDRESS)){
//				resolveAddressLatTV.setText(""+resultData.getString(OurLocation.LATITUDE, ""));
//				resolveAddressLngTV.setText(""+resultData.getString(OurLocation.LONGITUDE, ""));
				log("------- RESOLVE ADDRESS REPLY------");
				log("latitude: " + resultData.getString(OurLocation.LATITUDE, ""));
				log("longitude: " + resultData.getString(OurLocation.LONGITUDE, ""));
				// stop service
				Intent i = new Intent(getActivity(),LocationService.class);
				getActivity().stopService(i);
				//TODO: send stuff
			}
			
			// LOCATIONSERVICE RESOLVE COORDS REPLY
			if(resultData.getString(OurLocation.RESPONSE_TYPE).equals(OurLocation.RESOLVECOORDS)){
				log("------- RESOLVE COODS REPLY------");
				log("address: " + resultData.getString(OurLocation.ADDRESS, ""));
				// stop service
				Intent i = new Intent(getActivity(),LocationService.class);
				getActivity().stopService(i);
				//TODO: send stuff
			}
			
			// LOCATIONSERVICE GET WIFI ACCESS POINTS REPLY
			if(resultData.getString(OurLocation.RESPONSE_TYPE).equals(OurLocation.GETWIFIAPS)){
				log("------- GET WIFI AP REPLY------");
				log("name: " + resultData.getString(OurLocation.NAME));
				log("mac: " + resultData.getString(OurLocation.MAC));
				// sending access points name and mac to client
				WifiBTDevice dev = new WifiBTDevice(resultData.getString(OurLocation.NAME), resultData.getString(OurLocation.MAC));
				helper.sendMessageSendBundle(BundleMessage.getInstance().createBundle(BUNDLE_MESSAGE.WIFI_SEARCH_REQUEST, dev));
			}
			
			// WEATHERFORECASTSERVICE FORECAST REPLY
			if(resultData.getString(WeatherForecast.RESPONSE_TYPE).equals(WeatherForecast.WEATHERFORECAST)){
				log("reply received...");
				String s = resultData.getString("payload");
				JSONParser parser = new JSONParser();
				JSONObject object = null;
				de.uniulm.bagception.bundlemessageprotocol.entities.WeatherForecast forecast = null;
				try {
					object = (JSONObject) parser.parse(s);
					forecast = new de.uniulm.bagception.bundlemessageprotocol.entities.WeatherForecast(
						object.get("city").toString(), 
						Float.parseFloat(object.get("temp").toString()),
						Float.parseFloat(object.get("wind").toString()),
						Float.parseFloat(object.get("clouds").toString()),
						Float.parseFloat(object.get("temp_min").toString()),
						Float.parseFloat(object.get("temp_max").toString()),
						Float.parseFloat(object.get("rain").toString())
					);
					
					log("------- GET WEATHER FORECAST------");
					log(" city: " + forecast.getCity());
					log(" temp: " + forecast.getTemp());
					log(" wind: " + forecast.getWind()); 
					log(" clouds: " + forecast.getClouds());
					log(" tempMin: " + forecast.getTemp_min());
					log(" tempMax: " + forecast.getTemp_max()); 
					log(" rain: " + forecast.getRain());
					// sending weatherforecast to client
//				helper.sendMessageSendBundle(BundleMessage.getInstance().createBundle(BUNDLE_MESSAGE.WEATHERFORECAST_REPLY, forecast));
					
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
			}
			if(resultData.getString(Calendar.RESPONSE_TYPE).equals(Calendar.CALENDAR_NAMES)){
				log("------- GET CALENDAR NAMES ------");
				ArrayList<String> calendarNames = new ArrayList<String>();
				String s = resultData.getString("payload");
				if(resultData.containsKey(Calendar.CALENDAR_NAMES)){
					calendarNames = resultData.getStringArrayList("calendarNames");
					for(String st : calendarNames){
						log(st);
					}
				}
			}
			if(resultData.getString(Calendar.RESPONSE_TYPE).equals(Calendar.CALENDAR_EVENTS)){
				log("------- GET CALENDAR EVENTS ------");
					String s = resultData.getString("payload");
					if(resultData.containsKey(Calendar.CALENDAR_EVENTS)){
						ArrayList<String> calendarEvents = resultData.getStringArrayList("calendarEvents");
						JSONParser parser = new JSONParser();
						JSONObject obj = null;
						for(String st : calendarEvents){
							log(st);
							try {
								obj = (JSONObject) parser.parse(st);
								CalendarEvent event = CalendarEvent.fromJSON(obj);
								log(event.getName() + " " + event.getLocation());
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
					}
			}
			
		}
	}
	
	private void log(String s){
		Log.d("MCS", s);
	}
}
