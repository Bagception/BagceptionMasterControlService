package de.uniulm.bagception.bagceptionmastercontrolserver.database;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import de.uniulm.bagception.bagceptionmastercontrolserver.R;
import de.uniulm.bagception.bundlemessageprotocol.entities.Activity;
import de.uniulm.bagception.bundlemessageprotocol.entities.Category;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.bundlemessageprotocol.entities.ItemAttribute;
import de.uniulm.bagception.bundlemessageprotocol.entities.Location;

public class DatabaseTest extends android.app.Activity {
	
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
		
		
		
//		try {
//			db.addCategory(cat);
//			db.addCategory(cat2);
//		} catch (DatabaseException e){
//			e.printStackTrace();
//		}
		
		
		
//		String hose = "Andere Hose";
//		String shirt = "Zweites T-Shirt";
//		ArrayList<String> tagIds = new ArrayList<String>();
//		tagIds.add("123456789");
		
		
		// Vollständiges Item
		String iName = "SuperItem";
		// Category cat2
		// ImageHash = 0
		boolean isActivityIndependent = true;
		boolean isIndependentItem = true;
		ItemAttribute iAttributes = new ItemAttribute("25", "Sonne", "Day");
		String[] iTagIDs = new String[] {"123456789"};
		Category iCategory = null;
		try {
			iCategory = db.getCategory("Unizeugs");
		} catch (DatabaseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Item superItem = new Item(-1, iName, iCategory, 0, isActivityIndependent, isIndependentItem, iAttributes, iTagIDs);
		//Log.w("TEST", "SuperItem: " + superItem);
		
		
//		try {
//			List<Category> tm = db.getCategories();
//			
//			for(int t = 0; t < tm.size(); t++) {
//				Category ca = tm.get(t);
//				Log.w("TEST", "Kategorie: " + ca);
//			}
//		} catch (DatabaseException e) {
//			e.printStackTrace();
//		}
		
		
//		try {
//			List<Item> il = db.getItems();
//			Log.w("TEST", "Item: " + il);
//			
//			for(int i = 0; i < il.size(); i++){
//				Item itmp = il.get(i);
//				Log.w("TEST", "Item: " + itmp.getName());
//			}
//		} catch (DatabaseException e) {
//			e.printStackTrace();
//		}
		
		// Insert to database
//		try {
//			db.addItem(superItem);
//			Log.w("TEST", "Einfügen SuperItem erfolgreich");
//		} catch (DatabaseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		// Delete SuperItem
//		try {
//			db.deleteItem(superItem);
//			Log.w("TEST", "Löschen erfolgreich");
//		} catch (DatabaseException e) {
//			e.printStackTrace();
//		}
		
		
		// Get SuperItem
//		try {
//			Item tSuperItem = db.getItemByName(iName);
//			Log.w("TEST", "Name: " + tSuperItem.getName());
//			Log.w("TEST", "Kategorie: " + tSuperItem.getCategory());
//			
//			long iID = tSuperItem.getId();
//			Log.w("TEST", "Item ID: " + iID);
//			
//			boolean ids = db.getIndependentItem(iID);
//			Log.w("TEST", "IndependentItem: " + ids);
//			
//			boolean cids = db.getContextItem(iID);
//			Log.w("TEST", "ContextItem: " + cids);
//			
//			List<String> tagids = db.getTagId(iID);
//			Log.w("TEST", "TagID: " + tagids);
//			
//			ItemAttribute att = db.getItemAttribute(iID);
//			Log.w("TEST", "Attribute: " + att.getItemId() + att.getTemperature() + att.getWeather() + att.getLightness());
//			
////			Log.w("TEST", "Anzahl Attr.: " + db.getItemAttributes().size());
//			
//		} catch (DatabaseException e) {
//			e.printStackTrace();
//		}
		
		Location loc = new Location("Uni", (float) 0, (float) 0, 0);
		Log.w("TEST", "Loc: " + loc);
		
		try {
			db.addLocation(loc);
			Log.w("TEST", "Location hinzugefügt");
		} catch (DatabaseException e){
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
