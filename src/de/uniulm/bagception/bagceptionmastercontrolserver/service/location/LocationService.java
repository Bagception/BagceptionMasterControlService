package de.uniulm.bagception.bagceptionmastercontrolserver.service.location;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.util.Log;
import de.uniulm.bagception.bagceptionmastercontrolserver.database.DatabaseException;
import de.uniulm.bagception.bagceptionmastercontrolserver.database.DatabaseHelper;
import de.uniulm.bagception.bundlemessageprotocol.entities.Location;
import de.uniulm.bagception.services.attributes.OurLocation;

public class LocationService extends Service{
	
	private ResultReceiver resultReceiver;
	private DatabaseHelper dbHelper;
	
	private LocationManager locationManager;
	private LocationListener locationListener;
	private BluetoothAdapter bluetoothAdapter;
	private BroadcastReceiver mReceiver;
	private boolean firstTime = false;
	private List<Location> knownLocations = null; 
	private HashMap<String, String> wifiBTDevices;
	
	
	private ArrayList<android.location.Location> storedLocations;
	private float LOCATION_ACCURACY_THRESHOLD = 100f; // accuracy in meters

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		dbHelper = new DatabaseHelper(getBaseContext());
		
		storedLocations = new ArrayList<android.location.Location>();
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
		
		
		locationListener = new LocationListener() {
			public void onLocationChanged(android.location.Location location) {
				log("new location arrived");
				sendBestPositionFromLocations(location);
			}

			@Override
			public void onProviderDisabled(String provider) {}
			@Override
			public void onProviderEnabled(String provider) {}
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {}
		};
	}
	
	
	
	@Override
	public void onDestroy() {
		unregisterListeners();
		super.onDestroy();
	}
	
	public void onStop(){
		
	}
	
	

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		resultReceiver = intent.getParcelableExtra("receiverTag");
		log("request received!");
		
		String requestType = "";
		if(intent.hasExtra(OurLocation.REQUEST_TYPE))requestType = intent.getStringExtra(OurLocation.REQUEST_TYPE);
		
		if(requestType.equals(OurLocation.GETLOCATION)){
			storedLocations.clear();
//			searchForWifiAccessPoints();
			
			// check if device supports bluetooth
			if(bluetoothAdapter != null){
				log("hasBT");
				if(bluetoothAdapter.isEnabled()){
					log("isEnabledBT");
					searchForBluetoothDevices();
				}
			}

			// check if gps based location search is enabled
			if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
				log("gps based enabled");
			}
			
			// check if network and cell based location search is enabled
			log("kurz davor");
			if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
				log("network based enabled");
				locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
				// search for nearby wifi aps and check if their mac match with a location mac
				searchForWifiAccessPoints();
			}
		}
		
		// RESOLVE ADDRESS TO COORDS
		if(requestType.equals(OurLocation.RESOLVEADDRESS)){
			log("resolve address...");
			String address = intent.getStringExtra(OurLocation.ADDRESS);
			log("address to resolve: " + address);
			
			Geocoder coder = new Geocoder(this);
			List<Address> addresses;
			
			try {
				addresses = coder.getFromLocationName(address, 1);
				if(addresses != null){
					log("coder resolved address");
					Address foundAddress = addresses.get(0);
					Bundle b = new Bundle();
					b.putString(OurLocation.RESPONSE_TYPE, OurLocation.RESOLVEADDRESS);
					b.putString(OurLocation.LONGITUDE, ""+foundAddress.getLongitude());
					b.putString(OurLocation.LATITUDE, ""+foundAddress.getLatitude());
					resultReceiver.send(0, b);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			stopSelf();
		}
		
		
		if(requestType.equals(OurLocation.RESOLVECOORDS)){
			log("resolvecoords!!!");
			float lat = intent.getFloatExtra(OurLocation.LATITUDE, 0);
			float lng = intent.getFloatExtra(OurLocation.LONGITUDE, 0);
			String address = "";
			String city = "";
			String country = "";
			Geocoder geocoder;
			List<Address> addresses;
			geocoder = new Geocoder(this, Locale.getDefault());
			try {
				addresses = geocoder.getFromLocation(lat, lng, 1);
				address = addresses.get(0).getAddressLine(0);
				city = addresses.get(0).getAddressLine(1);
				country = addresses.get(0).getAddressLine(2);
				Bundle b = new Bundle();
				b.putString(OurLocation.RESPONSE_TYPE, OurLocation.RESOLVECOORDS);
				b.putString(OurLocation.ADDRESS, address + ", " + city + ", " + country);
				resultReceiver.send(0, b);
				
//				log("address: " + address + " city: " + city + " country: " + country);
			} catch (IOException e) {
				log("address not found");
				Bundle b = new Bundle();
				b.putString(OurLocation.RESPONSE_TYPE, OurLocation.RESOLVECOORDS);
				b.putString(OurLocation.ADDRESS, "address not found");
				resultReceiver.send(0, b);
				e.printStackTrace();
			}
			stopSelf();

		}
		
		if(requestType.equals(OurLocation.GETWIFIAPS)){
			log("getWIFIAPs");
				WifiManager mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
				wifiBTDevices = new HashMap<String, String>();
				if(mainWifi.isWifiEnabled()){
					// get all known locations from db
					IntentFilter i = new IntentFilter();
					i.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
					registerReceiver(new BroadcastReceiver(){
						@Override
						public void onReceive(Context context, Intent intent) {
							WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
							mWifiManager.getScanResults();
							List<ScanResult> scanResults = mWifiManager.getScanResults();
							for(ScanResult sr : scanResults){
//								log(sr.SSID + " " + sr.BSSID);
								if(!wifiBTDevices.containsKey(sr.BSSID)){
									wifiBTDevices.put(sr.BSSID, sr.SSID);
									Bundle b = new Bundle();
									b.putString(OurLocation.RESPONSE_TYPE, OurLocation.GETWIFIAPS);
									b.putString(OurLocation.NAME, sr.SSID);
									b.putString(OurLocation.MAC, sr.BSSID);
									resultReceiver.send(0, b);
								}
							}
						}
					}
					,i);
					mainWifi.startScan();
				}
		}
		
		
		if(requestType.equals(OurLocation.GETBLUETOOTHDEVICES)){
			log("###### requestType correct! searching for bt devices");
			wifiBTDevices = new HashMap<String, String>();
			mReceiver = new BroadcastReceiver() {
				public void onReceive(Context context, Intent intent) {
					String action = intent.getAction();
					// When discovery finds a device
					if (BluetoothDevice.ACTION_FOUND.equals(action)) {
						
						log("###### device found!");
						
						// Get the BluetoothDevice object from the Intent
						BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
						if(!wifiBTDevices.containsKey(device.getAddress())){
							wifiBTDevices.put(device.getAddress(), device.getName());
							Bundle b = new Bundle();
							b.putString(OurLocation.RESPONSE_TYPE, OurLocation.GETBLUETOOTHDEVICES);
							b.putString(OurLocation.NAME, device.getName());
							b.putString(OurLocation.MAC, device.getAddress());
							resultReceiver.send(0, b);
							log("###### device send!");

						}
					}
				}
			};
			// Register the BroadcastReceiver
			IntentFilter registerFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
			registerReceiver(mReceiver, registerFilter); 
			log("...");
		}
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	
	
	/**
	 * searches for nearby wifi access points and checks if a detected mac (bssid) matches with locations mac
	 */
	public void searchForWifiAccessPoints(){
		log("searchForWifiAPs");
		wifiBTDevices = new HashMap<String, String>();
		try {
			WifiManager mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			if(mainWifi.isWifiEnabled()){
				// get all known locations from db
				knownLocations = dbHelper.getLocations();
				if(knownLocations == null) return;
				IntentFilter i = new IntentFilter();
				i.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
				
				registerReceiver(new BroadcastReceiver(){
					@Override
					public void onReceive(Context context, Intent intent) {
						WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
						mWifiManager.getScanResults();
						List<ScanResult> scanResults = mWifiManager.getScanResults();
						for(ScanResult sr : scanResults){
							if(!wifiBTDevices.containsKey(sr.BSSID)){
								wifiBTDevices.put(sr.BSSID, sr.SSID);
								log(sr.SSID + " " + sr.BSSID);
								Location loc = isMACKnown(sr.BSSID, knownLocations);
								if(loc != null){
									android.location.Location location = new android.location.Location("WIFI");
									location.setAccuracy(1);
									location.setLatitude(loc.getLat());
									location.setLongitude(loc.getLng());
									sendBestPositionFromLocations(location);
								}
							}
						}
					}
				}
				,i);
				mainWifi.startScan();
				
			}
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
	}
	
	public void searchForBluetoothDevices() {
		log("searchFobrBTDevices");
		// get all known locations from db
		try {
			knownLocations = dbHelper.getLocations();
			if(knownLocations == null) return;
			// Create a BroadcastReceiver for ACTION_FOUND
			mReceiver = new BroadcastReceiver() {
				
				public void onReceive(Context context, Intent intent) {
					log("started...");
					String action = intent.getAction();
					// When discovery finds a device
					if (BluetoothDevice.ACTION_FOUND.equals(action)) {
						log("device found...");
						// Get the BluetoothDevice object from the Intent
						BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
						
						Location loc = isMACKnown(device.getAddress(), knownLocations);
						if(loc != null){
	                		android.location.Location location = new android.location.Location("BLUETOOTH");
	                		location.setAccuracy(1);
	                		location.setLatitude(loc.getLat());
	                		location.setLongitude(loc.getLng());
	                		sendBestPositionFromLocations(location);
	                	}
					}else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
						log("discovery finished...");
						unregisterReceiver(mReceiver);
					}
				}
			};
			// Register the BroadcastReceiver
			IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
			registerReceiver(mReceiver, filter); // Don't forget to unregister
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * Sends the location with the best accuracy back to the ResultReceiver.
	 * <br>The accuracy must be bether (lower) than LOCATION_ACCURACY_THRESHOLD.
	 * <br>Service will wait for a new location from LocationManager if accuracy > LOCATION_ACCURACY_THRESHOLD.
	 * @param location current location
	 */
	private void sendBestPositionFromLocations(android.location.Location location) {
		storedLocations.add(location);
		android.location.Location bestLocation = storedLocations.get(0);
		for(android.location.Location l : storedLocations){
			// search for the location with the best accuracy
			if(l.getAccuracy() >= bestLocation.getAccuracy()){
				bestLocation = l;
			}
		}
		
		log("bestLoc acc: " + bestLocation.getAccuracy() + " type: " + bestLocation.getProvider());
		if(bestLocation.getAccuracy() < LOCATION_ACCURACY_THRESHOLD){
			storedLocations.clear();
			unregisterListeners();
			double latitude = bestLocation.getLatitude();
			double longitude = bestLocation.getLongitude();
			float accuracy = bestLocation.getAccuracy();
			String provider = bestLocation.getProvider();
			log("prv: " + provider + " acc: " + accuracy + " lat: " + latitude + " lng: " + longitude);
			
			// sending answer
			if(!firstTime){
				Bundle b = new Bundle();
				b.putString(OurLocation.RESPONSE_TYPE, OurLocation.LOCATION);
				b.putFloat(OurLocation.ACCURACY, accuracy);
				b.putDouble(OurLocation.LONGITUDE, longitude);
				b.putDouble(OurLocation.LATITUDE, latitude);
				b.putString(OurLocation.PROVIDER, provider);
				resultReceiver.send(0, b);
				log("sending answer...");
				firstTime = true;
			}
		}
	}
	
	private void unregisterListeners(){
		// unregisterBluetooth
		locationManager.removeUpdates(locationListener);
	}
	
	private void log(String s){
//		Log.d("LocationService", s);
	}
	
	private Location isMACKnown(String mac, List<Location> knownLocations){
		for(Location l : knownLocations){
			if(l.getMac().equals(mac)){
				return l;
			}
		}
		return null;
	}
}
