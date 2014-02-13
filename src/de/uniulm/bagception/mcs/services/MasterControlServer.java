package de.uniulm.bagception.mcs.services;

import java.util.ArrayList;
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
import de.uniulm.bagception.bagceptionmastercontrolserver.logic.ItemIndexSystem;
import de.uniulm.bagception.bagceptionmastercontrolserver.logic.ServiceSystem;
import de.uniulm.bagception.bagceptionmastercontrolserver.ui.fragments.ServiceStatusFragment;
import de.uniulm.bagception.bagceptionmastercontrolserver.ui.log_fragment.LOGGER;
import de.uniulm.bagception.bagceptionmastercontrolserver.ui.service_status_fragment.ServiceInfo;
import de.uniulm.bagception.bagceptionmastercontrolserver.ui.service_status_fragment.ServiceInfo.STATUS;
import de.uniulm.bagception.bluetoothservermessengercommunication.messenger.MessengerHelper;
import de.uniulm.bagception.bluetoothservermessengercommunication.messenger.MessengerHelperCallback;
import de.uniulm.bagception.broadcastconstants.BagceptionBroadcastContants;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage.BUNDLE_MESSAGE;
import de.uniulm.bagception.bundlemessageprotocol.entities.ContainerStateUpdate;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.bundlemessageprotocol.entities.WeatherForecast;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.AdministrationCommand;
import de.uniulm.bagception.bundlemessageprotocol.serializer.PictureSerializer;
import de.uniulm.bagception.protocol.bundle.constants.StatusCode;
import de.uniulm.bagception.services.ServiceNames;
import de.uniulm.bagception.services.attributes.OurLocation;

public class MasterControlServer extends ObservableService implements Runnable,
		MessengerHelperCallback, CaseOpenServiceBroadcastReactor {

	/**
	 * Handles the control flow of the container
	 * 
	 * @author phil
	 * 
	 */

	private static MasterControlServer debuginstance;

	private Thread mainThread;
	private AdministrationDatabaseAdapter adminDBAdapter;
	private DatabaseHelper dbHelper;

	// bt
	private MessengerHelper btHelper;

	private CaseOpenBroadcastActor caseActor;
	private volatile boolean isScanning = false;

	private ItemIndexSystem itemIndexSystem;
	private ActivitySystem activitySystem;
	private ServiceSystem serviceSystem;

	private int batteryStatus = 0;

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
			activitySystem = new ActivitySystem();
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		serviceSystem = new ServiceSystem(this);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		btHelper.unregister(this);
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
//			Log.w("TEST", "IMAGEHASH BEI DER ABFRAGE: " + imageIDInt);
			
			LOGGER.C(this, " img request for id " + imageID);
			Bitmap bmp;
			try {

				if (dbHelper.getImageString(imageIDInt) == null) {
					Log.w("TEST", "ImageString is null");
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
			setStatusChanged();
			break;
		}

		case ADMINISTRATION_COMMAND: {
			JSONObject json = BundleMessage.getInstance().extractObject(b);
			AdministrationCommand<?> a_cmd = AdministrationCommand
					.fromJSONObject(json);
			a_cmd.accept(adminDBAdapter);
			LOGGER.C(this, "admin command " + a_cmd.getEntity().name() + ", "
					+ a_cmd.getOperation().name());
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
			String address = o.get(OurLocation.ADDRESS).toString();
			serviceSystem.resolveAddressRequest(address);
			break;
		}
		
		case RESOLVE_COORDS_REQUEST:{
			LOGGER.C(this, "RESOLVE_COORDS_REQUEST");
			JSONObject o = BundleMessage.getInstance().extractObject(b);
			float lat = Float.parseFloat(o.get(OurLocation.LATITUDE).toString());
			float lng = Float.parseFloat(o.get(OurLocation.LONGITUDE).toString());
			serviceSystem.resolveCoordsRequest(lat, lng);
			break;
		}
		
		case WIFI_SEARCH_REQUEST:{
			Log.d("MCS", "TADAAA...wifi request");
			LOGGER.C(this, "WIFI_SEARCH_REQUEST");
			serviceSystem.wifiSearchRequest();
			break;
		}
			
		case BLUETOOTH_SEARCH_REQUEST:
			LOGGER.C(this, "BLUETOOTH_SEARCH_REQUEST");
			serviceSystem.bluetoothSearchRequest();
			break;
			
		case WEATHERFORECAST_REQUEST:
			LOGGER.C(this, "WEATHERFORECAST_REQUEST");
			JSONObject o = BundleMessage.getInstance().extractObject(b);
			float lat = Float.parseFloat(o.get(OurLocation.LATITUDE).toString());
			float lng = Float.parseFloat(o.get(OurLocation.LONGITUDE).toString());
			serviceSystem.weatherForecastRequest(lat, lng);
			break;
			
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
			// TODO?
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
						// tag exists in database
						LOGGER.C(this, "TAG found: " + i.getName()+", hash: "+i.getImageHash());
						if (itemIndexSystem.itemScanned(i)) {
							// item put in
							LOGGER.C(this, "Item in: " + i.getName());
						} else {
							// item taken out
							LOGGER.C(this, "Item out: " + i.getName());
						}
						setStatusChanged();
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

	private void setStatusChanged() {

		ContainerStateUpdate statusUpdate = new ContainerStateUpdate(
				activitySystem.getCurrentActivity(),
				itemIndexSystem.getCurrentItems(), batteryStatus);

		Bundle toSend = BundleMessage.getInstance().createBundle(
				BundleMessage.BUNDLE_MESSAGE.CONTAINER_STATUS_UPDATE,
				statusUpdate.toString());
		btHelper.sendMessageBundle(toSend);
		
	}

	public void sendToRemote(BUNDLE_MESSAGE msg, Object toSendObj) {
		Bundle toSendBun = BundleMessage.getInstance().createBundle(msg,
				toSendObj);
		btHelper.sendMessageBundle(toSendBun);
	}

	public static void DEBUG() {
		debuginstance.setStatusChanged();
	}


	//battery
	private BroadcastReceiver mBatteryInfoReceiver = new BroadcastReceiver(){
	    @Override
	    public void onReceive(Context ctxt, Intent intent) {
	    	batteryStatus = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
	    }
	  };


}
