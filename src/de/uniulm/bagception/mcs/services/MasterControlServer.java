package de.uniulm.bagception.mcs.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import de.philipphock.android.lib.logging.LOG;
import de.philipphock.android.lib.services.ServiceUtil;
import de.philipphock.android.lib.services.observation.ObservableService;
import de.uniulm.bagception.bagceptionmastercontrolserver.actor_reactor.CaseOpenBroadcastActor;
import de.uniulm.bagception.bagceptionmastercontrolserver.actor_reactor.CaseOpenServiceBroadcastReactor;
import de.uniulm.bagception.bagceptionmastercontrolserver.database.AdministrationDatabaseAdapter;
import de.uniulm.bagception.bagceptionmastercontrolserver.database.DatabaseException;
import de.uniulm.bagception.bagceptionmastercontrolserver.database.DatabaseHelper;
import de.uniulm.bagception.bagceptionmastercontrolserver.logic.ActivitySystem;
import de.uniulm.bagception.bagceptionmastercontrolserver.logic.ContextInterpreter;
import de.uniulm.bagception.bagceptionmastercontrolserver.logic.ItemIndexSystem;
import de.uniulm.bagception.bagceptionmastercontrolserver.logic.ServiceSystem;
import de.uniulm.bagception.bagceptionmastercontrolserver.service.location.LocationService;
import de.uniulm.bagception.bagceptionmastercontrolserver.ui.fragments.ServiceStatusFragment;
import de.uniulm.bagception.bagceptionmastercontrolserver.ui.log_fragment.LOGGER;
import de.uniulm.bagception.bagceptionmastercontrolserver.ui.service_status_fragment.ServiceInfo;
import de.uniulm.bagception.bagceptionmastercontrolserver.ui.service_status_fragment.ServiceInfo.STATUS;
import de.uniulm.bagception.bluetoothservermessengercommunication.messenger.MessengerHelper;
import de.uniulm.bagception.bluetoothservermessengercommunication.messenger.MessengerHelperCallback;
import de.uniulm.bagception.broadcastconstants.BagceptionBroadcastContants;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage.BUNDLE_MESSAGE;
import de.uniulm.bagception.bundlemessageprotocol.entities.Activity;
import de.uniulm.bagception.bundlemessageprotocol.entities.ActivityPriorityList;
import de.uniulm.bagception.bundlemessageprotocol.entities.CalendarEvent;
import de.uniulm.bagception.bundlemessageprotocol.entities.ContainerStateUpdate;
import de.uniulm.bagception.bundlemessageprotocol.entities.ContextSuggestion;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.bundlemessageprotocol.entities.Location;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.AdministrationCommand;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.AdministrationCommandProcessor;
import de.uniulm.bagception.bundlemessageprotocol.serializer.PictureSerializer;
import de.uniulm.bagception.intentservicecommunication.MyResultReceiver;
import de.uniulm.bagception.intentservicecommunication.MyResultReceiver.Receiver;
import de.uniulm.bagception.protocol.bundle.constants.StatusCode;
import de.uniulm.bagception.services.ServiceNames;
import de.uniulm.bagception.services.attributes.OurLocation;

