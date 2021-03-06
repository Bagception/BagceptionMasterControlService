package de.uniulm.bagception.bagceptionmastercontrolserver.ui.service_status_fragment;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.widget.BaseAdapter;
import android.widget.Toast;
import de.philipphock.android.lib.services.ServiceUtil;
import de.philipphock.android.lib.services.observation.ServiceObservationActor;
import de.philipphock.android.lib.services.observation.ServiceObservationReactor;

/**
 * collects data of the component-services's states and controls their lifecycle (triggers start/stop)
 * <br> Keep in mind that stopping a service turns out to be difficult when another component is bound to it
 * @author phil
 *
 */
public class ServiceInfo implements ServiceObservationReactor {

	private final String serviceName;
	private BaseAdapter adapter;
	private Activity activity;
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
		
		if (activity != null)
			resume();
	}

	public String getName() {
		return serviceName;
	}

	public STATUS getStatus() {
		return status;
	}

	public String getServiceSystemName(){
		return serviceSystemName;
	}
	
	public void setStatus(STATUS state){
		this.status = state;
		dataseChanged();

	}
	
	public void setActivity(Activity activity){
		this.activity = activity;
	}
	
	public void setAdapter(BaseAdapter adapter){
		this.adapter = adapter;
	}
	
	//callbacks for activity
	public void resume() {
		if (ServiceUtil.isServiceRunning(activity, serviceSystemName)) {
			status = STATUS.ONLINE;
		}else{
			if (status == STATUS.UNKNOWN){
				status = STATUS.OFFLINE;
			}
		}
		soActor.register(activity);
		dataseChanged();
		//register
	}
	
	public void pause(){
		//unregister
		if (activity != null)
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
		}else{
			if (status == STATUS.NOT_INSTALLED){
				Toast.makeText(activity, "The Service seems not installed, I will try it anyway", Toast.LENGTH_SHORT).show();
			}
			activity.startService(i);
			
			checkIfInstalledTimer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					if (ServiceInfo.this.status==STATUS.OFFLINE){
				    	ServiceInfo.this.status=STATUS.NOT_INSTALLED;
				    	ServiceInfo.this.dataseChanged();
				    }
				}
			}, 100);
			
			
			return;
		}
	}

		
	final Timer checkIfInstalledTimer = new Timer(); 
	
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
