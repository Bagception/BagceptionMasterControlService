package de.uniulm.bagception.mcs.services;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import de.philipphock.android.lib.services.ServiceUtil;
import de.philipphock.android.lib.services.messenger.MessengerService;
import de.uniulm.bagception.bagceptionmastercontrolserver.ui.fragments.ServiceStatusFragment;
import de.uniulm.bagception.bagceptionmastercontrolserver.ui.log_fragment.LOGGER;
import de.uniulm.bagception.bagceptionmastercontrolserver.ui.service_status_fragment.ServiceInfo;
import de.uniulm.bagception.bagceptionmastercontrolserver.ui.service_status_fragment.ServiceInfo.STATUS;

public class MasterControlServer extends MessengerService implements Runnable{

	private Thread mainThread;
	@Override
	protected void handleMessage(Message m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onFirstInit() {
		mainThread = new Thread(this);
		mainThread.setDaemon(true);
		mainThread.start();
		
	}


	
	@Override
	public void onDestroy() {
		super.onDestroy();
		LOGGER.C(this, "MCS stopped");
	}
	
	private void bootstrap(){
		
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

}
