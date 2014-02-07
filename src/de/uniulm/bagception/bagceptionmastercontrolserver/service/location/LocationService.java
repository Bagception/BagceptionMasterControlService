package de.uniulm.bagception.bagceptionmastercontrolserver.service.location;

import java.util.ArrayList;
import java.util.List;

import de.uniulm.bagception.bagceptionmastercontrolserver.database.DatabaseException;
import de.uniulm.bagception.bagceptionmastercontrolserver.database.DatabaseHelper;
import de.uniulm.bagception.bundlemessageprotocol.entities.Location;
import de.uniulm.bagception.services.attributes.OurLocation;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.util.Log;

public class LocationService extends Service{
	
	private ResultReceiver resultReceiver;
	private DatabaseHelper dbHelper;
	
	private LocationManager locationManager;
	private LocationListener locationListener;
	private BluetoothAdapter bluetoothAdapter;
	private BroadcastReceiver mReceiver;
	private boolean isBTRegistered = false;
	private boolean firstTime = false;
	private List<Location> knownLocations = null; 
	
	
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
		super.onDestroy();
		unregisterListeners();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		resultReceiver = intent.getParcelableExtra("receiverTag");
		log("request received!");
		
		
		
		String requestType = "";
		if(intent.hasExtra(OurLocation.REQUEST_TYPE))requestType = intent.getStringExtra(OurLocation.REQUEST_TYPE);
		
		
		if(requestType.equals(OurLocation.GETLOCATION)){
			storedLocations.clear();
			searchForWifiAccessPoints();
			
			// check if device supports bluetooth
//			if(bluetoothAdapter != null){
//				log("hasBT");
//				if(bluetoothAdapter.isEnabled()){
//					log("isEnabledBT");
//					searchForBluetoothDevices();
//				}
//			}

			// check if gps based location search is enabled
//			if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
//				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//				log("gps based enabled");
//			}
			
			// check if network and cell based location search is enabled
			log("kurz davor");
			if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
				log("network based enabled");
				locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
				// search for nearby wifi aps and check if their mac match with a location mac
				searchForWifiAccessPoints();
			}
			
			
		}
		
		
		if(requestType.equals(OurLocation.RESOLVE_ADDRESS)){
			
		}
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	
	
	/**
	 * searches for nearby wifi access points and checks if a detected mac (bssid) matches with locations mac
	 */
	public void searchForWifiAccessPoints(){
		log("searchForWifiAPs");
		try {
			WifiManager mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			if(mainWifi.isWifiEnabled()){
				knownLocations = dbHelper.getLocations();
				if(knownLocations == null) return;
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
				,i);
				mainWifi.startScan();
				
			}
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
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
					String action = intent.getAction();
					// When discovery finds a device
					if (BluetoothDevice.ACTION_FOUND.equals(action)) {
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
					}
				}
			};
			// Register the BroadcastReceiver
			IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
			registerReceiver(mReceiver, filter); // Don't forget to unregister
													// during onDestroy
			isBTRegistered = true;
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
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
		if(isBTRegistered){
			unregisterReceiver(mReceiver); 
			isBTRegistered=false;
		}
		locationManager.removeUpdates(locationListener);
	}
	
	private void log(String s){
		Log.d("LocationService", s);
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
