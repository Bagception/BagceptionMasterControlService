package de.uniulm.bagception.bagceptionmastercontrolserver.logic;


import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import de.uniulm.bagception.bagceptionmastercontrolserver.service.calendar.CalendarService;
import de.uniulm.bagception.bagceptionmastercontrolserver.service.location.LocationService;
import de.uniulm.bagception.bagceptionmastercontrolserver.service.weatherforecast.WeatherForecastService;
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

public class ServiceSystem implements Receiver{
	
	private MyResultReceiver mResultreceiver;
	private MasterControlServer mcs;

	
	public ServiceSystem(MasterControlServer mcs){
		this.mcs = mcs;
		mResultreceiver = new MyResultReceiver(new Handler());
		mResultreceiver.setReceiver(this);
	}

	
	public void locationRequest(){
		// GET CURRENT LOCATION
		Intent i = new Intent(mcs,LocationService.class);
		i.putExtra("receiverTag", mResultreceiver);
		i.putExtra(OurLocation.REQUEST_TYPE, OurLocation.GETLOCATION);
		mcs.startService(i);
	}
	
	public void resolveAddressRequest(String address){
		// RESOLVE ADDRESS TO COORDS
		Intent i = new Intent(mcs,LocationService.class);
		i.putExtra("receiverTag", mResultreceiver);
		i.putExtra(OurLocation.REQUEST_TYPE, OurLocation.RESOLVEADDRESS);
		i.putExtra(OurLocation.ADDRESS, address);
		mcs.startService(i);
	}
	
	public void resolveCoordsRequest(float lat, float lng){
		// RESOLVE COORDS TO ADDRESS
		Intent i = new Intent(mcs,LocationService.class);
		i.putExtra("receiverTag", mResultreceiver);
		i.putExtra(OurLocation.REQUEST_TYPE, OurLocation.RESOLVECOORDS);
		i.putExtra(OurLocation.LATITUDE, lat);
		i.putExtra(OurLocation.LONGITUDE, lng);
		mcs.startService(i);
	}
	
	public void wifiSearchRequest(){
		// GET WIFI ACCESS POINTS
		Intent i = new Intent(mcs,LocationService.class);
		i.putExtra("receiverTag", mResultreceiver);
		i.putExtra(OurLocation.REQUEST_TYPE, OurLocation.GETWIFIAPS);
		mcs.startService(i);
	}

	public void bluetoothSearchRequest(){
		// GET ALL BLUETOOTH DEVICES
		Intent i = new Intent(mcs,LocationService.class);
		i.putExtra("receiverTag", mResultreceiver);
		i.putExtra(OurLocation.REQUEST_TYPE, OurLocation.GETBLUETOOTHDEVICES);
		mcs.startService(i);
	}

	public void weatherForecastRequest(float lat, float lng){
		// WEATHER FORECAST
		Intent i = new Intent(mcs,WeatherForecastService.class);
		i.putExtra(WeatherForecast.LATITUDE, lat);
		i.putExtra(WeatherForecast.LONGITUDE, lng);
		i.putExtra("receiverTag", mResultreceiver);
		mcs.startService(i);
	}
	
	public void calendarNameRequest() {
		// CALENDAR NAME
		Intent i = new Intent(mcs, CalendarService.class);
		i.putExtra("receiverTag", mResultreceiver);
		i.putExtra(Calendar.REQUEST_TYPE, Calendar.CALENDAR_NAMES);
		mcs.startService(i);
	}
	
	public void calendarEventRequest() {
		Intent i = new Intent(mcs, CalendarService.class);
		i.putExtra("receiverTag", mResultreceiver);
		i.putExtra(Calendar.REQUEST_TYPE, Calendar.CALENDAR_EVENTS);
		mcs.startService(i);
	}
	
