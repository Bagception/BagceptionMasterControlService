package de.uniulm.bagception.bagceptionmastercontrolserver.service.calendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.util.Log;
import de.uniulm.bagception.bundlemessageprotocol.entities.CalendarEvent;

public class CalendarReader {
	
	private String[] EVENT_PROJECTION;						// select statement
	private String SELECTION;								//  where statement
	private Cursor cursor;
	private ArrayList<CalendarEvent> calendarEvents;				// return value
	private HashMap<String, Integer> idNameHashMap;			// hashmap of unique calendar name id pairs
	private Uri uri;
	private Context context;

	
	public ArrayList<CalendarEvent> readCalendarEvent(Context context, String[] calendarNames, int[] calendarIDs, int numberOfEvents) {
		this.context = context;
		cursor = null;
		calendarEvents = new ArrayList<CalendarEvent>();			
		idNameHashMap = new HashMap<String, Integer>();		

		
		uri = Calendars.CONTENT_URI;
		
		EVENT_PROJECTION = new String[] {			
					Calendars._ID,
					Calendars.NAME,
		};
		
		// getting calendar names and ids
		cursor = context.getContentResolver().query(
					uri,
					EVENT_PROJECTION, 
					null, null, null);
		 
		while (cursor.moveToNext()) {
			idNameHashMap.put(cursor.getString(1), Integer.parseInt(cursor.getString(0)));
//			log("id: " + cursor.getString(0));
//			log("name: " + cursor.getString(1));
		}
		
		uri = Uri.parse("content://com.android.calendar/events");
		
		EVENT_PROJECTION = new String[] {
				"title",
				"calendar_id",
				"description",
				"eventLocation",
				"dtstart",
				"dtend"
		};
		
		
		// building selection statement (where clause)
		// 1. just select dates in future
		SELECTION = Events.DTSTART + ">" + System.currentTimeMillis();
		
		// 2. adding all events from given calendar ids
		if(calendarIDs != null){
			for(int i = 0; i < calendarIDs.length; i++){
				SELECTION += " AND " + "calendar_id=" + calendarIDs[i]; 
			}
		}
		
		// 3. adding all events from given calendar names 
		if(calendarNames != null){
			for(int i = 0; i< calendarNames.length; i++){
				if(idNameHashMap.containsKey(calendarNames[i])){
					SELECTION += " AND " + "calendar_id=" + idNameHashMap.get(calendarNames[i]);
				}
			}
		}
		
		// building query
		cursor = context.getContentResolver().query(
				uri,
				EVENT_PROJECTION, 
				SELECTION, null, "dtstart"	
				);
		
//		log("##################################");
		
		int number = 0;
		while (cursor.moveToNext() && (number<numberOfEvents || numberOfEvents==-1)) {
			String name = "";
			String calendarName = "";
			String description = "";
			String location = "";
			long startDate = 0;
			long endDate = 0;
			if(!cursor.getString(0).isEmpty()) name = cursor.getString(0);
			if(!cursor.getString(1).isEmpty()) calendarName = cursor.getString(1);
			if(!cursor.getString(2).isEmpty()) description = cursor.getString(2);
			if(!cursor.getString(3).isEmpty()) location = cursor.getString(3);
			if(!cursor.getString(4).isEmpty()) startDate = Long.parseLong(cursor.getString(4));
			if(!cursor.getString(5).isEmpty()) endDate = Long.parseLong(cursor.getString(5));
			
			CalendarEvent event = new CalendarEvent(name, calendarName, description, location, startDate, endDate);
			calendarEvents.add(event);
			number++;
		}
		return calendarEvents;
	}
	
	/*
	 * returns ArrayList with all calendar names
	 */
	public ArrayList<String> getCalendarNames(Context context){
		
		ArrayList<String> calendarNames = new ArrayList<String>();
		uri = Calendars.CONTENT_URI;
		
		EVENT_PROJECTION = new String[] {			
					Calendars._ID,
					Calendars.NAME,
		};
		
		// getting calendar names and ids
		cursor = context.getContentResolver().query(
					uri,
					EVENT_PROJECTION, 
					null, null, null);
		 
		while (cursor.moveToNext()) {
			calendarNames.add(cursor.getString(1));
		}
		return calendarNames;
	}
	
	
	public String getDate(long milliSeconds) {
	    SimpleDateFormat formatter = new SimpleDateFormat(
	            "dd/MM/yyyy hh:mm:ss a");
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTimeInMillis(milliSeconds);
	    return formatter.format(calendar.getTime());
	}
	
	private void log(String s){
		Log.d("CalendarReader", s);
	}

}
