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
		
		String hose = "Andere Hose";
		String shirt = "Zweites T-Shirt";
		ArrayList<String> tagIds = new ArrayList<String>();
		tagIds.add("123456789");
		
		// Insert to database
		
		try {
			db.addCategory(cat);
			Log.w("TEST", "Kategorie " + hose + " hinzugefügt");
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		
		Item i = new Item("Blablabla");
		Log.w("TEST", "Item erfolgreich erzeugt");
		
		try {
			db.addItem(i);
			Log.w("TEST", "Item " + i.getName() + " hinzugefügt");
			
		} catch (DatabaseException e) {
			Log.w("TEST", "Das ging schief");
			e.printStackTrace();
		}
		
		
		
		
		Item ic = new Item("CatItem", cat);
		Log.w("TEST", "Item CatItem hinzugefügt");
		
		try {
			db.addItem(ic);
			Log.w("TEST", "Item " + ic.getName() + " mit der");
			Log.w("TEST", "Kategorie " + ic.getCategory().getName());
			Log.w("TEST", "Anzahl Items: " + db.getItems().size());
		} catch (DatabaseException e) {
			Log.w("TEST", "Das ging schief");
			e.printStackTrace();
		}
		
		try {
			Item u = db.getItemByName("Blablabla");
			u = new Item("Blablabla2");
			db.updateItem(u);
			
			Log.w("TEST", "Anzahl Items nach update: " + db.getItems().size());
		} catch (DatabaseException e) {
			Log.w("TEST", "Da ging was schief");
		}
		
		try {
			db.getItems();
		}
		
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
