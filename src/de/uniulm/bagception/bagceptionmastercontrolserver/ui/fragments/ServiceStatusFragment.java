package de.uniulm.bagception.bagceptionmastercontrolserver.ui.fragments;

import android.app.ListFragment;
import android.os.Bundle;
import de.uniulm.bagception.bagceptionmastercontrolserver.ui.service_status_fragment.ServiceInfo;
import de.uniulm.bagception.bagceptionmastercontrolserver.ui.service_status_fragment.ServiceInfoArrayAdapter;

public class ServiceStatusFragment extends ListFragment {

	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		
		
		ServiceInfoArrayAdapter adapter = new ServiceInfoArrayAdapter(getActivity());
		adapter.add(new ServiceInfo(getActivity(),adapter,"MasterControlService"));
		adapter.add(new ServiceInfo(getActivity(),adapter,"MiniMe API Service"));
		adapter.add(new ServiceInfo(getActivity(),adapter,"Bluetooth Service"));
		adapter.add(new ServiceInfo(getActivity(),adapter,"Case Open Service"));
		
		setListAdapter(adapter);

	}

//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//
//		return inflater.inflate(R.layout.service_status_fragment, null, false);
//
//	}


}
