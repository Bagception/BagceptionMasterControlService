package de.uniulm.bagception.bagceptionmastercontrolserver.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import de.uniulm.bagception.bagceptionmastercontrolserver.R;
import de.uniulm.bagception.bagceptionmastercontrolserver.ui.log_fragment.LOGGER;
import de.uniulm.bagception.mcs.services.MasterControlServer;

/**
 * Part of the multi-pane view<br>
 * provides the UI for the {@link LOGGER}
 * @author phil
 *
 */
public class DebugFragment extends Fragment{


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.debugfragmentlayout, null, false);

		Button sendUpdate =(Button) v.findViewById(R.id.sendupdate);
		sendUpdate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				MasterControlServer.DEBUG();
				
			}
		});

		

		return v;
	}

	

}
