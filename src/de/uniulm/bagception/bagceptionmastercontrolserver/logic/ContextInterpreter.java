package de.uniulm.bagception.bagceptionmastercontrolserver.logic;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.uniulm.bagception.bundlemessageprotocol.entities.ContextSuggestion;
import de.uniulm.bagception.bundlemessageprotocol.entities.ContextSuggestion.CONTEXT;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.bundlemessageprotocol.entities.WeatherForecast;
import de.uniulm.bagception.mcs.services.MasterControlServer;

public class ContextInterpreter {

	private final MasterControlServer mcs;
	private List<ContextSuggestion> suggestions;
	
	private Set<CachedContextInfo> cache = new HashSet<CachedContextInfo>();
	
	
	
	public ContextInterpreter(MasterControlServer mcs){
		this.mcs = mcs;
	}
	
	/**
	 * the logic
	 * @param itemsIn
	 */
	public void updateList(List<Item> itemsIn){
		//TODO
		//eval itemsIn with cached context data
		
	}
	
	/**
	 * 
	 * @param itemsIn the items in the bag
	 * @return a list of suggestions
	 */
	public  List<ContextSuggestion> getContextSuggetions(){
		return suggestions;
	}
	
	
	private void onContextDataRecv(){
		//TODO update context data cache when new data is recv
		//drop context if too old
		//replace context
		
	}
	
	private HashSet<CachedContextInfo> getContexts(WeatherForecast forecast){
		//TODO calculates the current context
		HashSet<CachedContextInfo> ret = new HashSet<CachedContextInfo>();
		//wenn es reget:
		//	ret.add(new CachedContextInfo(CONTEXT.RAIN, "mehr text?, TODO REPLACE! vllt regenwahrscheinlichekit"));
		//TODO usw
		
		return ret;
	}
	
	
	public class CachedContextInfo{
		private final CONTEXT context;
		private final long timestamp;
		private final String data;
		
		public CachedContextInfo(CONTEXT c, String data){
			this.context=c;
			this.data=data;
			this.timestamp=System.currentTimeMillis();
		}

		public CONTEXT getContext() {
			return context;
		}

		public long getTimestamp() {
			return timestamp;
		}

		public String getData() {
			return data;
		}
		
		
	}
	
	
}
