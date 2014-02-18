package de.uniulm.bagception;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import de.uniulm.bagception.bagceptionmastercontrolserver.ui.log_fragment.LOGGER;
import de.uniulm.bagception.mcs.services.MasterControlServer;

/**
 * ensures that the service is started at system start
 * @author phil
 *
 */
public class BootLoader extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		LOGGER.C(this, "Bootstrap code reached");
		BootLoader.start(context);
	}
	
	public static void start(Context context){
		 Intent startServiceIntent = new Intent(context, MasterControlServer.class);
	        context.startService(startServiceIntent);	
	}

}
