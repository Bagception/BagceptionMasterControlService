package de.uniulm.bagception.bagceptionmastercontrolserver.ui.fragments;

import android.app.ListFragment;
import android.os.Bundle;
import de.uniulm.bagception.bagceptionmastercontrolserver.ui.service_status_fragment.ServiceInfo;
import de.uniulm.bagception.bagceptionmastercontrolserver.ui.service_status_fragment.ServiceInfoArrayAdapter;
import de.uniulm.bagception.services.ServiceNames;

public class ServiceStatusFragment extends ListFragment {

	
	private ServiceInfoArrayAdapter adapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		 adapter = new ServiceInfoArrayAdapter(getActivity());
		
		
		adapter.add(new ServiceInfo(getActivity(),adapter,"MasterControlService",ServiceNames.MASTER_CONTROL_SERVICE));
		adapter.add(new ServiceInfo(getActivity(),adapter,"RFID Service",ServiceNames.RFID_SERVICE));
		adapter.add(new ServiceInfo(getActivity(),adapter,"Case Open Service",ServiceNames.CASE_OPEN_SERVICE));
		adapter.add(new ServiceInfo(getActivity(),adapter,"Bluetooth Service",ServiceNames.BLUETOOTH_SERVER_SERVICE));
		
		setListAdapter(adapter);

	}

	@Override
	public void onResume() {
		adapter.forceUpdate();
		
		super.onResume();
	}
	
	@Override
	public void onPause() {
		adapter.pause();
		super.onPause();
	}
	
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//
//		return inflater.inflate(R.layout.service_status_fragment, null, false);
//
//	}


}
