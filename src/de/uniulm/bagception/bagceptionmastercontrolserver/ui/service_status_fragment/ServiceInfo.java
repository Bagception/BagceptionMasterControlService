package de.uniulm.bagception.bagceptionmastercontrolserver.ui.service_status_fragment;

import android.app.Activity;
import android.content.Intent;
import android.widget.BaseAdapter;
import de.philipphock.android.lib.logging.LOG;
import de.philipphock.android.lib.services.ServiceUtil;
import de.philipphock.android.lib.services.observation.ServiceObservationActor;
import de.philipphock.android.lib.services.observation.ServiceObservationReactor;

public class ServiceInfo implements ServiceObservationReactor {

	private final String serviceName;
	private final BaseAdapter adapter;
	private final Activity activity;
	private final String serviceSystemName;

	private final ServiceObservationActor soActor;
	
	public enum STATUS {
		ONLINE("online"), OFFLINE("offline"), NOT_INSTALLED("not installed"), UNKNOWN(
				"unknown");
		private final String name;

		private STATUS(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	};

	private STATUS status = STATUS.UNKNOWN;

	public ServiceInfo(Activity activity, BaseAdapter adapter,
			String serviceName, String serviceSystemName) {
		this.serviceName = serviceName;
		this.adapter = adapter;
		this.activity = activity;
		this.serviceSystemName = serviceSystemName;
		soActor = new ServiceObservationActor(this, serviceSystemName);
		
		resume();
	}

	public String getName() {
		return serviceName;
	}

	public STATUS getStatus() {
		return status;
	}

	
	//callbacks for activity
	public void resume() {
		if (ServiceUtil.isServiceRunning(activity, serviceSystemName)) {
			status = STATUS.ONLINE;
		}else{
			status = STATUS.OFFLINE;
		}
		soActor.register(activity);
		dataseChanged();
		//register
	}
	
	public void pause(){
		//unregister
		soActor.unregister(activity);
	}

	private void dataseChanged() {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				adapter.notifyDataSetChanged();
			}
		});
	}
	
	public void startStopService(){
		
		Intent i = new Intent(serviceSystemName);
		if (status==STATUS.ONLINE){
			activity.stopService(i);
			return;
		}
		if (status==STATUS.OFFLINE){
			activity.startService(i);
			return;
		}
	}

	
	//ServiceObservationReactor
	@Override
	public void onServiceStarted(String serviceName) {
		status=STATUS.ONLINE;
		dataseChanged();
	}

	@Override
	public void onServiceStopped(String serviceName) {
		status=STATUS.OFFLINE;
		dataseChanged();
		
	}
}