	private void log(String s){
		Log.d("MCS", s);
	}

	
	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		log("answer received!");
		
		
		if(resultData.containsKey(OurLocation.RESPONSE_TYPE)){
			
			// LOCATIONSERVICE LOCATION REPLY
			if(resultData.getString(OurLocation.RESPONSE_TYPE).equals(OurLocation.LOCATION)){
				log("------- LOCATION REPLY------");
				log("provider: " + resultData.getString(OurLocation.PROVIDER, ""));
				log("accuracy: " +resultData.getFloat(OurLocation.ACCURACY, 0));
				log("latitude: " +resultData.getDouble(OurLocation.LATITUDE, 0));
				log("longitude: " +resultData.getDouble(OurLocation.LONGITUDE, 0));
				// stop service
				Intent i = new Intent(mcs,LocationService.class);
				mcs.stopService(i);
				
				// sending current position to client
				Position currentPosition = new Position(resultData.getString(OurLocation.PROVIDER, ""),
														resultData.getFloat(OurLocation.LATITUDE, 0),
														resultData.getFloat(OurLocation.LONGITUDE, 0),
														resultData.getFloat(OurLocation.ACCURACY, 0));
				mcs.sendToRemote(BUNDLE_MESSAGE.LOCATION_REPLY, currentPosition);
			}
			
			// LOCATIONSERVICE RESOLVE ADDRESS REPLY
			if(resultData.getString(OurLocation.RESPONSE_TYPE).equals(OurLocation.RESOLVEADDRESS)){
				log("------- RESOLVE ADDRESS REPLY------");
				log("latitude: " + resultData.getString(OurLocation.LATITUDE, ""));
				log("longitude: " + resultData.getString(OurLocation.LONGITUDE, ""));
				// stop service
				Intent i = new Intent(mcs,LocationService.class);
				mcs.stopService(i);
				//TODO: send stuff
			}
			
			// LOCATIONSERVICE RESOLVE COORDS REPLY
			if(resultData.getString(OurLocation.RESPONSE_TYPE).equals(OurLocation.RESOLVECOORDS)){
				log("------- RESOLVE COODS REPLY------");
				log("address: " + resultData.getString(OurLocation.ADDRESS, ""));
				// stop service
				Intent i = new Intent(mcs,LocationService.class);
				mcs.stopService(i);
				//TODO: send stuff
			}
			
			// LOCATIONSERVICE GET WIFI ACCESS POINTS REPLY
			if(resultData.getString(OurLocation.RESPONSE_TYPE).equals(OurLocation.GETWIFIAPS)){
				log("------- GET WIFI AP REPLY------");
				log("name: " + resultData.getString(OurLocation.NAME));
				log("mac: " + resultData.getString(OurLocation.MAC));
				// sending access points name and mac to client
				WifiBTDevice dev = new WifiBTDevice(resultData.getString(OurLocation.NAME), resultData.getString(OurLocation.MAC));
				mcs.sendToRemote(BUNDLE_MESSAGE.WIFI_SEARCH_REPLY, dev);
			}
			
			if(resultData.getString(OurLocation.RESPONSE_TYPE).equals(OurLocation.GETBLUETOOTHDEVICES)){
				log("------- GET BT DEVICES REPLY------");
				log("name: " + resultData.getString(OurLocation.NAME));
				log("mac: " + resultData.getString(OurLocation.MAC));
				// sending access points name and mac to client
				WifiBTDevice dev = new WifiBTDevice(resultData.getString(OurLocation.NAME), resultData.getString(OurLocation.MAC));
//				helper.sendMessageSendBundle(BundleMessage.getInstance().createBundle(BUNDLE_MESSAGE.BLUETOOTH_SEARCH_REPLY, dev));
			}
			
			// WEATHERFORECASTSERVICE FORECAST REPLY
			if(resultData.getString(OurLocation.RESPONSE_TYPE).equals(WeatherForecast.WEATHERFORECAST)){
//				String s = resultData.getString("payload");
//				try {
//					JSONObject object = new JSONObject(s);
//					String city = object.getString("city");
//					float temp = Float.parseFloat(object.getString(WeatherForecast.TEMP));
//					float wind = Float.parseFloat(object.getString(WeatherForecast.WIND));
//					float clouds = Float.parseFloat(object.getString(WeatherForecast.CLOUDS));
//					float temp_min = Float.parseFloat(object.getString(WeatherForecast.TEMP_MIN));
//					float temp_max = Float.parseFloat(object.getString(WeatherForecast.TEMP_MAX));
//					float rain = Float.parseFloat(object.getString(WeatherForecast.RAIN));
//					// sending weatherforecast to client
//					de.uniulm.bagception.bundlemessageprotocol.entities.WeatherForecast forecast = new de.uniulm.bagception.bundlemessageprotocol.entities.WeatherForecast(city, temp, wind, clouds, temp_min, temp_max, rain);
//					helper.sendMessageSendBundle(BundleMessage.getInstance().createBundle(BUNDLE_MESSAGE.WEATHERFORECAST_REPLY, forecast));
//					log("------- GET WEATHER FORECAST------");
//					log(" city: " + forecast.getCity());
//					log(" temp: " + forecast.getTemp());
//					log(" wind: " + forecast.getWind()); 
//					log(" clouds: " + forecast.getClouds());
//					log(" tempMin: " + forecast.getTemp_min());
//					log(" tempMax: " + forecast.getTemp_max()); 
//					log(" rain: " + forecast.getRain());
//					//TODO: stop service
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
			}
			if(resultData.getString(Calendar.RESPONSE_TYPE).equals(Calendar.CALENDAR_NAMES)){
				log("------- GET CALENDAR NAMES ------");
				ArrayList<String> calendarNames = new ArrayList<String>();
				ArrayList<CalendarEvent> calendarEvents = new ArrayList<CalendarEvent>();
				String s = resultData.getString("payload");
				if(resultData.containsKey(Calendar.CALENDAR_NAMES)){
					calendarNames = resultData.getStringArrayList("calendarNames");
					for(String st : calendarNames){
						calendarEvents.add(new CalendarEvent("", st, "", "", -1, -1));
						log(st);
					}
				}
				//TODO: send to client...
				// kann ich eine arraylist mit calendarevent ssenden?
			}
			if(resultData.getString(Calendar.RESPONSE_TYPE).equals(Calendar.CALENDAR_EVENTS)){
				
			}
		}
	}


}
