package de.uniulm.bagception.bagceptionmastercontrolserver.actor_reactor;

import de.philipphock.android.lib.Reactor;

public interface CaseOpenServiceBroadcastReactor extends Reactor{

	
	public void caseOpened();
	public void caseClosed();
}
