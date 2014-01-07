package de.uniulm.bagception.mcs.services;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;
import de.philipphock.android.lib.logging.LOG;
import de.philipphock.android.lib.services.ServiceUtil;
import de.philipphock.android.lib.services.observation.ObservableService;
import de.uniulm.bagception.bagceptionmastercontrolserver.actor_reactor.CaseOpenBroadcastActor;
import de.uniulm.bagception.bagceptionmastercontrolserver.actor_reactor.CaseOpenServiceBroadcastReactor;
import de.uniulm.bagception.bagceptionmastercontrolserver.ui.fragments.ServiceStatusFragment;
import de.uniulm.bagception.bagceptionmastercontrolserver.ui.log_fragment.LOGGER;
import de.uniulm.bagception.bagceptionmastercontrolserver.ui.service_status_fragment.ServiceInfo;
import de.uniulm.bagception.bagceptionmastercontrolserver.ui.service_status_fragment.ServiceInfo.STATUS;
import de.uniulm.bagception.bluetoothservermessengercommunication.messenger.MessengerHelper;
import de.uniulm.bagception.bluetoothservermessengercommunication.messenger.MessengerHelperCallback;
import de.uniulm.bagception.broadcastconstants.BagceptionBroadcastContants;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.protocol.bundle.constants.Command;
import de.uniulm.bagception.protocol.bundle.constants.Response;
import de.uniulm.bagception.protocol.bundle.constants.StatusCode;
import de.uniulm.bagception.services.ServiceNames;


public class MasterControlServer extends ObservableService implements Runnable, MessengerHelperCallback, CaseOpenServiceBroadcastReactor{

/**
 * Handles the control flow of the container
 * @author phil
 *
 */

	private Thread mainThread;
	
	//bt
	private MessengerHelper btHelper;
	private CaseOpenBroadcastActor caseActor;
	private volatile boolean isScanning = false;


	@Override
	protected void onFirstInit() {
		
		mainThread = new Thread(this);
		mainThread.setDaemon(true);
		mainThread.start();
		
		caseActor = new CaseOpenBroadcastActor(this);
		caseActor.register(this);
		
		IntentFilter f = new IntentFilter(BagceptionBroadcastContants.BROADCAST_RFID_FINISHED);
		f.addAction(BagceptionBroadcastContants.BROADCAST_RFID_TAG_FOUND);
		f.addAction(BagceptionBroadcastContants.BROADCAST_RFID_START_SCANNING);
		f.addAction(BagceptionBroadcastContants.BROADCAST_RFID_NOTCONNECTED);
		registerReceiver(RFIDReceiver, f);
	}


	
	@Override
	public void onDestroy() {
		super.onDestroy();
		btHelper.unregister(this);
		caseActor.unregister(this);
		unregisterReceiver(RFIDReceiver);
		
		//stop services
		Intent i = new Intent();
		for(ServiceInfo serviceInfo:ServiceStatusFragment.bagceptionSystemServices){
			i.setAction(serviceInfo.getServiceSystemName());
			stopService(i);
			LOGGER.C(this, "stopping service "+serviceInfo.getName());
		}
		
		
		LOGGER.C(this, "MCS stopped");
	}
	
	/**
	 * starts all system components
	 */
	private void bootstrap(){
		ServiceUtil.logRunningServices(this, "###");
		//first start all necessary services
		LOGGER.C(this, "initiating bootstrap method, starting necessary services");
		Intent i = new Intent();
		for(ServiceInfo serviceInfo:ServiceStatusFragment.bagceptionSystemServices){
			if (!ServiceUtil.isServiceRunning(this, serviceInfo.getServiceSystemName())){
				LOGGER.C(this, "Service "+serviceInfo.getName() + " is offline.. starting it now");
				i.setAction(serviceInfo.getServiceSystemName());
				startService(i);
			}else{
				LOGGER.C(this, "Service "+serviceInfo.getName() + " already online.. ");
			}
		}
		
		//then check if all are started
		delayHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				//check if all services are running
				int runcount=0;
				LOGGER.C(this, "All services should be activated now.. I will check that:");
				for(ServiceInfo serviceInfo:ServiceStatusFragment.bagceptionSystemServices){
					if (!ServiceUtil.isServiceRunning(MasterControlServer.this, serviceInfo.getServiceSystemName())){
						LOGGER.C(this, "Service "+serviceInfo.getName() + " is still offline.. I think it is not installed");
						serviceInfo.setStatus(STATUS.NOT_INSTALLED);
					}else{
						runcount++;
						
					}
				}
				if (runcount==ServiceStatusFragment.bagceptionSystemServices.size()){
					LOGGER.C(this, "All Services online :)");
					//all services online:
					btHelper = new MessengerHelper(MasterControlServer.this, ServiceNames.BLUETOOTH_SERVER_SERVICE);
					btHelper.register(MasterControlServer.this);
					
				}
			}
		}, 100);
	}
	
	private final Handler delayHandler = new Handler();
	
