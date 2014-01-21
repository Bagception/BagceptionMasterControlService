package de.uniulm.bagception.bagceptionmastercontrolserver.database;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.RemoteException;
import android.util.Log;

import de.uniulm.bagception.bagceptionmastercontrolserver.database.BagceptionContract.Items;
import de.uniulm.bagception.bagceptionmastercontrolserver.database.BagceptionContract.Photos;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;

public class DatabaseConnector {
	
	private static HashMap<String,Item> items = new HashMap<String, Item>();
	public static Item getItemByName(String name){
		return items.get(name);
	}
	
	public static final String ITEM_HOSE = "Hose";
	public static final String ITEM_REGENJACKE = "Regenjacke";
	public static final String ITEM_SHIRT = "T-Shirt";
	public static final String ITEM_TRINKEN = "Trinken";
	
	static{
		
		{
			ArrayList<String> ids = new ArrayList<String>();
			ids.add("32 40 00 01 0E 30 00 E2 00 34 12 DC 03 01 18 28 24 27 37 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00");
			String name = "Regenjacke";
			Item ip =  new Item(name,"",ids);
			
			items.put(name, ip);
		}
		
		{
			ArrayList<String> ids = new ArrayList<String>();
			ids.add("32 40 00 01 0E 30 00 E2 00 34 12 DC 03 01 18 28 24 27 40 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00");
			String name = "T-Shirt";
			items.put(name, new Item(name,"",ids));
		}

		{
			ArrayList<String> ids = new ArrayList<String>();
			ids.add("32 40 00 01 0E 30 00 E2 00 34 12 DC 03 01 18 28 24 27 39 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00");
			String name = "Hose";
			items.put(name, new Item(name,"",ids));
		}
		
		{
			ArrayList<String> ids = new ArrayList<String>();
			ids.add("32 40 00 01 0E 30 00 E2 00 34 12 DC 03 01 18 28 24 27 38 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00");
			String name = "Trinken";
			items.put(name, new Item(name,"",ids));
		}		
		
	}
	
	public static Item getItem(String id){
		if (id.equals("32 40 00 01 0E 30 00 E2 00 34 12 DC 03 01 18 28 24 27 37 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00")){
			return DatabaseConnector.getItemByName(DatabaseConnector.ITEM_REGENJACKE);
		}
		if (id.equals("32 40 00 01 0E 30 00 E2 00 34 12 DC 03 01 18 28 24 27 40 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00")){
			return DatabaseConnector.getItemByName(DatabaseConnector.ITEM_SHIRT);
		} 
		if (id.equals("32 40 00 01 0E 30 00 E2 00 34 12 DC 03 01 18 28 24 27 39 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00")){
			return DatabaseConnector.getItemByName(DatabaseConnector.ITEM_HOSE);
		} 
		if (id.equals("32 40 00 01 0E 30 00 E2 00 34 12 DC 03 01 18 28 24 27 38 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00")){
			return DatabaseConnector.getItemByName(DatabaseConnector.ITEM_TRINKEN);
		} 
		
		return null;
	}
}




/**
	
	private void dummyApplyBatch() {

      String item = "Fussball";
      String visibility = "public";
      boolean hasPic = true;
      //Context ctx = getActivity();
      //String somePath = ctx.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
      
      ArrayList<ContentProviderOperation> ops = 
            new ArrayList<ContentProviderOperation>();
      ops.add(ContentProviderOperation.newInsert(Items.CONTENT_URI)
            .withValue(Items.NAME, item)
            .withValue(Items.VISIBILITY, visibility)
            .build());
      if (hasPic) {
         ops.add(ContentProviderOperation.newInsert(Photos.CONTENT_URI)
               .withValue(Photos._DATA, somePath)
               .withValueBackReference(Photos.ITEMS_ID, 0).
               build());
      }
      try {
         ContentResolver resolver = getActivity().getContentResolver();
         resolver.applyBatch(BagceptionContract.AUTHORITY, ops);
      } catch (OperationApplicationException e) {
         Log.e("Error: ", "cannot apply batch: " + e.getLocalizedMessage(), e);
      } catch (RemoteException e) {
         Log.e("Error: ", "cannot apply batch: " + e.getLocalizedMessage(), e);
      }

      // EventBus.getDefault().post(whatever);

   }
   
   
   private long itemUpdate() {

      String itemId = "10";
      String selection = BagceptionContract.SELECTION_ID_BASED; // BaseColumns._ID
                                                               // + " = ? "
      String[] selectionArgs = {itemId};
      ContentResolver resolver = getActivity().getContentResolver();
      ContentValues values = new ContentValues();
      values.put(Items.NAME, "Fussball");
      values.put(Items.VISIBILITY, "public");
      long updateCount = 
            resolver.update(Items.CONTENT_URI, values, selection, selectionArgs);
      
      
      return updateCount;
   }


   private void dummyQuery() {

      String itemId = "10";
      String selection = BagceptionContract.SELECTION_ID_BASED; // BaseColumns._ID
                                                               // + " = ? "
      String[] selectionArgs = {itemId};
      ContentResolver resolver = getActivity().getContentResolver();
      Cursor c = resolver.query(
            Items.CONTENT_URI,          // die URI
            Items.PROJECTION_ALL,       // optionale Angabe der gewünschten Spalten
            selection,                  // optionale WHERE Klausel (ohne Keyword)
            selectionArgs,              // optionale Wildcard Ersetzungen
            Items.SORT_ORDER_DEFAULT);  // optionale ORDER BY Klausel (ohne Keyword)
      
      if (c != null && c.moveToFirst()) {
         // int idx = c.getColumnIndex(Items.NAME);
         String name = c.getString(1);
         String tagid = c.getString(2);
         ...
      }
      
   }
   */
