package de.uniulm.bagception.bagceptionmastercontrolserver.database;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable{

	
	private String name;
	private String description;
	private ArrayList<String> tagIDs;
	
	
	
	public Item()		  { }
	public Item(Parcel in){ readFromParcel(in); }
	
	
	private void readFromParcel(Parcel in) {
		name = in.readString();
		description = in.readString();
		tagIDs = in.readArrayList(null);
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(name);
		out.writeString(description);
		out.writeStringList(tagIDs);
	}
	

}
