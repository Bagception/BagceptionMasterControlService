package de.uniulm.bagception.bagceptionmastercontrolserver.service.weatherforecast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import de.uniulm.bagception.services.attributes.WeatherForecast;

public class WeatherForecastService extends IntentService {

	private ResultReceiver resultReceiver;

	public WeatherForecastService() {
		super("WeatherForecastService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d("WeatherForecastService", "intent received");
		resultReceiver = intent.getParcelableExtra("receiverTag");
		float lat = intent.getFloatExtra(WeatherForecast.LATITUDE, 0);
		float lng = intent.getFloatExtra(WeatherForecast.LONGITUDE, 0);
		
		String unit = intent.getStringExtra(WeatherForecast.UNIT);
		
		if(unit!=null){
			unit = "&units="+ unit;
		}else{
			unit = "&units=metric";
		}
		
		String address = "http://api.openweathermap.org/data/2.5/find?";
		String uri = address+"lat="+lat+"&lon="+lng+unit;
		DownloadJSONWeatherForecast task = new DownloadJSONWeatherForecast(resultReceiver);
		task.execute(uri);
	}


	private class DownloadJSONWeatherForecast extends
			AsyncTask<String, Void, JSONObject> {

		private ResultReceiver resultReceiver;

		public DownloadJSONWeatherForecast(ResultReceiver resultReceiver) {
			this.resultReceiver = resultReceiver;
		}

		@Override
		protected JSONObject doInBackground(String... urls) {
			log("do in background");
			String response = "";
			JSONObject jsonObject = null;
			for (String url : urls) {
				log("url: " + url);
				DefaultHttpClient client = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(url);
				try {
					HttpResponse execute = client.execute(httpGet);
					InputStream content = execute.getEntity().getContent();

					BufferedReader buffer = new BufferedReader(
							new InputStreamReader(content));
					String s = "";
					while ((s = buffer.readLine()) != null) {
						response += s;
					}
					JSONParser parser = new JSONParser();
					jsonObject = (JSONObject) parser.parse(response);
					Log.d("complete json", jsonObject.toString());
				} catch (Exception e) {
					log("error");
					e.printStackTrace();
				}
			}
			
			JSONObject answer = new JSONObject();
				log("building answer");
				JSONArray array = null;
				JSONObject mainObject = null;
				JSONObject windObject = null;
				JSONObject cloudObject = null;
				JSONObject rainObject = null;
				
				JSONParser parser = new JSONParser();
				
				String city = "";
				float temp = -99;
				float tempMin = -99;
				float tempMax = -99;
				float wind = -99;
				float clouds = -99;
				float rain = -99f;
				de.uniulm.bagception.bundlemessageprotocol.entities.WeatherForecast forecast = 
						new de.uniulm.bagception.bundlemessageprotocol.entities.WeatherForecast(city, tempMax, wind, clouds, tempMin, tempMax, rain);
				if(jsonObject.containsKey("list")){
					log("has list element");
					array = (JSONArray) jsonObject.get("list");
					try {
						JSONObject obj = (JSONObject) parser.parse(array.get(0).toString());
//						if(obj.containsKey("name")) forecast.setCity(obj.get("name").toString());
//						if(obj.containsKey("main")) mainObject = (JSONObject) parser.parse(obj.get("main").toString());
//						if(mainObject.containsKey("temp")) forecast.setTemp(Float.parseFloat(mainObject.get("temp").toString()));
//						if(mainObject.containsKey("temp_min")) forecast.setTemp_min(Float.parseFloat(mainObject.get("temp_min").toString()));
//						if(mainObject.containsKey("temp_max")) forecast.setTemp_max(Float.parseFloat(mainObject.get("temp_max").toString()));
//						if(obj.containsKey("wind")) windObject = (JSONObject) parser.parse(obj.get("wind").toString());
//						if(windObject != null && windObject.containsKey("speed")) forecast.setWind(Float.parseFloat(windObject.get("speed").toString()));
//						if(obj.containsKey("clouds")) cloudObject = (JSONObject) parser.parse(obj.get("clouds").toString());
//						if(cloudObject != null && cloudObject.containsKey("all")) forecast.setClouds(Float.parseFloat(cloudObject.get("all").toString()));
//						if(obj.containsKey("rain")) rainObject = (JSONObject) parser.parse(obj.get("rain").toString());
//						if(rainObject != null && rainObject.containsKey("3h"))	forecast.setRain(Float.parseFloat(rainObject.get("3h").toString()));
						if(obj.containsKey("name")) city =  obj.get("name").toString();
						if(obj.containsKey("main")) mainObject = (JSONObject) parser.parse(obj.get("main").toString());
						if(mainObject.containsKey("temp")) temp =  Float.parseFloat(mainObject.get("temp").toString());
						if(mainObject.containsKey("temp_min")) tempMin = Float.parseFloat(mainObject.get("temp_min").toString());
						if(mainObject.containsKey("temp_max")) tempMax = Float.parseFloat(mainObject.get("temp_max").toString());
						if(obj.containsKey("wind")) windObject = (JSONObject) parser.parse(obj.get("wind").toString());
						if(windObject != null && windObject.containsKey("speed")) wind = Float.parseFloat(windObject.get("speed").toString());
						if(obj.containsKey("clouds")) cloudObject = (JSONObject) parser.parse(obj.get("clouds").toString());
						if(cloudObject != null && cloudObject.containsKey("all")) clouds = Float.parseFloat(cloudObject.get("all").toString());
						if(obj.containsKey("rain")) rainObject = (JSONObject) parser.parse(obj.get("rain").toString());
						if(rainObject != null && rainObject.containsKey("3h"))	rain = Float.parseFloat(rainObject.get("3h").toString());
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				
				answer.put("city", city);
				answer.put("temp", temp);
				answer.put("wind", wind);
				answer.put("clouds", clouds);
				answer.put("temp_min", tempMin);
				answer.put("temp_max", tempMax);
				answer.put("rain", rain);
			return answer;
		}
		
		private void log(String string) {
			Log.d("WeatherForecastService", string);
		}

		@Override
		protected void onPostExecute(JSONObject jsonObject) {
			Bundle b = new Bundle();
			b.putString("payload", jsonObject.toString());
			b.putString(WeatherForecast.RESPONSE_TYPE, WeatherForecast.WEATHERFORECAST);
			resultReceiver.send(0, b);
		}

	}

}
