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
import android.util.Log;
import android.view.Menu;

public class ItemHandler extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_database_handler);
		
		Log.w("Top: ", "Ich leg jetzt das Bundle an");
		
		Bundle b = new Bundle();
		b.putString("name", "Handtuch");
		b.putString("category", null);
		b.putString("tagid", "123456789");
//		
//		Log.w("Top: ", "Jetzt kommt das Intent ran");
//		Intent i = new Intent(this, DatabaseHandler.class);
//		i.putExtras(b);
//		startActivity(i);
//		
//		Log.w("Top: ", "Ich bin in der richtigen Activity");
//		Bundle b = getIntent().getExtras();
		// onActionCreateItem(b);
		onActionQueryItem("123456789");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.database_handler, menu);
		return true;
	}
	
	public void onActionCreateItem(Bundle savedInstanceState) {
		
		Log.w("Top", "Ich bin in der Insert Methode");
		
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
		    	  Log.e("Error: ", "Item kann nicht angelegt werden");
		    	  return;
		      }
		      
		      if (hasPic != null) {
		    	  long itemId = ContentUris.parseId(result);
		    	  insertPhoto(values, resolver, hasPic, itemId);
		      }
		      
		      Log.w("Top: ", "Alles gefunzt!");
		
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

		long itemId = savedInstanceState.getLong(BaseColumns._ID, -1);
		
	      if (itemId == -1) {
	         throw new IllegalStateException("Cannot update record with itemId == -1");
	      }
	      
	      String name = savedInstanceState.getString(Items.NAME);
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

	   public void onActionDeleteItem(Bundle savedInstanceState) {
		   
		      long itemId = savedInstanceState.getLong(BaseColumns._ID, -1);
		      
		      if (itemId == -1) {
		         throw new IllegalStateException("Cannot delete record with itemId == -1");
		      }
		      
		      Uri delUri = ContentUris.withAppendedId(Items.CONTENT_URI, itemId);
		      
		      long resultCount = getContentResolver().delete(delUri, null, null);
		      
		      if (resultCount == 0) {
		         Log.e("Error: ", "Couldn't delete item with id " + itemId);
		         return;
		      }
	   }
	   
	   // TODO
	   public void onActionQueryItem(String tagId) {

		   Log.w("Top ", "Bin in der Query Methode");
		   
	      //String itemId = "10";
		  //String itemId = tagId.getString("itemId", null);
		  
		  if (tagId != null) {
		      String selection = BagceptionContract.SELECTION_TAGID_BASED; // BaseColumns._ID
		                                                               // + " = ? "
		      String[] selectionArgs = {tagId};
		      ContentResolver resolver = getContentResolver();
		     
		    Log.w("Top ", "Erstelle jetzt Cursor");  
		      
		      Cursor c = resolver.query(
		            Items.CONTENT_URI,          // die URI
		            Items.PROJECTION_ALL,       // optionale Angabe der gewünschten Spalten
		            selection,                  // optionale WHERE Klausel (ohne Keyword)
		            selectionArgs,              // optionale Wildcard Ersetzungen
		            //Items.SORT_ORDER_DEFAULT);  // optionale ORDER BY Klausel (ohne Keyword)
		            null);
		      
		      Log.w("Top ", "Cursor wurde erstellt");
		      
		      if (c != null && c.moveToFirst()) {
		         // int idx = c.getColumnIndex(Items.NAME);
		         String name = c.getString(1);
		         String tagid = c.getString(2);
		         
		         Log.w("Top ", name);
		         Log.w("Top ", tagid);
		      }		      
		  }
	      
	   }


}
