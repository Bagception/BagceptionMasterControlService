package de.uniulm.bagception.bagceptionmastercontrolserver.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import de.uniulm.bagception.bagceptionmastercontrolserver.R;
import de.uniulm.bagception.bagceptionmastercontrolserver.ui.log_fragment.LOGGER;

public class LogFragment extends Fragment {
	// TODO implement
	private TextView logView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.logger_fragment, null, false);

		Button clearButton = (Button) v.findViewById(R.id.clearLogger);

		logView = (TextView) v.findViewById(R.id.logOut);
		logView.setKeyListener(null);
		clearButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				clear();

			}
		});

		Button updateButton = (Button) v.findViewById(R.id.updateLogger);
		updateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				updateLog();
			}
		});

		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		updateLog();
	}

	private void clear() {
		LOGGER.clear();
		updateLog();
	}

	private void updateLog() {
		logView.setText("");

		for (String line : LOGGER.getLogs()) {
			logView.append(line);
			logView.append("\n");
		}

	}

}
