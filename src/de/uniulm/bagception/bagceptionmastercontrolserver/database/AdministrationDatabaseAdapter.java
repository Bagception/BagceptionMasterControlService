package de.uniulm.bagception.bagceptionmastercontrolserver.database;

import android.util.Log;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage.BUNDLE_MESSAGE;
import de.uniulm.bagception.bundlemessageprotocol.entities.Activity;
import de.uniulm.bagception.bundlemessageprotocol.entities.Category;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.bundlemessageprotocol.entities.Location;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.ActivityCommand;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.AdministrationCommand;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.AdministrationCommandProcessor;
import de.uniulm.bagception.mcs.services.MasterControlServer;

public class AdministrationDatabaseAdapter extends AdministrationCommandProcessor{
	private final MasterControlServer mcs;
	public AdministrationDatabaseAdapter(MasterControlServer msc) {
		this.mcs = msc;
	}
	
	//demo response method, just to demonstrate the communication back
	private void sendActivityResponse(AdministrationCommand<Activity> req){
		mcs.sendToRemote(BUNDLE_MESSAGE.ADMINISTRATION_COMMAND, ActivityCommand.reply(req, true, ""));
	}
	
	@Override
	public void onItemAdd(Item a,AdministrationCommand<Item> i) {
		Log.d("db_admin_adapter", "onItemAdd");
		
	}

	@Override
	public void onItemDel(Item a,AdministrationCommand<Item> i) {
		Log.d("db_admin_adapter", "onItemDel");		
	}

	@Override
	public void onItemEdit(Item toEdit, Item editValues,AdministrationCommand<Item> i) {
		Log.d("db_admin_adapter", "onItemEdit");		
	}

	@Override
	public void onItemList(AdministrationCommand<Item> i) {
		Log.d("db_admin_adapter", "onItemList");		
	}

	@Override
	public void onActivityAdd(Activity a,AdministrationCommand<Activity> aa) {
		Log.d("db_admin_adapter", "onActivityAdd");		
	}

	@Override
	public void onActivityDel(Activity a,AdministrationCommand<Activity> aa) {
		Log.d("db_admin_adapter", "onActivityDel");
		
	}

	@Override
	public void onActivityEdit(Activity toEdit, Activity editValues,AdministrationCommand<Activity> a) {
		Log.d("db_admin_adapter", "onActivityEdit");		
	}

	@Override
	public void onActivityList(AdministrationCommand<Activity> a) {
		Log.d("db_admin_adapter", "onActivityList");		
	}

	@Override
	public void onLocationAdd(Location a,AdministrationCommand<Location> aa) {
		Log.d("db_admin_adapter", "onLocationAdd");		
	}

	@Override
	public void onLocationDel(Location a,AdministrationCommand<Location> aa) {
		Log.d("db_admin_adapter", "onLocationDel");		
	}

	@Override
	public void onLocationEdit(Location toEdit, Location editValues,AdministrationCommand<Location> a) {
		Log.d("db_admin_adapter", "onLocationEdit");		
	}

	@Override
	public void onLocationList(AdministrationCommand<Location> a) {
		Log.d("db_admin_adapter", "onLocationList");		
	}

	@Override
	public void onCategoryAdd(Category a,AdministrationCommand<Category> c) {
		Log.d("db_admin_adapter", "onCategoryAdd");		
	}

	@Override
	public void onCategoryDel(Category a,AdministrationCommand<Category> c) {
		Log.d("db_admin_adapter", "onCategoryDel");		
	}

	@Override
	public void onCategoryEdit(Category toEdit, Category editValues,AdministrationCommand<Category> c) {
		Log.d("db_admin_adapter", "onCategoryEdit");		
	}

	@Override
	public void onCategoryList(AdministrationCommand<Category> c) {
		Log.d("db_admin_adapter", "onCategoryList");		
	}

	

}
