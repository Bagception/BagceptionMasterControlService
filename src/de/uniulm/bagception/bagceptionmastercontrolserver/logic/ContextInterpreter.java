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
	private List<Item> itemList;
	private Set<CachedContextInfo> cache = new HashSet<CachedContextInfo>();
	private WeatherForecast forecast;
	private Object lock = new Object();

	public ContextInterpreter(MasterControlServer mcs) {
		this.mcs = mcs;
	}

	/**
	 * the logic
	 * 
	 * @param itemsIn
	 */
	public void updateList(List<Item> itemsIn) {
		synchronized (lock) {

			// TODO make async forecast request
			itemList = itemsIn;
			if (forecast == null)
				return;
			if (itemList == null)
				return;

			// TODO
			// eval itemsIn with cached context data
			// update suggestions
			// use data from getContexts()
			if (cache == null) return;
			for (CachedContextInfo i : cache) {
				if (i.getTimestamp() < System.currentTimeMillis()
						- (4 * 1000 * 60 * 60)) {
					// context too old

				} else {
					// use context
					//TODO set suggestions
					switch (i.getContext()) {
					case BRIGHT:
						break;
					case COLD:
						break;
					case DARK:
						break;
					case RAIN:
						break;
					case SUNNY:
						break;
					case WARM:
						break;
					default:
						break;
					}
				}
			}

		}
	}

	/**
	 * 
	 * @param itemsIn
	 *            the items in the bag
	 * @return a list of suggestions
	 */
	public synchronized List<ContextSuggestion> getContextSuggetions() {
		synchronized (lock) {
			return suggestions;
		}
	}

	// TODO methode aufrufen
	private void onContextDataRecv(WeatherForecast forecast) {
		synchronized (lock) {
			cache.clear();
			cache = getContexts(forecast);
			if (itemList != null) {
				updateList(itemList);
			}
		}
	}

	private HashSet<CachedContextInfo> getContexts(WeatherForecast forecast) {
		synchronized (lock) {
			// TODO calculates the current context
			HashSet<CachedContextInfo> ret = new HashSet<CachedContextInfo>();
			// wenn es reget:#
			//if ..
			ret.add(new CachedContextInfo(CONTEXT.RAIN,
					"mehr text?, TODO REPLACE! vllt regenwahrscheinlichekit"));
			// TODO usw

			return ret;
		}
	}

	public class CachedContextInfo {
		private final CONTEXT context;
		private final long timestamp;
		private final String data;

		public CachedContextInfo(CONTEXT c, String data) {
			this.context = c;
			this.data = data;
			this.timestamp = System.currentTimeMillis();
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
