package de.uniulm.bagception.bagceptionmastercontrolserver.ui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.uniulm.bagception.bagceptionmastercontrolserver.R;
import de.uniulm.bagception.bagceptionmastercontrolserver.ui.log_fragment.LOGGER;

/**
 * Part of the multi-pane view<br>
 * provides the UI for the {@link LOGGER}
 * @author phil, xaffe
 *
 */
public class DebugFragment extends Fragment{
	


//	private BundleMessageHelper helper;

	
	

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		//helper = new BundleMessageHelper(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.debugfragmentlayout, null, false);
		
		
		
	
		return v;
	}

	
//	private void log(String s){
//		Log.d("MCS", s);
//	}
}
