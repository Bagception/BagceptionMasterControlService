package de.uniulm.bagception.bagceptionmastercontrolserver.ui.fragments;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.uniulm.bagception.bagceptionmastercontrolserver.R;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;

public class ItemListArrayAdapter extends ArrayAdapter<Item> {

	
	private List<Item> itemsIn;

	
	public ItemListArrayAdapter(Context context) {
		super(context, R.layout.item_elem);

		 	
	}
	
	public void setItemsIn(List<Item> itemsIn){
		this.itemsIn = itemsIn;
		notifyDataSetInvalidated();
	}
	

	public void addAll(List<Item> items,final HashSet<Long> itemsToPrefer) {
		Collections.sort(items,new Comparator<Item>() {

			@Override
			public int compare(Item lhs, Item rhs) {
				long id0 = lhs.getId();
				long id1 = rhs.getId();
				
				if (itemsToPrefer.contains(id0) && !itemsToPrefer.contains(id1)){
					return -1;
				}
				if (!itemsToPrefer.contains(id0) && !itemsToPrefer.contains(id1)){
					return 0;
				}
				if (itemsToPrefer.contains(id0) && itemsToPrefer.contains(id1)){
					return 0;
				}
				if (!itemsToPrefer.contains(id0) && itemsToPrefer.contains(id1)){
					return 1;
				}
					
				
				return 0;
			}

		});
		super.addAll(items);
	}
	
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_elem, null);
        }

        Item item = getItem(position);
        if (item!= null) {
            TextView itemView = (TextView) view.findViewById(R.id.name);
            ImageView imageView = (ImageView) view.findViewById(R.id.icon);
            if (itemView != null) {
                itemView.setText(item.getName());
            }
            if (imageView != null){
            	imageView.setImageBitmap(item.getImage());
            }
      
            
            if (itemsIn.contains(item)){
            	view.setBackgroundColor(Color.argb(50,182,255,182));
            	
            }else{
            	view.setBackgroundColor(Color.argb(50,255, 182, 182));
            }
         }

        return view;
    }

	


}
