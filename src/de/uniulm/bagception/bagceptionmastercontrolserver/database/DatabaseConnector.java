package de.uniulm.bagception.bagceptionmastercontrolserver.database;

import java.util.ArrayList;
import java.util.HashMap;

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
