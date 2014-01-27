package de.uniulm.bagception.bagceptionmastercontrolserver.database;

import android.util.Log;
import de.uniulm.bagception.bundlemessageprotocol.entities.Activity;
import de.uniulm.bagception.bundlemessageprotocol.entities.Category;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.bundlemessageprotocol.entities.Location;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.AdministrationCommandProcessor;

public class AdministrationDatabaseAdapter extends AdministrationCommandProcessor{

	
	
	@Override
	public void onItemAdd(Item a) {
		Log.d("db_admin_adapter", "onItemAdd");
		
	}

	@Override
	public void onItemDel(Item a) {
		Log.d("db_admin_adapter", "onItemDel");		
	}

	@Override
	public void onItemEdit(Item toEdit, Item editValues) {
		Log.d("db_admin_adapter", "onItemEdit");		
	}

	@Override
	public void onItemList() {
		Log.d("db_admin_adapter", "onItemList");		
	}

	@Override
	public void onActivityAdd(Activity a) {
		Log.d("db_admin_adapter", "onActivityAdd");		
	}

	@Override
	public void onActivityDel(Activity a) {
		Log.d("db_admin_adapter", "onActivityDel");
		
	}

	@Override
	public void onActivityEdit(Activity toEdit, Activity editValues) {
		Log.d("db_admin_adapter", "onActivityEdit");		
	}

	@Override
	public void onActivityList() {
		Log.d("db_admin_adapter", "onActivityList");		
	}

	@Override
	public void onLocationAdd(Location a) {
		Log.d("db_admin_adapter", "onLocationAdd");		
	}

	@Override
	public void onLocationDel(Location a) {
		Log.d("db_admin_adapter", "onLocationDel");		
	}

	@Override
	public void onLocationEdit(Location toEdit, Location editValues) {
		Log.d("db_admin_adapter", "onLocationEdit");		
	}

	@Override
	public void onLocationList() {
		Log.d("db_admin_adapter", "onLocationList");		
	}

	@Override
	public void onCategoryAdd(Category a) {
		Log.d("db_admin_adapter", "onCategoryAdd");		
	}

	@Override
	public void onCategoryDel(Category a) {
		Log.d("db_admin_adapter", "onCategoryDel");		
	}

	@Override
	public void onCategoryEdit(Category toEdit, Category editValues) {
		Log.d("db_admin_adapter", "onCategoryEdit");		
	}

	@Override
	public void onCategoryList() {
		Log.d("db_admin_adapter", "onCategoryList");		
	}

	

}
