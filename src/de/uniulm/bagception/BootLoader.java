package de.uniulm.bagception;

import de.uniulm.bagception.bagceptionmastercontrolserver.ui.log_fragment.LOGGER;
import de.uniulm.bagception.mcs.services.MasterControlServer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootLoader extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		LOGGER.C(this, "Bootstrap code reached");
		 Intent startServiceIntent = new Intent(context, MasterControlServer.class);
	        context.startService(startServiceIntent);		
	}

}