//	private void serviceDiagnose(){
//		
//	}

	@Override
	public void run() {
		LOGGER.C(this, "MCS started");
		bootstrap();
		//stopSelf();
	}
	


	//\\ MessengerHelperCallback //
	@Override
	public void onBundleMessage(Bundle b) {
		//this comes from the client
		//DEBUG, send it back
		LOG.out(this, b);
		btHelper.sendMessageBundle(b);
	}

	@Override
	public void onResponseMessage(Bundle b) {
		// nothing to do here
		
	}

	@Override
	public void onStatusMessage(Bundle b) {
		// nothing to do here
		StatusCode status = StatusCode.getStatusCode(b);
		switch (status){
			case DISCONNECTED:
				//TODO?
				break;
			default: break;
		}
		
	}

	@Override
	public void onCommandMessage(Bundle b) {
		//nothing to do here
		
		
	}

	@Override
	public void onError(Exception e) {
		//we assume this is not going to happen
	}

	@Override
	public void connectedWithRemoteService() {
		//we assume this works correctly
		
	}

	@Override
	public void disconnectedFromRemoteService() {
		//we assume this works correctly

	}
	// MessengerHelperCallback \\



	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}



	// \\CaseOpenServiceBroadcastReactor//
	@Override
	public void  caseOpened() {
		synchronized (this) {
			if(isScanning) return;
			Intent rfidScanBC = new Intent(BagceptionBroadcastContants.BROADCAST_RFID_STARTSCAN);
			sendBroadcast(rfidScanBC);
		}
		
		
		
	}

	@Override
	public void caseClosed() {
		Intent rfidScanBC = new Intent(BagceptionBroadcastContants.BROADCAST_RFID_STOPSCAN);
		sendBroadcast(rfidScanBC);
		LOG.out(this,"case closed");
		LOGGER.C(this, "case closed");
	}
	
	//CaseOpenServiceBroadcastReactor\\
	
	private final ToneGenerator toneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
	private final BroadcastReceiver RFIDReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			
			
			if (BagceptionBroadcastContants.BROADCAST_RFID_FINISHED.equals(intent.getAction())){
				//scan finished
				
				isScanning = false;
				Toast.makeText(MasterControlServer.this, "scanning finished", Toast.LENGTH_SHORT).show();
				LOGGER.C(this, "stop rfid scan");
			}else if (BagceptionBroadcastContants.BROADCAST_RFID_TAG_FOUND.equals(intent.getAction())){
				//tag found
				
				toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP);
				
				String id = intent.getStringExtra(BagceptionBroadcastContants.BROADCAST_RFID_TAG_FOUND);
//				Item i = DatabaseConnector.getItem(id);
//				if (i!=null){
//					//tag exists in database
//					LOGGER.C(this, "TAG found: "+i.getName());
//					
//					Bundle b = BundleMessage.getInstance().toItemFoundBundle(i);
//					b.putBoolean("exists", true);
//					LOG.out(this, b);
//					btHelper.sendMessageBundle(b);
//				}else{
//					//tag not found in db
//					ArrayList<String> ids = new ArrayList<String>();
//					ids.add(id);
//					btHelper.sendMessageBundle(BundleMessage.getInstance().toItemFoundNotBundle(new Item("","",ids)));
//				}
					
				
			}else if (BagceptionBroadcastContants.BROADCAST_RFID_START_SCANNING.equals(intent.getAction())){
				// rfid start scanning
				
				Toast.makeText(MasterControlServer.this, "start scanning", Toast.LENGTH_SHORT).show();
				LOGGER.C(this, "start rfid scan");
			}else if (BagceptionBroadcastContants.BROADCAST_RFID_NOTCONNECTED.equals(intent.getAction())){
				//rfid not connected
				
				Toast.makeText(MasterControlServer.this, "RFID scanner not connected", Toast.LENGTH_SHORT).show();
				LOGGER.C(this, "RFID scanner not connected");
			}
			
			
		}
	};

}