public class MasterControlServer extends ObservableService implements Runnable,
		MessengerHelperCallback, CaseOpenServiceBroadcastReactor, Receiver {

	/**
	 * Handles the control flow of the container
	 * 
	 * @author phil
	 * 
	 */

	public static MasterControlServer debuginstance;
	public MasterControlServer() {
		debuginstance = this;
	}
	
	private Thread mainThread;
	private AdministrationDatabaseAdapter adminDBAdapter;
	private DatabaseHelper dbHelper;
	private MyResultReceiver resultReceiver;
	private Location loc;
	private Activity tmp;
	
	// bt
	private MessengerHelper btHelper;

	private CaseOpenBroadcastActor caseActor;
	private volatile boolean isScanning = false;

	private ItemIndexSystem itemIndexSystem;
	public ItemIndexSystem getItemIndexSystem(){
		return itemIndexSystem;
	}
	
	private ActivitySystem activitySystem;
	public ActivitySystem getActivitySystem(){
		return activitySystem;
	}
	private ServiceSystem serviceSystem;
	
	private ContextInterpreter contextInterpreter;
	public ContextInterpreter getContextInterpreter(){
		return contextInterpreter;
	}
	private int batteryStatus = 0;

	public DatabaseHelper getDB(){
		return dbHelper;
	}
	

	@Override
	public void onCreate() {
		debuginstance = this;
		super.onCreate();
	}

	@Override
	protected void onFirstInit() {
		dbHelper = new DatabaseHelper(this);

		adminDBAdapter = new AdministrationDatabaseAdapter(this, dbHelper);
		mainThread = new Thread(this);
		mainThread.setDaemon(true);
		mainThread.start();

		caseActor = new CaseOpenBroadcastActor(this);
		caseActor.register(this);
		
		resultReceiver = new MyResultReceiver(new Handler());
		resultReceiver.setReceiver(contextInterpreter);

		contextInterpreter=new ContextInterpreter(this);
		
		IntentFilter f = new IntentFilter(
				BagceptionBroadcastContants.BROADCAST_RFID_FINISHED);
		f.addAction(BagceptionBroadcastContants.BROADCAST_RFID_TAG_FOUND);
		f.addAction(BagceptionBroadcastContants.BROADCAST_RFID_START_SCANNING);
		f.addAction(BagceptionBroadcastContants.BROADCAST_RFID_NOTCONNECTED);
		registerReceiver(RFIDReceiver, f);

		registerReceiver(this.mBatteryInfoReceiver, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));

		itemIndexSystem = new ItemIndexSystem(dbHelper);
		try {
			activitySystem = new ActivitySystem(this);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		serviceSystem = new ServiceSystem(this);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (btHelper!=null)
			btHelper.unregister(this);
		
		if (caseActor!=null)
			caseActor.unregister(this);
		unregisterReceiver(RFIDReceiver);
		unregisterReceiver(mBatteryInfoReceiver);

		// stop services
		Intent i = new Intent();
		for (ServiceInfo serviceInfo : ServiceStatusFragment.bagceptionSystemServices) {
			i.setAction(serviceInfo.getServiceSystemName());
			stopService(i);
			LOGGER.C(this, "stopping service " + serviceInfo.getName());
		}

		LOGGER.C(this, "MCS stopped");
	}

	/**
	 * starts all system components
	 */
	private void bootstrap() {
		ServiceUtil.logRunningServices(this, "###");
		// first start all necessary services
		LOGGER.C(this,
				"initiating bootstrap method, starting necessary services");
		Intent i = new Intent();
		
		for (ServiceInfo serviceInfo : ServiceStatusFragment.bagceptionSystemServices) {
			if (!ServiceUtil.isServiceRunning(this,
					serviceInfo.getServiceSystemName())) {
				LOGGER.C(this, "Service " + serviceInfo.getName()
						+ " is offline.. starting it now");
				i.setAction(serviceInfo.getServiceSystemName());
				startService(i);
			} else {
				LOGGER.C(this, "Service " + serviceInfo.getName()
						+ " already online.. ");
			}
		}

		// then check if all are started
		delayHandler.postDelayed(new Runnable() {
 
			@Override
			public void run() {
				// check if all services are running
				int runcount = 0;
				LOGGER.C(this,
						"All services should be activated now.. I will check that:");
				for (ServiceInfo serviceInfo : ServiceStatusFragment.bagceptionSystemServices) {
					if (!ServiceUtil.isServiceRunning(MasterControlServer.this,
							serviceInfo.getServiceSystemName())) {
						LOGGER.C(
								this,
								"Service "
										+ serviceInfo.getName()
										+ " is still offline.. I think it is not installed");
						serviceInfo.setStatus(STATUS.NOT_INSTALLED);
					} else {
						runcount++;

					}
				}
				if (runcount == ServiceStatusFragment.bagceptionSystemServices
						.size()) {
					LOGGER.C(this, "All Services online :)");
					// all services online:
					btHelper = new MessengerHelper(MasterControlServer.this,
							ServiceNames.BLUETOOTH_SERVER_SERVICE);
					btHelper.register(MasterControlServer.this);

				}
			}
		}, 100);
	}

	private final Handler delayHandler = new Handler();

	// private void serviceDiagnose(){
	//
	// }

	@Override
	public void run() {
		LOGGER.C(this, "MCS started");
		bootstrap();
		// stopSelf();
	}

	// \\ MessengerHelperCallback //
	@SuppressWarnings("unchecked")
	@Override
	public void onBundleMessage(Bundle b) {
		// this comes from the btclient
		LOGGER.C(this, "bundleMessage recv");

		switch (BundleMessage.getInstance().getBundleMessageType(b)) {
		case IMAGE_REQUEST:{
			JSONObject o = BundleMessage.getInstance().extractObject(b);
			String imageID = o.get("img").toString();
			long  imageIDInt = Integer.parseInt(imageID);
			
			LOGGER.C(this, " img request for id " + imageID);
			Bitmap bmp;
			try {

				if (dbHelper.getImageString(imageIDInt) == null) {
				} else {
					bmp = Item.deserialize(dbHelper.getImageString(imageIDInt));
					if (bmp == null){
						LOGGER.C(this, " no db entry for image " + imageID);
					}else{
						LOGGER.C(this, " image found: " + imageID);
						String serializedImage = PictureSerializer.serialize(bmp);
						JSONObject obj = new JSONObject();
						obj.put("img", serializedImage);
						obj.put("id", imageID);
						btHelper.sendMessageBundle(BundleMessage.getInstance()
								.createBundle(BUNDLE_MESSAGE.IMAGE_REPLY, obj));
					}

				}

			} catch (DatabaseException e) {
				e.printStackTrace();
			}

			break;
		}
		case CONTAINER_STATUS_UPDATE_REQUEST:{
			LOGGER.C(this, "CONTAINER_STATUS_UPDATE_REQUEST");
				try {
					setStatusChanged();
				} catch (DatabaseException e) {
					e.printStackTrace();
				}
			break;
		}

		case ADMINISTRATION_COMMAND: {
			JSONObject json = BundleMessage.getInstance().extractObject(b);
			AdministrationCommand<?> a_cmd = AdministrationCommand
					.fromJSONObject(json);
			a_cmd.accept(adminDBAdapter);
			LOGGER.C(this, "admin command " + a_cmd.getEntity().name() + ", "
					+ a_cmd.getOperation().name());
			
			a_cmd.accept(activityProcessor);
			
			break;
			
		}
		case LOCATION_REQUEST:{
			LOGGER.C(this, "LOCATION_REQUEST");
			serviceSystem.locationRequest();
			break;
		}
		
		case RESOLVE_ADDRESS_REQUEST:{
			LOGGER.C(this, "RESOLVE_ADDRESS_REQUEST");
			JSONObject o = BundleMessage.getInstance().extractObject(b);
			Location addressLocation = Location.fromJSON(o);
			// HACK: address is stored in name attribute of the location object ;)
			serviceSystem.resolveAddressRequest(addressLocation.getName());
			break;
		}
		
		case RESOLVE_COORDS_REQUEST:{
			LOGGER.C(this, "RESOLVE_COORDS_REQUEST");

			JSONObject o = BundleMessage.getInstance().extractObject(b);
			Location coordsLocation = Location.fromJSON(o);
			serviceSystem.resolveCoordsRequest(coordsLocation.getLat(), coordsLocation.getLng());
			break;
		}
		
		case WIFI_SEARCH_REQUEST:{
			LOGGER.C(this, "WIFI_SEARCH_REQUEST");
			serviceSystem.wifiSearchRequest();
			break;
		}
			
		case BLUETOOTH_SEARCH_REQUEST:{
			LOGGER.C(this, "BLUETOOTH_SEARCH_REQUEST");
			serviceSystem.bluetoothSearchRequest();
			break;
		}
			
		case WEATHERFORECAST_REQUEST:{
			LOGGER.C(this, "WEATHERFORECAST_REQUEST");
			JSONObject o = BundleMessage.getInstance().extractObject(b);
			float lat = Float.parseFloat(o.get(OurLocation.LATITUDE).toString());
			float lng = Float.parseFloat(o.get(OurLocation.LONGITUDE).toString());
			serviceSystem.weatherForecastRequest(lat, lng);
			break;
		}
		case CALENDAR_NAME_REQUEST:{
			LOGGER.C(this, "CALENDAR_NAME_REQUEST");
			serviceSystem.calendarNameRequest();
			break;
		}
		case CALENDAR_EVENT_REQUEST:{
				try {
					List<Activity> activityList = dbHelper.getActivities();
					LOGGER.C(this, "CALENDAR_NAME_REQUEST");
					serviceSystem.calendarEventRequest(activityList);
					break;
					
				} catch (DatabaseException e) {
					e.printStackTrace();
				}
			break;
		}
		case CALENDAR_ADD_EVENT_REQUEST:{
			LOGGER.C(this, "CALENDAR_ADD_EVENT_REQUEST");
			JSONObject o = BundleMessage.getInstance().extractObject(b);
			CalendarEvent event = CalendarEvent.fromJSON(o);
			serviceSystem.calendarAddEventRequest(event);
			break;
		}
		
		case CALENDAR_REMOVE_EVENT_REQUEST:{
			LOGGER.C(this, "CALENDAR_REMOVE_EVENT_REQUEST");
			JSONObject o = BundleMessage.getInstance().extractObject(b);
			CalendarEvent event = CalendarEvent.fromJSON(o);
			serviceSystem.calendarRemoveRequest(event);
			break;
		}
		
		default:
			break;
		}

	}

	@Override
	public void onResponseMessage(Bundle b) {
		// nothing to do here

	}

	@Override
	public void onStatusMessage(Bundle b) {
		// nothing to do here
		StatusCode status = StatusCode.getStatusCode(b);
		switch (status) {
		case DISCONNECTED:
			break;
		default:
			break;
		}

	}

	@Override
	public void onCommandMessage(Bundle b) {
		// nothing to do here

	}

	@Override
	public void onError(Exception e) {
		// we assume this is not going to happen
	}

	@Override
	public void connectedWithRemoteService() {
		// we assume this works correctly

	}

	@Override
	public void disconnectedFromRemoteService() {
		// we assume this works correctly

	}

	// MessengerHelperCallback \\

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	// \\CaseOpenServiceBroadcastReactor//
	@Override
	public void caseOpened() {
		synchronized (this) {
			if (isScanning)
				return;
			Intent rfidScanBC = new Intent(
					BagceptionBroadcastContants.BROADCAST_RFID_STARTSCAN);
			sendBroadcast(rfidScanBC);
		}

	}

	@Override
	public void caseClosed() {
		Intent rfidScanBC = new Intent(
				BagceptionBroadcastContants.BROADCAST_RFID_STOPSCAN);
		sendBroadcast(rfidScanBC);
		LOG.out(this, "case closed");
		LOGGER.C(this, "case closed");
		itemIndexSystem.caseClosed();
	}

	// CaseOpenServiceBroadcastReactor\\

	private final ToneGenerator toneGenerator = new ToneGenerator(
			AudioManager.STREAM_MUSIC, 100);
	private final BroadcastReceiver RFIDReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			if (BagceptionBroadcastContants.BROADCAST_RFID_FINISHED
					.equals(intent.getAction())) {
				// scan finished

				isScanning = false;
				Toast.makeText(MasterControlServer.this, "scanning finished",
						Toast.LENGTH_SHORT).show();
				LOGGER.C(this, "stop rfid scan");
			} else if (BagceptionBroadcastContants.BROADCAST_RFID_TAG_FOUND
					.equals(intent.getAction())) {
				// tag found
				
				toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP);

				String id = intent
						.getStringExtra(BagceptionBroadcastContants.BROADCAST_RFID_TAG_FOUND);
				LOGGER.C(this, "Tag scanned: " + id);
				Item i = null;
				try {
					long item_id = dbHelper.getItemId(id);
					
					if (item_id != -1) {
						i = dbHelper.getItem(item_id);
					}
					
					if (i != null) {
						

						itemScanned(i,id);
						
					} else {
						// tag not found in db
						ArrayList<String> ids = new ArrayList<String>();
						ids.add(id);
						btHelper.sendMessageBundle(BundleMessage.getInstance()
								.toItemFoundNotBundle(new Item("", ids)));
					}
				} catch (DatabaseException e) {
					e.printStackTrace();
				}

			} else if (BagceptionBroadcastContants.BROADCAST_RFID_START_SCANNING
					.equals(intent.getAction())) {
				// rfid start scanning

				Toast.makeText(MasterControlServer.this, "start scanning",
						Toast.LENGTH_SHORT).show();
				LOGGER.C(this, "start rfid scan");
			} else if (BagceptionBroadcastContants.BROADCAST_RFID_NOTCONNECTED
					.equals(intent.getAction())) {
				// rfid not connected

				Toast.makeText(MasterControlServer.this,
						"RFID scanner not connected", Toast.LENGTH_SHORT)
						.show();
				LOGGER.C(this, "RFID scanner not connected");
			}

		}
	};
	
	
	public void itemScanned(Item i,String id) throws DatabaseException{
		// tag exists in database
		LOGGER.C(this, "TAG found: " + i.getName()+", hash: "+i.getImageHash());
		if (itemIndexSystem.itemScanned(i,id)) {
			// item put in
			LOGGER.C(this, "Item in: " + i.getName());
		} else {
			// item taken out
			LOGGER.C(this, "Item out: " + i.getName());
		}
		sendToRemote(BUNDLE_MESSAGE.ITEM_FOUND, null);
		List<Item> items = itemIndexSystem.getCurrentItems();
		
//		if (activitySystem.isManuallyDetermActivity()){
//			setStatusChanged();
//			Log.d("ACT"," not in recognition");
//			//return;
//		}
		Log.d("ACT","in recognition");
		ActivityPriorityList activityPriorityList = activitySystem.activityRecognition(items);
		
		Activity first = null;
		
		if(activityPriorityList != null && activityPriorityList.getActivities().size() > 0){
			first = activityPriorityList.getActivities().get(0);
		}
		
		if (first!=null){
			if (!activitySystem.isManuallyDetermActivity())
				activitySystem.setCurrentActivity(first);
			tmp = first;
		}
		
		sendToRemote(BUNDLE_MESSAGE.ACTIVITY_PRIORITY_LIST, activityPriorityList);
		setStatusChanged();

	}

	private synchronized void setStatusChanged(ContainerStateUpdate s){
		Set<Item> toCheck = new HashSet<Item>(itemIndexSystem.getCurrentItems());
		toCheck.addAll(s.getMissingItems());
		if (s.getActivity() != null){
			if (s.getActivity().getItemsForActivity() != null){
				toCheck.addAll(s.getActivity().getItemsForActivity());
			}
		}
		
		try {
			contextInterpreter.updateList(new ArrayList<Item>(toCheck),true);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}

		
		s = new ContainerStateUpdate(s.getActivity(), s.getItemList(), contextInterpreter.getContextSuggetions(), s.getBatteryState());
		Bundle toSend = BundleMessage.getInstance().createBundle(
				BundleMessage.BUNDLE_MESSAGE.CONTAINER_STATUS_UPDATE,
				s.toString());
		
		
		btHelper.sendMessageBundle(toSend);
		LOGGER.C(this, "status update send");
	}
	
	
	public void setStatusChanged() throws DatabaseException {

		ContainerStateUpdate statusUpdate = new ContainerStateUpdate(
				activitySystem.getCurrentActivity(),
				itemIndexSystem.getCurrentItems(),
				null,
				batteryStatus);
				
		setStatusChanged(statusUpdate);
		
	}

	public void sendToRemote(BUNDLE_MESSAGE msg, Object toSendObj) {
		Bundle toSendBun = BundleMessage.getInstance().createBundle(msg,
				toSendObj);
		btHelper.sendMessageBundle(toSendBun);
	}
	@SuppressWarnings("unchecked")
	public void sendMessageToRemote(String message) {
		JSONObject obj = new JSONObject();
		obj.put("msg", message);
		sendToRemote(BUNDLE_MESSAGE.STRING_MESSAGE,obj);
	}

	public static void DEBUG() throws DatabaseException {
		debuginstance.setStatusChanged();
	}


	//battery
	private BroadcastReceiver mBatteryInfoReceiver = new BroadcastReceiver(){
	    @Override
	    public void onReceive(Context ctxt, Intent intent) {
	    	batteryStatus = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
	    }
	  };

	  
	private final AdministrationCommandProcessor activityProcessor = new AdministrationCommandProcessor(){
		
		public void onActivityStop(Activity a, de.uniulm.bagception.bundlemessageprotocol.entities.administration.AdministrationCommand<Activity> cmd) {
			try {
				activitySystem.setCurrentActivity(Activity.NO_ACTIVITY);
				activitySystem.setManuallyDetermActivity(false);
			} catch (DatabaseException e) {
				e.printStackTrace();
			}
			try {
				setStatusChanged();
			} catch (DatabaseException e) {
				e.printStackTrace();
			}

		};
		public void onActivityStart(Activity a, AdministrationCommand<Activity> cmd) {
			try {
				
				activitySystem.setCurrentActivity(a);
				activitySystem.setManuallyDetermActivity(true);
								
			} catch (DatabaseException e) {
				e.printStackTrace();
			}
			try {
				setStatusChanged();
			} catch (DatabaseException e) {
				e.printStackTrace();
			}
		}
	};
		
		
		@Override
		public void onReceiveResult(int resultCode, Bundle resultData) {
			
			if(resultData.getString(OurLocation.RESPONSE_TYPE).equals(OurLocation.LOCATION)){

				// stop service
				Intent i = new Intent(this, LocationService.class);
				stopService(i);
				
				// sending current position to client
				loc = new Location("", resultData.getFloat(OurLocation.LATITUDE, 0), resultData.getFloat(OurLocation.LONGITUDE, 0), 0);
			}
		}
		
		
}
