package de.uniulm.bagception.bagceptionmastercontrolserver.database;

import de.uniulm.bagception.bundlemessageprotocol.entities.Item;

public class DatabaseConnector {
	public static Item getItem(String id){
		if (id.equals("32 40 00 01 0E 30 00 E2 00 34 12 DC 03 01 18 28 24 27 37 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00")){
			return new Item("Regenjacke");
		}
		if (id.equals("32 40 00 01 0E 30 00 E2 00 34 12 DC 03 01 18 28 24 27 40 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00")){
			return new Item("T-Shirt");
		} 
		if (id.equals("32 40 00 01 0E 30 00 E2 00 34 12 DC 03 01 18 28 24 27 39 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00")){
			return new Item("Hose");
		} 
		if (id.equals("32 40 00 01 0E 30 00 E2 00 34 12 DC 03 01 18 28 24 27 38 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00")){
			return new Item("Trinken");
		} 
		
		return null;
	}
}
