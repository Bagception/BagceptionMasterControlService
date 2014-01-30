package de.uniulm.bagception.bagceptionmastercontrolserver.database;

import java.util.ArrayList;
import java.util.List;

import de.uniulm.bagception.bagceptionmastercontrolserver.R;
import de.uniulm.bagception.bagceptionmastercontrolserver.R.layout;
import de.uniulm.bagception.bagceptionmastercontrolserver.R.menu;
import de.uniulm.bagception.bundlemessageprotocol.entities.Category;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.bundlemessageprotocol.entities.Location;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class DatabaseTest extends Activity {
	
	DatabaseHelper db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_database_test);
		
		db = new DatabaseHelper(getBaseContext());
		
		
/****************************************************************************************************************************************************
 *  NUR TESTAUFRUFE!!!!!!!!!!!!!!!!!!!		
 ****************************************************************************************************************************************************/
		// Create Objects
		Category cat = new Category("Anziehsachen");
		Category cat2 = new Category("Unizeugs");
		
		String hose = "Andere Hose";
		String shirt = "Zweites T-Shirt";
		ArrayList<String> tagIds = new ArrayList<String>();
		tagIds.add("123456789");
		
		// Insert to database
		
//		try {
//			db.addCategory(cat2);
//			Log.w("TEST", "Kategorie " + cat2.getName() + " hinzugefügt");
//		} catch (DatabaseException e) {
//			e.printStackTrace();
//		}
		
//		Item i = new Item("LaLiLu");
//		
//		try {
//			db.addItem(i);
//			Log.w("TEST", "Item " + i.getName() + " hinzugefügt");
//			
//		} catch (DatabaseException e) {
//			Log.w("TEST", "Das ging schief");
//			e.printStackTrace();
//		}
		
		
		
		
//		Item ic = new Item("CatItem", cat);
//		Log.w("TEST", "Item CatItem hinzugefügt");
//		
//		try {
//			db.addItem(ic);
//			Log.w("TEST", "Item " + ic.getName() + " mit der");
//			Log.w("TEST", "Kategorie " + ic.getCategory().getName());
//		} catch (DatabaseException e) {
//			Log.w("TEST", "Das ging schief");
//			e.printStackTrace();
//		}
		

		try {
			Item u = db.getItemByName("FUCK");
			
			Category ctmp = db.getCategory("Unizeugs");
			Log.w("TEST", "CAT Name: " + ctmp.getName() + ", ID: " + ctmp.getId());
			
			u = new Item(u.getId(), u.getName(), ctmp, u.getImageHash(), false,	u.getIndependentItem(), u.getAttribute(), new ArrayList<String>());
			db.updateItem(u);
			Log.w("TEST", "Item FUCK geupdatet " + u.getCategory().getId());
		} catch (DatabaseException e) {
			Log.w("TEST", "Da ging was schief");
		}
		
		try {
			List<Item> items = db.getItems();
			
			for(int j = 0; j < items.size(); j++){
				Item tmp = items.get(j);
				Log.w("TEST", "Item: " + tmp.getName());
				Log.w("TEST", "Itemcat: " + tmp.getCategory());
			}
			
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		
		
		try {
			List<Category> c = db.getCategories();

			for(int j = 0; j < c.size(); j++) {
				Category tmp = c.get(j);
				Log.w("TEST", "Kategorie: " + tmp.getName());
			}
			
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			Log.w("TEST", "Fehler beim Aufrufen des Items");
			e.printStackTrace();
		}
		
		
		try {
			Item fu = db.getItemByName("FUCK");
			Log.w("TEST", "Item: " + fu);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		
//		de.uniulm.bagception.bundlemessageprotocol.entities.Activity a = new de.uniulm.bagception.bundlemessageprotocol.entities.Activity();
//		Location location = new Location();
//		location.setName("Scheißhaus");
//		location.setLat((float) 1.23456);
//		location.setLon((float) 9.87654);
//		
//		try {
//			db.addLocation(location);
//			Log.w("TEST", "Localtion " + location.getName() + " hinzugefügt");
//		} catch (DatabaseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		a.setName("Scheißen");
//		a.setLocation(location);
//		
//		try {
//			db.addActivity(a);
//			Log.w("TEST", "Aktivität " + a.getName() + " hinzugefügt");
//		} catch (DatabaseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		try {
//			db.deleteActivity(a);
//			Log.w("TEST", "Aktivität " + a.getName() + " wurde gelöscht");
//		} catch (DatabaseException e) {
//			e.printStackTrace();
//		}
//		
//		try {
//			List<de.uniulm.bagception.bundlemessageprotocol.entities.Activity> act = db.getActivities();
//			Log.w("TEST", "Anzahl Aktivitäten: " + act.size());
//			
//			de.uniulm.bagception.bundlemessageprotocol.entities.Activity a2 = new de.uniulm.bagception.bundlemessageprotocol.entities.Activity();
//			a2 = act.get(1);
//			
////			List<Location> loc = db.getLocations();
////			
////			Location l = loc.get(0);
//			
//			Log.w("TEST", "Aktivität " + a2.getName());// + " hat den Ort " + a2.getLocation().getName());
//		} catch (DatabaseException e) {
//			e.printStackTrace();
//		}
		
		
/*****************************************************************************************************************************************************
 * 
 *****************************************************************************************************************************************************/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.database_test, menu);
		return true;
	}

}
