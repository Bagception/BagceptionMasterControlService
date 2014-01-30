package de.uniulm.bagception.bagceptionmastercontrolserver.database;

import java.util.List;

import android.util.Log;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage.BUNDLE_MESSAGE;
import de.uniulm.bagception.bundlemessageprotocol.entities.Activity;
import de.uniulm.bagception.bundlemessageprotocol.entities.Category;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.bundlemessageprotocol.entities.Location;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.ActivityCommand;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.AdministrationCommand;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.AdministrationCommandProcessor;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.CategoryCommand;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.ItemCommand;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.LocationCommand;
import de.uniulm.bagception.mcs.services.MasterControlServer;

public class AdministrationDatabaseAdapter extends AdministrationCommandProcessor{
	private final MasterControlServer mcs;
	private final DatabaseInterface dbi;
	public AdministrationDatabaseAdapter(MasterControlServer msc,DatabaseInterface dbi) {
		this.mcs = msc;
		this.dbi = dbi;
	}
	
	//demo response method, just to demonstrate the communication back
	private void sendActivityResponse(AdministrationCommand<Activity> req){
		mcs.sendToRemote(BUNDLE_MESSAGE.ADMINISTRATION_COMMAND, ActivityCommand.reply(req, false, ""));
		
	}
	
	@Override
	public void onItemAdd(Item a,AdministrationCommand<Item> i) {
		Log.d("db_admin_adapter", "onItemAdd");
		Log.d("db_admin_adapter", "item: "+a.getName());
		try {
			dbi.addItem(a);
			mcs.sendToRemote(BUNDLE_MESSAGE.ADMINISTRATION_COMMAND, ItemCommand.reply(i, true, ""));
		} catch (DatabaseException e) {
			mcs.sendToRemote(BUNDLE_MESSAGE.ADMINISTRATION_COMMAND, ItemCommand.reply(i, false, "unable to create item"));
		}
		

	}

	@Override
	public void onItemDel(Item a,AdministrationCommand<Item> i) {
		Log.d("db_admin_adapter", "onItemDel");
		try {
			dbi.deleteItem(a);
			mcs.sendToRemote(BUNDLE_MESSAGE.ADMINISTRATION_COMMAND, ItemCommand.reply(i, true, ""));
		} catch (DatabaseException e) {
			mcs.sendToRemote(BUNDLE_MESSAGE.ADMINISTRATION_COMMAND, ItemCommand.reply(i, false, "unable to delete item"));
		}
	}

	@Override
	public void onItemEdit(Item toEdit, Item editValues,AdministrationCommand<Item> i) {
		Log.d("db_admin_adapter", "onItemEdit");
		try {
			dbi.editItem(toEdit,editValues);
			mcs.sendToRemote(BUNDLE_MESSAGE.ADMINISTRATION_COMMAND, ItemCommand.reply(i, true, ""));
		} catch (DatabaseException e) {
			mcs.sendToRemote(BUNDLE_MESSAGE.ADMINISTRATION_COMMAND, ItemCommand.reply(i, false, "unable to edit item"));
		}
	}

	@Override
	public void onItemList(AdministrationCommand<Item> i) {
		Log.d("db_admin_adapter", "onItemList");		
		//try {
			//List<Item> items = dbi.getItems();
			//Item[] itemsArray = items.toArray(new Item[items.size()]);
			Item [] itemsArray = new Item[]{new Item("item1",null),new Item("item2",null),new Item("item3",null)};

			i.setPayloadObjects(itemsArray);
			mcs.sendToRemote(BUNDLE_MESSAGE.ADMINISTRATION_COMMAND, ItemCommand.reply(i, true, ""));
//		} catch (DatabaseException e) {
//			mcs.sendToRemote(BUNDLE_MESSAGE.ADMINISTRATION_COMMAND, ItemCommand.reply(i, false, "unable to list items"));
//		}
	}

	@Override
	public void onActivityAdd(Activity a,AdministrationCommand<Activity> aa) {
		Log.d("db_admin_adapter", "onActivityAdd");
		try {
			dbi.addActivity(a);
			mcs.sendToRemote(BUNDLE_MESSAGE.ADMINISTRATION_COMMAND, ActivityCommand.reply(aa, true, ""));
		} catch (DatabaseException e) {
			mcs.sendToRemote(BUNDLE_MESSAGE.ADMINISTRATION_COMMAND, ActivityCommand.reply(aa, false, "unable to create activity"));
		}
	}

	@Override
	public void onActivityDel(Activity a,AdministrationCommand<Activity> aa) {
		Log.d("db_admin_adapter", "onActivityDel");
		try {
			dbi.deleteActivity(a);
			mcs.sendToRemote(BUNDLE_MESSAGE.ADMINISTRATION_COMMAND, ActivityCommand.reply(aa, true, ""));
		} catch (DatabaseException e) {
			mcs.sendToRemote(BUNDLE_MESSAGE.ADMINISTRATION_COMMAND, ActivityCommand.reply(aa, false, "unable to delete activity"));
		}
	}

	@Override
	public void onActivityEdit(Activity toEdit, Activity editValues,AdministrationCommand<Activity> a) {
		Log.d("db_admin_adapter", "onActivityEdit");		
		try {
			dbi.editActivity(toEdit,editValues);
			mcs.sendToRemote(BUNDLE_MESSAGE.ADMINISTRATION_COMMAND, ActivityCommand.reply(a, true, ""));
		} catch (DatabaseException e) {
			mcs.sendToRemote(BUNDLE_MESSAGE.ADMINISTRATION_COMMAND, ActivityCommand.reply(a, false, "unable to edit activity"));
		}
	}

