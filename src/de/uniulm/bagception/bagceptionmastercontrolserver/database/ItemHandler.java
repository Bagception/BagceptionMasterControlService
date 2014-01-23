package de.uniulm.bagception.bagceptionmastercontrolserver.database;

import java.util.ArrayList;

import de.uniulm.bagception.bagceptionmastercontrolserver.R;
import de.uniulm.bagception.bagceptionmastercontrolserver.R.layout;
import de.uniulm.bagception.bagceptionmastercontrolserver.R.menu;
import de.uniulm.bagception.bagceptionmastercontrolserver.database.BagceptionContract.*;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.BaseColumns;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;

public class ItemHandler extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_database_handler);
		
//		Log.w("Top: ", "Ich leg jetzt das Bundle an");
		
		String tagid = "123456789";
		
		Bundle b = new Bundle();
		b.putString("name", "Handtuch");
		b.putString("category", null);
		b.putString("tagid", tagid);
//		
//		Log.w("Top: ", "Jetzt kommt das Intent ran");
//		Intent i = new Intent(this, DatabaseHandler.class);
//		i.putExtras(b);
//		startActivity(i);
//		
//		Log.w("Top: ", "Ich bin in der richtigen Activity");
//		Bundle b = getIntent().getExtras();
		
		onActionCreateItem(b);
//		
//		onActionQueryItem(tagid);
		
