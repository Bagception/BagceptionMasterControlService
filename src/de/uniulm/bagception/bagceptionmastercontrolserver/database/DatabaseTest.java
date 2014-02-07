package de.uniulm.bagception.bagceptionmastercontrolserver.database;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
		
		
		
		try {
			db.addCategory(cat);
			db.addCategory(cat2);
		} catch (DatabaseException e){
			e.printStackTrace();
		}
		
		
		// Vollständiges Item
		String iName = "SuperItem";
		// Category cat2
		// ImageHash = 0
		boolean isActivityIndependent = true;
		boolean isIndependentItem = false;
		ItemAttribute iAttributes = new ItemAttribute("25", "Sonne", "Day");
		String[] iTagIDs = new String[] {"123456789", "987654321"};
		String tag_id = "987654321";
		Category iCategory = null;
		try {
			iCategory = db.getCategory("Unizeugs");
		} catch (DatabaseException e1) {
			e1.printStackTrace();
		}
		
		Item superItem = new Item(-1, iName, iCategory, 0, isActivityIndependent, isIndependentItem, iAttributes, iTagIDs);
//		Log.w("TEST", "SuperItem: " + superItem);
		
		
		Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		
		try{
			db.addItem(superItem);
			Log.w("TEST", "Item erfolgreich hinzugefügt");
		} catch(DatabaseException e){
			e.printStackTrace();
		}
		
		long tagid;
		try {
			tagid = db.getItemId("123456789");
			Log.w("TEST", "TagID: " + tagid);
			
			long id = db.getItem(tagid).getId();
			Log.w("TEST", "ItemID: " + id);
			
			db.addImage(id, bmp);
			Log.w("TEST", "Bild erfolgreich hinzugefügt");
			
			Bitmap b = db.getImage(id);
			Log.w("TEST", "Bitmap: " + b);
			
			
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
