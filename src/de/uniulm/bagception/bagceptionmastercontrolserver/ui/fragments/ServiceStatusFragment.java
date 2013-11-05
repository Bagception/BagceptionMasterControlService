package de.uniulm.bagception.bagceptionmastercontrolserver.ui.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.ListFragment;
import android.os.Bundle;
import de.uniulm.bagception.bagceptionmastercontrolserver.ui.service_status_fragment.ServiceInfo;
import de.uniulm.bagception.bagceptionmastercontrolserver.ui.service_status_fragment.ServiceInfoArrayAdapter;
import de.uniulm.bagception.services.ServiceNames;

public class ServiceStatusFragment extends ListFragment {

	private ServiceInfoArrayAdapter adapter;

	public static final List<ServiceInfo> bagceptionSystemServices = new ArrayList<ServiceInfo>(4);
	
	static{
		 {
			 ServiceInfo info = new ServiceInfo(null,null,"MasterControlService",ServiceNames.MASTER_CONTROL_SERVICE);
			 bagceptionSystemServices.add(info);
		 }
		
		 {
			 ServiceInfo info = new ServiceInfo(null,null,"RFID Service",ServiceNames.RFID_SERVICE);
			 bagceptionSystemServices.add(info);
		 }
		 
		 {
			 ServiceInfo info = new ServiceInfo(null,null,"Case Open Service",ServiceNames.CASE_OPEN_SERVICE);
			 bagceptionSystemServices.add(info);
		 }
		 
		 {
			 ServiceInfo info = new ServiceInfo(null,null,"Bluetooth Service",ServiceNames.BLUETOOTH_SERVER_SERVICE);
			 bagceptionSystemServices.add(info);
		 }
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		 adapter = new ServiceInfoArrayAdapter(getActivity());
		 for (ServiceInfo info:bagceptionSystemServices){
			 info.setActivity(getActivity());
			 info.setAdapter(adapter);
			 adapter.add(info);
		 }
		 
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

	// @Override
	// public View onCreateView(LayoutInflater inflater, ViewGroup container,
	// Bundle savedInstanceState) {
	//
	// return inflater.inflate(R.layout.service_status_fragment, null, false);
	//
	// }

}
