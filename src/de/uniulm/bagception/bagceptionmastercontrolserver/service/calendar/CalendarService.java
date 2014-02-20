package de.uniulm.bagception.bagceptionmastercontrolserver.service.calendar;

import java.util.ArrayList;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import de.uniulm.bagception.bundlemessageprotocol.entities.CalendarEvent;
import de.uniulm.bagception.services.attributes.Calendar;

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
		
		//TODO: get calendarnames from db
		if(intent.hasExtra(Calendar.REQUEST_TYPE)){
			if(intent.getStringExtra(Calendar.REQUEST_TYPE).equals(Calendar.CALENDAR_EVENTS)){
				log("requesting calendar events");
				// getting intent extras
				String[] calendarNames = intent.getStringArrayExtra(Calendar.CALENDAR_NAMES);
				int[] calendarIDs = intent.getIntArrayExtra(Calendar.CALENDAR_IDS);
				int numberOfEvents = intent.getIntExtra(Calendar.NUMBER_OF_EVENTS, -1);
				
				// getting and sending calendar events
				Bundle b = new Bundle();
				ArrayList<CalendarEvent> events = reader.readCalendarEvent(this, calendarNames, calendarIDs, numberOfEvents);
				ArrayList<String> stringEvents = new ArrayList<String>();
				for(CalendarEvent ce : events){
					stringEvents.add(ce.toJSONObject().toString());
				}
				b.putString(Calendar.RESPONSE_TYPE, Calendar.CALENDAR_EVENTS);
				b.putStringArrayList(Calendar.CALENDAR_EVENTS, stringEvents);
				resultReceiver.send(0, b);
//				log("sending calendar events...");
			}
			if(intent.getStringExtra(Calendar.REQUEST_TYPE).equals(Calendar.CALENDAR_NAMES)){
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