	@Override
	public void onActivityList(AdministrationCommand<Activity> a) {
		Log.d("db_admin_adapter", "onActivityList");	
		try {
			List<Activity> activities = dbi.getActivities();
			Activity[] itemsArray = activities.toArray(new Activity[activities.size()]);
			a.setPayloadObjects(itemsArray);
			mcs.sendToRemote(BUNDLE_MESSAGE.ADMINISTRATION_COMMAND, ActivityCommand.reply(a, true, ""));
		} catch (DatabaseException e) {
			mcs.sendToRemote(BUNDLE_MESSAGE.ADMINISTRATION_COMMAND, ActivityCommand.reply(a, false, "unable to list activities"));
		}
	}

	@Override
	public void onLocationAdd(Location a,AdministrationCommand<Location> aa) {
		Log.d("db_admin_adapter", "onLocationAdd");	
		try {
			dbi.addLocation(a);
			mcs.sendToRemote(BUNDLE_MESSAGE.ADMINISTRATION_COMMAND, LocationCommand.reply(aa, true, ""));
		} catch (DatabaseException e) {
			mcs.sendToRemote(BUNDLE_MESSAGE.ADMINISTRATION_COMMAND, LocationCommand.reply(aa, false, "unable to add location"));
		}
	}

	@Override
	public void onLocationDel(Location a,AdministrationCommand<Location> aa) {
		Log.d("db_admin_adapter", "onLocationDel");		
		try {
			dbi.deleteLocation(a);
			mcs.sendToRemote(BUNDLE_MESSAGE.ADMINISTRATION_COMMAND, LocationCommand.reply(aa, true, ""));
		} catch (DatabaseException e) {
			mcs.sendToRemote(BUNDLE_MESSAGE.ADMINISTRATION_COMMAND, LocationCommand.reply(aa, false, "unable to remove location"));
		}
	}

	@Override
	public void onLocationEdit(Location toEdit, Location editValues,AdministrationCommand<Location> a) {
		Log.d("db_admin_adapter", "onLocationEdit");
		try {
			dbi.editLocation(toEdit, editValues);
			mcs.sendToRemote(BUNDLE_MESSAGE.ADMINISTRATION_COMMAND, LocationCommand.reply(a, true, ""));
		} catch (DatabaseException e) {
			mcs.sendToRemote(BUNDLE_MESSAGE.ADMINISTRATION_COMMAND, LocationCommand.reply(a, false, "unable to edit location"));
		}
	}

	@Override
	public void onLocationList(AdministrationCommand<Location> a) {
		Log.d("db_admin_adapter", "onLocationList");
		try {
			List<Location> locations = dbi.getLocations();
			Location[] itemsArray = locations.toArray(new Location[locations.size()]);
			a.setPayloadObjects(itemsArray);
			mcs.sendToRemote(BUNDLE_MESSAGE.ADMINISTRATION_COMMAND, LocationCommand.reply(a, true, ""));
		} catch (DatabaseException e) {
			mcs.sendToRemote(BUNDLE_MESSAGE.ADMINISTRATION_COMMAND, LocationCommand.reply(a, false, "unable to list locations"));
		}
	}

	@Override
	public void onCategoryAdd(Category a,AdministrationCommand<Category> c) {
		Log.d("db_admin_adapter", "onCategoryAdd");		
		try {
			dbi.addCategory(a);
			mcs.sendToRemote(BUNDLE_MESSAGE.ADMINISTRATION_COMMAND, CategoryCommand.reply(c, true, ""));
		} catch (DatabaseException e) {
			mcs.sendToRemote(BUNDLE_MESSAGE.ADMINISTRATION_COMMAND, CategoryCommand.reply(c, false, "unable to add category"));
		}
	}

	@Override
	public void onCategoryDel(Category a,AdministrationCommand<Category> c) {
		Log.d("db_admin_adapter", "onCategoryDel");
		try {
			dbi.deleteCategory(a);
			mcs.sendToRemote(BUNDLE_MESSAGE.ADMINISTRATION_COMMAND, CategoryCommand.reply(c, true, ""));
		} catch (DatabaseException e) {
			mcs.sendToRemote(BUNDLE_MESSAGE.ADMINISTRATION_COMMAND, CategoryCommand.reply(c, false, "unable to remove category"));
		}
	}

	@Override
	public void onCategoryEdit(Category toEdit, Category editValues,AdministrationCommand<Category> c) {
		Log.d("db_admin_adapter", "onCategoryEdit");		
		try {
			dbi.editCategory(toEdit, editValues);
			mcs.sendToRemote(BUNDLE_MESSAGE.ADMINISTRATION_COMMAND, CategoryCommand.reply(c, true, ""));
		} catch (DatabaseException e) {
			mcs.sendToRemote(BUNDLE_MESSAGE.ADMINISTRATION_COMMAND, CategoryCommand.reply(c, false, "unable to edit category"));
		}
	}

	@Override
	public void onCategoryList(AdministrationCommand<Category> c) {
		Log.d("db_admin_adapter", "onCategoryList");	
		try {
			List<Category> categories = dbi.getCategories();
			Category[] itemsArray = categories.toArray(new Category[categories.size()]);
			c.setPayloadObjects(itemsArray);
			mcs.sendToRemote(BUNDLE_MESSAGE.ADMINISTRATION_COMMAND, CategoryCommand.reply(c, true, ""));
		} catch (DatabaseException e) {
			mcs.sendToRemote(BUNDLE_MESSAGE.ADMINISTRATION_COMMAND, CategoryCommand.reply(c, false, "unable to list categories"));
		}
	}

	

}
