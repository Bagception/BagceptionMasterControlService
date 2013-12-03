package de.uniulm.bagception.bagceptionmastercontrolserver.actor_reactor;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import de.philipphock.android.lib.BroadcastActor;
import de.uniulm.bagception.broadcastconstants.BagceptionBroadcastContants;

public class CaseOpenBroadcastActor extends BroadcastActor<CaseOpenServiceBroadcastReactor> {

	public CaseOpenBroadcastActor(CaseOpenServiceBroadcastReactor reactor) {
		super(reactor);
	}

	@Override
	public void register(Context a) {
		IntentFilter filter = new IntentFilter(BagceptionBroadcastContants.BROADCAST_COMMAND_INTENT);
		filter.addAction(BagceptionBroadcastContants.BROADCAST_CASE_STATE);
	    a.registerReceiver(this, filter);		
	}

	@Override
	public void onReceive(Context context, Intent intent) {
			
		if (BagceptionBroadcastContants.BROADCAST_CASE_STATE.equals(intent.getAction())){
			if (BagceptionBroadcastContants.CASE_OPEN_STATE_CHANGED_TO_CLOSED == intent.getIntExtra(BagceptionBroadcastContants.BROADCAST_CASE_STATE,-1)){
				reactor.caseClosed();
			}else if (BagceptionBroadcastContants.CASE_OPEN_STATE_CHANGED_TO_OPEN == intent.getIntExtra(BagceptionBroadcastContants.BROADCAST_CASE_STATE,-1)){
				reactor.caseOpened();
			}
		}
		
	}

}
