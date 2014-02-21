package de.uniulm.bagception.bagceptionmastercontrolserver.logic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import de.uniulm.bagception.bagceptionmastercontrolserver.database.DatabaseException;
import de.uniulm.bagception.bagceptionmastercontrolserver.database.DatabaseHelper;
import de.uniulm.bagception.bagceptionmastercontrolserver.service.weatherforecast.WeatherForecastService;
import de.uniulm.bagception.bundlemessageprotocol.entities.ContextSuggestion;
import de.uniulm.bagception.bundlemessageprotocol.entities.ContextSuggestion.CONTEXT;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.bundlemessageprotocol.entities.ItemAttribute;
import de.uniulm.bagception.bundlemessageprotocol.entities.Location;
import de.uniulm.bagception.bundlemessageprotocol.entities.WeatherForecast;
import de.uniulm.bagception.intentservicecommunication.MyResultReceiver;
import de.uniulm.bagception.intentservicecommunication.MyResultReceiver.Receiver;
import de.uniulm.bagception.mcs.services.MasterControlServer;
//import de.uniulm.bagception.services.attributes.WeatherForecast;

public class ContextInterpreter implements Receiver{

	private final MasterControlServer mcs;
	private List<ContextSuggestion> suggestions;
	private List<Item> itemList;
	private Set<CachedContextInfo> cache = new HashSet<CachedContextInfo>();
	private WeatherForecast forecast;
	private Object lock = new Object();
	private MyResultReceiver resultReceiver;
	private final DatabaseHelper db;
	private List<Item> suggestedItems;
	private List<Item> suggestedContextItems;
	private Intent weatherIntent;

	
	private JSONParser parser = new JSONParser();
	private JSONObject object = null;
	
	
	public ContextInterpreter(MasterControlServer mcs) {
		this.mcs = mcs;
		
		resultReceiver = new MyResultReceiver(new Handler());
		resultReceiver.setReceiver(this);
		db=mcs.getDB();
	}

