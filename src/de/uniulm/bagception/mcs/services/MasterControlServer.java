package de.uniulm.bagception.mcs.services;

import android.os.Message;
import de.philipphock.android.lib.services.messenger.MessengerService;
import de.uniulm.bagception.bagceptionmastercontrolserver.ui.log_fragment.LOGGER;

public class MasterControlServer extends MessengerService implements Runnable{

	private Thread mainThread;
	@Override
	protected void handleMessage(Message m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onFirstInit() {
		mainThread = new Thread(this);
		mainThread.start();
		LOGGER.C(this, "MCS started");
	}

	@Override
	public void run() {
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		LOGGER.C(this, "MCS stopped");
	}
	
	private void bootstrap(){
		LOGGER.C(this, "initiating bootstrap method, starting necessary services");
		
	}

}
