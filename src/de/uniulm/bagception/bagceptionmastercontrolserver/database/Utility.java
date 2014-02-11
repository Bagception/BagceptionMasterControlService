package de.uniulm.bagception.bagceptionmastercontrolserver.database;

import java.io.ByteArrayOutputStream;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

public class Utility {
	
	 // convert from bitmap to byte array
	 public static byte[] getBytes(Bitmap bitmap) {
		 if(bitmap != null){
			  ByteArrayOutputStream stream = new ByteArrayOutputStream();
			  bitmap.compress(CompressFormat.PNG, 0, stream);
			  return stream.toByteArray();
		 } else return null;
	 }

	  // convert from byte array to bitmap
	 public static Bitmap getPhoto(byte[] image) {
		 if(image != null){
			 return BitmapFactory.decodeByteArray(image, 0, image.length);
		 } else return null;
	 }
}
