package de.uniulm.bagception.bagceptionmastercontrolserver.ui.service_status_fragment;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import de.philipphock.android.lib.logging.LOG;
import de.uniulm.bagception.bagceptionmastercontrolserver.R;
import de.uniulm.bagception.bagceptionmastercontrolserver.ui.service_status_fragment.ServiceInfo.STATUS;

public class ServiceInfoArrayAdapter extends ArrayAdapter<ServiceInfo> {

	public ServiceInfoArrayAdapter(Context context) {
		super(context, R.layout.service_status_fragment_item);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.service_status_fragment_item, null);
		}

		final ServiceInfo item = getItem(position);
		if (item != null) {
			TextView serviceName = (TextView) view
					.findViewById(R.id.serviceName);

			TextView serviceStatus = (TextView) view
					.findViewById(R.id.serviceStatus);

			
			
			Button serviceButton = (Button) view
					.findViewById(R.id.serviceControlButton);

			
			serviceName.setText(item.getName());
			
			serviceStatus.setText(item.getStatus().getName());

			serviceButton.setEnabled(true);
			if (item.getStatus() == STATUS.ONLINE){
				serviceStatus.setTextColor(Color.GREEN);
				serviceButton.setText("stop");
			}else if (item.getStatus() == STATUS.OFFLINE){
				serviceStatus.setTextColor(Color.RED);
				serviceButton.setText("start");
			}else if(item.getStatus() == STATUS.NOT_INSTALLED){
				serviceStatus.setTextColor(Color.BLUE);
				serviceButton.setEnabled(false);
				serviceButton.setText("start");
				
			}else if(item.getStatus() == STATUS.UNKNOWN){
				serviceStatus.setTextColor(Color.GRAY);
				serviceButton.setEnabled(false);
				serviceButton.setText("start");
				
			}
			

			
			
			serviceButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					item.startStopService();
				}
			});
			
		}

		return view;
	}
	
	
	public void forceUpdate(){
		for (int i=0;i<this.getCount();i++){
			ServiceInfo info = this.getItem(i);
			info.resume();
		}
	}

	public void pause(){
		for (int i=0;i<this.getCount();i++){
			ServiceInfo info = this.getItem(i);
			info.pause();
		}
	}
}
