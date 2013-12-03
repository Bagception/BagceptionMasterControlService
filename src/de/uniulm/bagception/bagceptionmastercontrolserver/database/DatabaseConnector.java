package de.uniulm.bagception.bagceptionmastercontrolserver.database;

import de.philipphock.android.lib.logging.LOG;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;

public class DatabaseConnector {
	public static Item getItem(String id){
		LOG.out("", "id:");
		LOG.out("", id);
		if (id.equals("E2 00 34 12 DC 03 01 18 28 24 27 37 ")){
			return new Item("Regenjacke");
		}
		if (id.equals("E2 00 34 12 DC 03 01 18 28 24 27 40 ")){
			return new Item("T-Shirt");
		} 
		if (id.equals("E2 00 34 12 DC 03 01 18 28 24 27 39 ")){
			return new Item("Hose");
		} 
		if (id.equals("E2 00 34 12 DC 03 01 18 28 24 27 38 ")){
			return new Item("Trinken");
		} 
		
		return null;
	}
}
