package de.uniulm.bagception.bagceptionmastercontrolserver.database;

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
		// Create Item
		Item i = new Item();
		Category cat = new Category("Anziehsachen");
		i.setName("Hose");
		i.setCategory(cat);
		
		Item i2 = new Item();
		i2.setName("T-Shirt");
		i2.setCategory(cat);
		
		// Insert to database
//		try {
//			db.addItem(i, "123456789");
//			Log.w("TEST", "Item " + i.getName() + " hinzugefügt");
//			
//			db.addItem(i2, "987654321");
//			Log.w("TEST", "Item " + i2.getName() + " hinzugefügt");
//		} catch (DatabaseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		try {
//			db.addCategory(cat);
//			Log.w("TEST", "Category " + cat.getName() + " hinzugefügt");
//		} catch (DatabaseException e) {
//			e.printStackTrace();
//		}
//		
//		try {
//			List<Item> j = db.getItems();
//			Item xyz = j.get(0);
//			Item zyx = j.get(1);
//			Log.w("TEST", "Alle Items: " + xyz.getName() + ", " + zyx.getName());
//		} catch (DatabaseException e) {
//			// TODO Auto-generated catch block
//			Log.w("TEST", "Fehler beim Aufrufen des Items");
//			e.printStackTrace();
//		}
//		
//		try {
//			db.deleteItem(i);
//			Log.w("TEST", i.getName() + " wurde gelöscht");
//		} catch (DatabaseException e) {
//			e.printStackTrace();
//		}
//		
//		try {
//			List<Item> j = db.getItems();
//			Item xyz = j.get(0);
//			Log.w("TEST", "Alle Items: " + xyz.getName());
//		} catch (DatabaseException e) {
//			// TODO Auto-generated catch block
//			Log.w("TEST", "Fehler beim Aufrufen des Items");
//			e.printStackTrace();
//		}
//		
//		try {
//			List<Category> c = db.getCategories();
//			int count = c.size();
//			Log.w("TEST", "Anzahl TagId: " + count);
//		} catch (DatabaseException e) {
//			// TODO Auto-generated catch block
//			Log.w("TEST", "Fehler beim Aufrufen des Items");
//			e.printStackTrace();
//		}
		
		
		
		
		
		
		de.uniulm.bagception.bundlemessageprotocol.entities.Activity a = new de.uniulm.bagception.bundlemessageprotocol.entities.Activity();
		Location location = new Location();
		location.setName("Scheißhaus");
		location.setLat((float) 1.23456);
		location.setLon((float) 9.87654);
		
//		try {
//			db.addLocation(location);
//			Log.w("TEST", "Localtion " + location.getName() + " hinzugefügt");
//		} catch (DatabaseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		a.setName("Scheißen");
		a.setLocation(location);
		
//		try {
//			db.addActivity(a);
//			Log.w("TEST", "Aktivität " + a.getName() + " hinzugefügt");
//		} catch (DatabaseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		try {
//			db.deleteActivity(a);
//			Log.w("TEST", "Aktivität " + a.getName() + " wurde gelöscht");
//		} catch (DatabaseException e) {
//			e.printStackTrace();
//		}
		
		try {
			List<de.uniulm.bagception.bundlemessageprotocol.entities.Activity> act = db.getActivities();
			Log.w("TEST", "Anzahl Aktivitäten: " + act.size());
			
			de.uniulm.bagception.bundlemessageprotocol.entities.Activity a2 = new de.uniulm.bagception.bundlemessageprotocol.entities.Activity();
			a2 = act.get(1);
			
//			List<Location> loc = db.getLocations();
//			
//			Location l = loc.get(0);
			
			Log.w("TEST", "Aktivität " + a2.getName());// + " hat den Ort " + a2.getLocation().getName());
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		
		
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
