package de.uniulm.bagception.bagceptionmastercontrolserver.ui.service_status_fragment;

import android.app.Activity;
import android.widget.BaseAdapter;

public class ServiceInfo {

	private final String serviceName;
	private final BaseAdapter adapter;
	private final Activity activity;

	public enum STATUS {
		ONLINE("online"), OFFLINE("offline"), NOT_INSTALLED("not installed"),UNKNOWN("unknown");
		private final String name;

		private STATUS(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	};

	private STATUS status = STATUS.UNKNOWN;

	public ServiceInfo(Activity activity, BaseAdapter adapter,
			String serviceName) {
		this.serviceName = serviceName;
		this.adapter = adapter;
		this.activity = activity;
	}

	public String getName() {
		return serviceName;
	}

	public STATUS getStatus() {
		return status;
	}

	private void dataseChanged() {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				adapter.notifyDataSetChanged();
			}
		});
	}
}
