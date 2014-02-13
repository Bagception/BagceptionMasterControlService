package de.uniulm.bagception.bagceptionmastercontrolserver.ui.log_fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import de.philipphock.android.lib.logging.LOG;

/**
 * provides a logging console so see what happens in this service
 * @author phil
 *
 */
public class LOGGER {
	private static final boolean out_to_logcat=true;
	private static final List<String> logList  = new LinkedList<String>(); 
	private static final Calendar cal = Calendar.getInstance();
	private static final StringBuilder sb = new StringBuilder();
	private static Context context;
	
	private static final Object lock = new Object();
	
	@SuppressLint("SimpleDateFormat")
	private static final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
	
	
	private static final int LOG_LIST_MAX = 100;
	
//	static{
//		for(int i=0;i<100;i++){
//			LOGGER.C("", i+" dummy");
//		}
//		
//	}
	
	public static void C(Object origin, Object message){
		synchronized (lock) {
			
		
			if (origin instanceof Context){
				LOGGER.context = (Context) origin;	
			}
			
			if (out_to_logcat)
				LOG.out(origin, message);
			
			sb.setLength(0);
			
			sb.append("[");
			sb.append(getTime());
			sb.append("]:\n");
			sb.append(message);
			logList.add(sb.toString());
			
			if (logList.size()>LOG_LIST_MAX){
				logList.remove(0);
			}
			Intent logIntent = new Intent("de.uniulm.bagception.bc.log");
			if (context == null) return;
			LocalBroadcastManager.getInstance(context).sendBroadcast(logIntent);
		}
	}
	
	
	public static void clear(){
		synchronized (lock) {
			logList.clear();
		}
	}
	
	public static final List<String> getLogs(){
		synchronized (lock) {
			return logList;
		}
				
	}
	
	private static final String getTime(){
		
		cal.setTimeInMillis(System.currentTimeMillis());
		Date date = cal.getTime();
		return formatter.format(date); 
	}
}
