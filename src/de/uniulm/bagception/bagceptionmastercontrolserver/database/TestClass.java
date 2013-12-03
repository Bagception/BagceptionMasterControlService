package de.uniulm.bagception.bagceptionmastercontrolserver.database;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

public class TestClass implements Parcelable.Creator<Item>{
	
	
	public TestClass(){
	}

	@Override
	public Item createFromParcel(Parcel source) {
		return new Item(source);
	}

	@Override
	public Item[] newArray(int size) {
		// TODO Auto-generated method stub
		return new Item[size];
	}

}
