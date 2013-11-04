package de.uniulm.bagception.bagceptionmastercontrolserver.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.uniulm.bagception.bagceptionmastercontrolserver.R;


public class ServiceStatusFragment extends Fragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return 		inflater.inflate(R.layout.service_status_fragment, null,false);

	}
}