//		b.remove("name");
//		b.putString("name", "Großes Handtuch");
//		onActionUpdateItem(b);
//		
//		onActionQueryItem(tagid);
//		Cursor del = onActionQueryItem(null);
//		onActionDeleteItem(del);
//		onActionGetItems();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.database_handler, menu);
		return true;
	}
	
	public void onActionCreateItem(Bundle savedInstanceState) {
		
		//Log.w("Top", "Ich bin in der Insert Methode");
		
			  String name = savedInstanceState.getString("name");
			  String category = savedInstanceState.getString("category", null);
			  String tagid = savedInstanceState.getString("tagid");
			  String activityIndependent = savedInstanceState.getString("activityIndependent", "false");
			  String visibility = savedInstanceState.getString("visibility", "privat");
		      String hasPic = savedInstanceState.getString("photo", null);
		      
		      //Context ctx = getActivity();
		      //String somePath = ctx.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
		      
		      ContentValues values = new ContentValues();
		      values.put(Items.NAME, name);
		      values.put(Items.CATEGORY, category);
		      values.put(Items.TAGID, tagid);
		      values.put(Items.ACTIVITY_IND, activityIndependent);
		      values.put(Items.VISIBILITY, visibility);
		      
		      
		      ContentResolver resolver = getContentResolver();
		      
		      Uri result = resolver.insert(Items.CONTENT_URI, values);
		      
		      if(result == null) {
		    	  //Log.e("Error: ", "Item kann nicht angelegt werden");
		    	  return;
		      }
		      
		      if (hasPic != null) {
		    	  long itemId = ContentUris.parseId(result);
		    	  insertPhoto(values, resolver, hasPic, itemId);
		      }
		      
		      Log.w("Top", "Item " + name + " wurde erstellt.");
		
   }
	   
	   
	   public void insertPhoto(ContentValues values, ContentResolver resolver, String hasPic, long itemId) {

		   values.clear();
		   values.put(Photos._DATA, hasPic);
		   values.put(Photos.ITEMS_ID, itemId);
		   
		   Uri photoResult = getContentResolver().insert(Photos.CONTENT_URI, values);
		   
		   if (photoResult == null) {
			   Log.e("Error: ", "Foto kann nicht gespeichert werden");
		   }
	}

	public void onActionUpdateItem(Bundle savedInstanceState) {
		
		Log.w("Top", "Bin jetzt in der Updatemethode");

//		long itemId = savedInstanceState.getLong(BaseColumns._ID, -1);
		
		Cursor bundle = onActionQueryItem(savedInstanceState.getString("tagid"));
		
		long itemId = bundle.getLong(0);
		
		Log.w("Top", "ID: " + itemId);
		
	      if (itemId == -1) {
	         throw new IllegalStateException("Cannot update record with itemId == -1");
	      }
	      
	      Log.w("Top", "Hole jetzt die Daten aus dem Bundle");
	      
//	      String name = savedInstanceState.getString(Items.NAME);
	      String name = savedInstanceState.getString("name");
	      String category = savedInstanceState.getString(Items.CATEGORY, null);
	      String tagid = savedInstanceState.getString(Items.TAGID);
	      String activityIndependent = savedInstanceState.getString(Items.ACTIVITY_IND, null);
	      String visibility = savedInstanceState.getString(Items.VISIBILITY, "privat");
	      
	      ContentValues values = new ContentValues();
	      values.put(Items.NAME, name);
	      values.put(Items.CATEGORY, category);
	      values.put(Items.TAGID, tagid);
	      values.put(Items.ACTIVITY_IND, activityIndependent);
	      values.put(Items.VISIBILITY, visibility);
	      
	      ContentResolver resolver = getContentResolver();
	      Uri updateUri = ContentUris.withAppendedId(Items.CONTENT_URI, itemId);
	      
	      long resultCount = resolver.update(updateUri, values, null, null);
	      
	      Log.w("Top", "Item wurde erfolgreich in " + name + " geändert");
	      
	      if (resultCount == 0) {
	         Log.e("Error: ", "Couldn't update item with id " + itemId);
	         return;
	      }
	      
	      
	      String photo = savedInstanceState.getString(Photos._DATA);
	      
	      if (photo != null) {
	    	  // always insert;
	    	  insertPhoto(values, resolver, photo, itemId);
	      }
	   }
	
	
		public void onActionDeleteItem(Cursor savedInstanceState) {
		   
		   	Log.w("Top", "Löschen Methode");
		   
		      //long itemId = savedInstanceState.getLong(BaseColumns._ID, -1);
		   	//long itemId = savedInstanceState.getLong("_id");
		   	long itemId = savedInstanceState.getLong(0);
		      
		    Log.w("Top", "ItemId bekommen");
		      
		      if (itemId == -1) {
		    	  Log.w("Top", "itemdId nicht vorhanden");
		         throw new IllegalStateException("Cannot delete record with itemId == -1");
		      }
		      
		      Uri delUri = ContentUris.withAppendedId(Items.CONTENT_URI, itemId);
		      
		      Log.w("Top", "Uri bekommen");
		      
		      long resultCount = getContentResolver().delete(delUri, null, null);
		      
		      if (resultCount == 0) {
		         Log.e("Error: ", "Couldn't delete item with id " + itemId);
		         return;
		      }
		      
		      Log.w("Top", "Item gelöscht");
	   }
	   
	   // TODO
	   public Cursor onActionQueryItem(String tagId) {

		   //Log.w("Top ", "Bin in der Query Methode");
		   
	      //String itemId = "10";
		  //String itemId = tagId.getString("itemId", null);
		   Cursor result = null;
		  
		  if (tagId != null) {
		      String selection = BagceptionContract.SELECTION_TAGID_BASED; // BaseColumns._ID
		                                                               // + " = ? "
		      String[] selectionArgs = {tagId};
		      ContentResolver resolver = getContentResolver();
		     
		    //Log.w("Top ", "Erstelle jetzt Cursor");  
		      
		      result = resolver.query(
		            Items.CONTENT_URI,          // die URI
		            Items.PROJECTION_ALL,       // optionale Angabe der gewünschten Spalten
		            selection,                  // optionale WHERE Klausel (ohne Keyword)
		            selectionArgs,              // optionale Wildcard Ersetzungen
		            //Items.SORT_ORDER_DEFAULT);  // optionale ORDER BY Klausel (ohne Keyword)
		            null);
		      
		      //Log.w("Top ", "Cursor wurde erstellt");
		      
		      if (result != null && result.moveToFirst()) {
		         // int idx = c.getColumnIndex(Items.NAME);
		         String name = result.getString(1);
		         String tagid = result.getString(2);
		         
		         Log.w("Top ", name);
		         Log.w("Top ", tagid);
		         
		         return result;
		      }		      
		      
		      if(result == null) {
		    	  return null;
		      }
		  }
		  
		  return null;	    
	   }
	   
		public Cursor onActionGetItems() {
			
			Log.w("Top", "Richtige Methode");
			Cursor result = null;
			ContentResolver resolver = getContentResolver();
			
			result = resolver.query(Items.CONTENT_URI, Items.PROJECTION_ALL, null, null, null);
			
			if (result != null && result.moveToFirst()) {
				
			Log.w("Top", "If Schleife xD");	
				do {
					
					long id = result.getLong(0);
					String name = result.getString(1);
					String tagid = result.getString(2);
					Log.w("Top", "ID: " + id);
					Log.w("Top", "Name: " + name);
					Log.w("Top", "TagId: " + tagid);
				} while (result.moveToNext());

			}
			
			
			
			return result;
		}


}
