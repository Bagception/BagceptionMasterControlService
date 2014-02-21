package de.uniulm.bagception.bagceptionmastercontrolserver.service.calendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.provider.CalendarContract.Events;
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
				log("sending response");
			}
			if(intent.getStringExtra(Calendar.REQUEST_TYPE).equals(Calendar.CALENDAR_NAMES)){
				Bundle b = new Bundle();
				b.putStringArrayList(Calendar.CALENDAR_NAMES, reader.getCalendarNames(this));
				b.putString(Calendar.RESPONSE_TYPE, Calendar.CALENDAR_NAMES);
				resultReceiver.send(0, b);
				log("sending calendar names...");
			}
			if(intent.getStringExtra(Calendar.REQUEST_TYPE).equals(Calendar.ADD_EVENT)){
				log("request wird bearbeitet...");
//				Bundle b = new Bundle();
				String s = intent.getStringExtra("payload");
				log(s);
				JSONParser parser = new JSONParser();
				JSONObject obj;
				CalendarEvent event = null;
				try {
					obj = (JSONObject) parser.parse(s);
					event = CalendarEvent.fromJSON(obj);
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (org.json.simple.parser.ParseException e) {
					e.printStackTrace();
				}
				log("name: " + event.getName());
				log("description: " + event.getDescription());
				log("calendar: " + event.getCalendarName());
				log("start: " + getDate(event.getStartDate()));
				log("end: " + getDate(event.getEndDate()));
				TimeZone tz = TimeZone.getDefault();
				intent.putExtra(Events.EVENT_TIMEZONE, "Germany");
				ContentResolver cr = getContentResolver();
				ContentValues values = new ContentValues();
				values.put(Events.DTSTART, event.getStartDate());
				values.put(Events.DTEND, event.getEndDate());
				values.put(Events.TITLE, event.getName());
				values.put(Events.DESCRIPTION, event.getDescription());
				values.put(Events.CALENDAR_ID, 1);
				values.put(Events.EVENT_TIMEZONE, tz.getID());
				Uri uri = cr.insert(Events.CONTENT_URI, values);
			}
		}
		
	}
	public String getDate(long milliSeconds) {
	    SimpleDateFormat formatter = new SimpleDateFormat(
	            "dd/MM/yyyy hh:mm:ss a");
	    java.util.Calendar calendar = java.util.Calendar.getInstance();
	    calendar.setTimeInMillis(milliSeconds);
	    return formatter.format(calendar.getTime());
	}

	private void log(String s){
		Log.d("CalendarService", s);
	}
}
