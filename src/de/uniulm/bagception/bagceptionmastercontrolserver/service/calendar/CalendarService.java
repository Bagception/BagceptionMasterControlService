package de.uniulm.bagception.bagceptionmastercontrolserver.service.calendar;

import de.uniulm.bagception.services.attributes.Calendar;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

public class CalendarService extends IntentService {

	private ResultReceiver resultReceiver;
	private CalendarReader reader;
	
	public CalendarService() {
		super("CalendarService");
		reader = new CalendarReader();

	}

	@Override
	protected void onHandleIntent(Intent intent) {
		resultReceiver = intent.getParcelableExtra("receiverTag");
		log("calendar request received!");
		
		
		if(intent.hasExtra(Calendar.REQUEST_TYPE)){
			if(intent.getStringExtra(Calendar.REQUEST_TYPE).equals(Calendar.CALENDAR_EVENTS)){
				// getting intent extras
				String[] calendarNames = intent.getStringArrayExtra(Calendar.CALENDAR_NAMES);
				int[] calendarIDs = intent.getIntArrayExtra(Calendar.CALENDAR_IDS);
				int numberOfEvents = intent.getIntExtra(Calendar.NUMBER_OF_EVENTS, -1);
				
				// getting and sending calendar events
				Bundle b = new Bundle();
				b.putStringArrayList(Calendar.CALENDAR_EVENTS, reader.readCalendarEvent(this, calendarNames, calendarIDs, numberOfEvents));
				b.putString(Calendar.RESPONSE_TYPE, Calendar.CALENDAR_EVENTS);
				resultReceiver.send(0, b);
				log("sending calendar events...");
			}
			if(intent.getStringExtra(Calendar.REQUEST_TYPE).equals(Calendar.CALENDAR_NAMES)){
				log("calendar names requested");
				Bundle b = new Bundle();
				b.putStringArrayList(Calendar.CALENDAR_NAMES, reader.getCalendarNames(this));
				b.putString(Calendar.RESPONSE_TYPE, Calendar.CALENDAR_NAMES);
				resultReceiver.send(0, b);
				log("sending calendar names...");
			}
		}
		
	}

	private void log(String s){
		Log.d("CalendarService", s);
	}
}