	/**
	 * the logic
	 * 
	 * @param itemsIn
	 * @throws DatabaseException 
	 */
	public void updateList(List<Item> itemsIn) throws DatabaseException {
		synchronized (lock) {

			Log.w("TEST", "updateList gestartet (Server/ContextInterpreter)");
			
			Location loc = mcs.getLocation();
			Log.w("TEST", "Location der Aktivität (Server/ContextInterpreter): " + loc);
			
			if(loc == null) return;
				
			itemList = itemsIn;
			
			Log.w("TEST", "List<Item> itemsIn (Server/ContextInterpreter): " + itemsIn);
			
			List<Long> item_ids = db.getContextItems();
			Log.w("TEST", "ContextItems (Server/ContextInterpreter): " + item_ids);			
			
			List<Item> contextItems = new ArrayList<Item>();
			
			if(item_ids != null){
				for(long l:item_ids){
					Log.w("TEST", "ContextItemId in Item umwandeln (Server/ContextInterpreter)");
					Item item = db.getItem(l);
					contextItems.add(item);
					Log.w("TEST", "ContextItems (Server/ContextInterpreter): " + contextItems);
				}
			}
			
			Log.d("TEST", "Überprüfe Wetter (Server/ContextInterpreter): " + forecast);
			Log.d("TEST", "Items in Bag (Server/ContextInterpreter): " + itemList);
			Log.d("TEST", "Forecast: " + forecast);
			
			if (forecast == null)
				Log.w("TEST", "Überprüfe Wetterkoordinaten (Server/ContextInterpreter): " + loc);
			
				weatherIntent = new Intent(mcs.getBaseContext(), WeatherForecastService.class);
				weatherIntent.putExtra("receiverTag", resultReceiver);
				weatherIntent.putExtra(de.uniulm.bagception.services.attributes.WeatherForecast.LATITUDE, loc.getLat());
				weatherIntent.putExtra(de.uniulm.bagception.services.attributes.WeatherForecast.LONGITUDE, loc.getLng());
				mcs.startService(weatherIntent);
				
			if (itemList == null)
				return;
			
			Log.w("TEST", "Überprüfung des Caches: " + cache.size());
			if(cache.size() == 0 && forecast != null){
				Log.w("TEST", "Wettercache gleich null (Server/ContextInterpreter)");
				onContextDataRecv(forecast);
			} 

			Log.w("TEST", "Überspringe den Cache");
			
			suggestions = new ArrayList<ContextSuggestion>();
			
			for (CachedContextInfo i : cache) {
				if (i.getTimestamp() < System.currentTimeMillis()
						- (4 * 1000 * 60 * 60)) {

					Log.w("TEST", "Cache veraltet (Server/ContextInterpreter)");
					
					Intent intent = new Intent(mcs.getBaseContext(), WeatherForecastService.class);
					intent.putExtra("receiverTag", resultReceiver);
					intent.putExtra(de.uniulm.bagception.services.attributes.WeatherForecast.LATITUDE, loc.getLat());
					intent.putExtra(de.uniulm.bagception.services.attributes.WeatherForecast.LONGITUDE, loc.getLng());
					mcs.startService(intent);
					
					onContextDataRecv(forecast);
					
				} else {
					ContextSuggestion suggestion;
					
					Log.w("TEST", "Cache nicht veraltet, überprüfe Items (Server/ContextInterpreter): " + itemList);
					for(Item item:itemList){
						
						ItemAttribute iA = item.getAttribute();
						Log.w("TEST", "ItemAttribute (Server/ContextInterpreter): " + iA);
						if(iA == null) return;
						
						switch (i.getContext()) {
						
						case BRIGHT:
							suggestedItems = new ArrayList<Item>();
							if(iA.getLightness().equals("light")){
								return;
							} else if(iA.getLightness().equals("dark")){
								
								if(item.getCategory() != null){
									long cat_id = item.getCategory().getId();
									
									// List with items with the same category_id
									List<Item> sug_items = db.getItems(cat_id);
									
									for(Item si:sug_items){
										if(si.getAttribute() != null){
											if(si.getAttribute().getLightness().equals("light")){
												suggestedItems.add(si);
											}
										}
									}
								}
							}
							suggestion = new ContextSuggestion(item, suggestedItems, i.getContext());
							suggestions.add(suggestion);
							break;
							
						case COLD:
							suggestedItems = new ArrayList<Item>();
							if(iA.getTemperature().equals("cold")){
								return;
							} else if(iA.getTemperature().equals("warm")){
								
								if(item.getCategory() != null){
									long cat_id = item.getCategory().getId();
									
									// List with items with the same category_id
									List<Item> sug_items = db.getItems(cat_id);
									
									for(Item si:sug_items){
										if(si.getAttribute() != null){
											if(si.getAttribute().getTemperature().equals("cold")){
												suggestedItems.add(si);
											}
										}
									}
								}
							}
							suggestion = new ContextSuggestion(item, suggestedItems, i.getContext());
							
							Log.w("TEST", "Suggestion: " + suggestion);
							Log.w("TEST", "Suggestions: " + suggestions);
							
							suggestions.add(suggestion);
							break;
							
						case DARK:
							suggestedItems = new ArrayList<Item>();
							if(iA.getLightness().equals("dark")){
								return;
							} else if(iA.getLightness().equals("light")){
								
								if(item.getCategory() != null){
									long cat_id = item.getCategory().getId();
									
									// List with items with the same category_id
									List<Item> sug_items = db.getItems(cat_id);
									
									for(Item si:sug_items){
										if(si.getAttribute() != null){
											if(si.getAttribute().getLightness().equals("dark")){
												suggestedItems.add(si);
											}
										}
									}
								}
							}
							suggestion = new ContextSuggestion(item, suggestedItems, i.getContext());
							
							Log.w("TEST", "SuggestionItem: " + suggestion);
							Log.w("TEST", "SuggestionItemS: " + suggestions);
							
							suggestions.add(suggestion);
							break;
							
						case RAIN:
							suggestedItems = new ArrayList<Item>();
							if(iA.getWeather().equals("rainy")){
								return;
							} else if(iA.getWeather().equals("sunny")){
								
								if(item.getCategory() != null){
									long cat_id = item.getCategory().getId();
									
									// List with items with the same category_id
									List<Item> sug_items = db.getItems(cat_id);
									
									for(Item si:sug_items){
										if(si.getAttribute() != null){
											if(si.getAttribute().getWeather().equals("rainy")){
												suggestedItems.add(si);
											}
										}
									}
								}
							}
							suggestion = new ContextSuggestion(item, suggestedItems, i.getContext());
							suggestions.add(suggestion);
							break;
							
						case SUNNY:
							suggestedItems = new ArrayList<Item>();
							if(iA.getWeather().equals("sunny")){
								return;
							} else if(iA.getWeather().equals("rainy")){
								
								if(item.getCategory() != null){
									long cat_id = item.getCategory().getId();
									
									// List with items with the same category_id
									List<Item> sug_items = db.getItems(cat_id);
									
									for(Item si:sug_items){
										if(si.getAttribute() != null){
											if(si.getAttribute().getWeather().equals("sunny")){
												suggestedItems.add(si);
											}
										}
									}
								}
							}
							suggestion = new ContextSuggestion(item, suggestedItems, i.getContext());
							
							Log.w("TEST", "Suggestion: " + suggestion);
							Log.w("TEST", "Suggestions: " + suggestions);
							
							suggestions.add(suggestion);
							break;
							
						case WARM:
							suggestedItems = new ArrayList<Item>();
							if(iA.getTemperature().equals("warm")){
								return;
							} else if(iA.getTemperature().equals("cold")){
								
								if(item.getCategory() != null){
									long cat_id = item.getCategory().getId();
									
									// List with items with the same category_id
									List<Item> sug_items = db.getItems(cat_id);
									
									for(Item si:sug_items){
										if(si.getAttribute() != null){
											if(si.getAttribute().getTemperature().equals("warm")){
												suggestedItems.add(si);
											}
										}
									}
								}
							}
							suggestion = new ContextSuggestion(item, suggestedItems, i.getContext());
							suggestions.add(suggestion);
							break;
						default:
							break;
						}
						
					}
					
					suggestedContextItems = new ArrayList<Item>();
					for(Item ci:contextItems){
						
						ItemAttribute cia = ci.getAttribute();
						if(cia == null) return;
						
						switch(i.getContext()){
						case BRIGHT:
							if(suggestedContextItems.contains(ci)) return;
							if(cia.getLightness().equals("ligth")){
								suggestedContextItems.add(ci);
							}
							suggestion = new ContextSuggestion(null, suggestedItems, i.getContext());
							suggestions.add(suggestion);
							break;
						case COLD:
							if(suggestedContextItems.contains(ci)) return;
							if(cia.getTemperature().equals("cold")){
								suggestedContextItems.add(ci);
							}
							suggestion = new ContextSuggestion(null, suggestedItems, i.getContext());
							suggestions.add(suggestion);
							break;
						case DARK:
							if(suggestedContextItems.contains(ci)) return;
							if(cia.getLightness().equals("dark")){
								suggestedContextItems.add(ci);
							}
							suggestion = new ContextSuggestion(null, suggestedItems, i.getContext());
							suggestions.add(suggestion);
							break;
						case RAIN:
							if(suggestedContextItems.contains(ci)) return;
							if(cia.getWeather().equals("rainy")){
								suggestedContextItems.add(ci);
							}
							suggestion = new ContextSuggestion(null, suggestedItems, i.getContext());
							suggestions.add(suggestion);
							break;
						case SUNNY:
							if(suggestedContextItems.contains(ci)) return;
							if(cia.getWeather().equals("sunny")){
								suggestedContextItems.add(ci);
							}
							suggestion = new ContextSuggestion(null, suggestedItems, i.getContext());
							suggestions.add(suggestion);
							break;
						case WARM:
							if(suggestedContextItems.contains(ci)) return;
							if(cia.getTemperature().equals("warm")){
								suggestedContextItems.add(ci);
							}
							suggestion = new ContextSuggestion(null, suggestedItems, i.getContext());
							suggestions.add(suggestion);
							break;
						default:
							break;
						
						}
						
						
						Log.w("TEST", "FOR-Schleife, suggestions, ContextInterpreter:371: " + suggestions);
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
			Log.w("TEST", "getContextSuggestions (Server/ContextInterpreter): " + suggestions);
			return suggestions;
		}
	}

	private void onContextDataRecv(WeatherForecast forecast) throws DatabaseException {
		synchronized (lock) {
			cache.clear();
			cache = getContexts(forecast);
			Log.w("TEST", "Wettervorhersage (Server/ContextInterpreter): " + forecast);
			
			if (itemList != null) {
				updateList(itemList);
			}
		}
	}

	private HashSet<CachedContextInfo> getContexts(WeatherForecast forecast) {
		synchronized (lock) {

			Log.w("TEST", "getContexts (Server/ContextInterpreter)");
			Log.w("TEST", "Object in getContexts: " + object);
			Log.w("TEST", "Forecast in getContexts: " + forecast);
			
			
			HashSet<CachedContextInfo> ret = new HashSet<CachedContextInfo>();
			float weather = Float.parseFloat(object.get("rain").toString());
			float temp = Float.parseFloat(object.get("temp").toString());
			long time = System.currentTimeMillis();
			long sunset = 64800000; // 18:00 pm = 64 800 000 Milliseconds
			
			if(weather > 50){
			ret.add(new CachedContextInfo(CONTEXT.RAIN, object.get("rain").toString()));
			} else {
				ret.add(new CachedContextInfo(CONTEXT.SUNNY, object.get("rain").toString()));
			} 

			if(temp > 18){
				ret.add(new CachedContextInfo(CONTEXT.WARM, object.get("temp").toString()));
			} else{
				ret.add(new CachedContextInfo(CONTEXT.COLD, object.get("temp").toString()));
			}
			
			if(time > sunset){
				ret.add(new CachedContextInfo(CONTEXT.DARK, "Uhrzeit"));
			} else{
				ret.add(new CachedContextInfo(CONTEXT.BRIGHT, "Uhrzeit"));
			}
			
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

	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		
		
		Log.w("TEST", "onReceiveResult (Server/ContextInterpreter)");

		if(resultData.getString(de.uniulm.bagception.services.attributes.WeatherForecast.RESPONSE_TYPE)
				.equals(de.uniulm.bagception.services.attributes.WeatherForecast.WEATHERFORECAST)){

			String s = resultData.getString("payload");
			
			Log.w("TEST", "p (Server/ContextInterpreter): " + s);
			
			try {
				object = (JSONObject) parser.parse(s);
				forecast = new de.uniulm.bagception.bundlemessageprotocol.entities.WeatherForecast(
					object.get("city").toString(), 
					Float.parseFloat(object.get("temp").toString()),
					Float.parseFloat(object.get("wind").toString()),
					Float.parseFloat(object.get("clouds").toString()),
					Float.parseFloat(object.get("temp_min").toString()),
					Float.parseFloat(object.get("temp_max").toString()),
					Float.parseFloat(object.get("rain").toString())
				);
				
				Log.w("TEST", "Wetter nach Abruf (Server/ContextInterpreter): " + forecast);
				
//				log("------- GET WEATHER FORECAST------");
//				log(" city: " + forecast.getCity());
//				log(" temp: " + forecast.getTemp());
//				log(" wind: " + forecast.getWind()); 
//				log(" clouds: " + forecast.getClouds());
//				log(" tempMin: " + forecast.getTemp_min());
//				log(" tempMax: " + forecast.getTemp_max()); 
//				log(" rain: " + forecast.getRain());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

}
