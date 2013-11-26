package de.uniulm.bagception.bagceptionmastercontrolserver.ui.log_fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
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
	@SuppressLint("SimpleDateFormat")
	private static final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
	
	
	private static final int LOG_LIST_MAX = 100;
	
//	static{
//		for(int i=0;i<100;i++){
//			LOGGER.C("", i+" dummy");
//		}
//		
//	}
	
	public static synchronized void C(Object origin, Object message){
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
	}
	
	
	public static void clear(){
		logList.clear();
	}
	
	public static final List<String> getLogs(){
		return logList;
				
	}
	
	private static final String getTime(){
		
		cal.setTimeInMillis(System.currentTimeMillis());
		Date date = cal.getTime();
		return formatter.format(date); 
	}
}
