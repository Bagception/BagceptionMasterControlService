package de.uniulm.bagception.bagceptionmastercontrolserver.database;

//import android.content.ContentResolver;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BagceptionOpenHelper extends SQLiteOpenHelper{
	
	private static final String NAME = DbSchema.DB_NAME;
	private static final int VERSION = 1;
	
	public BagceptionOpenHelper(Context context) {
		super(context, NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL("PRAGMA foreign_keys=ON;");
		db.execSQL(DbSchema.CREATE_TBL_ITEMS);
		db.execSQL(DbSchema.CREATE_TBL_PHOTOS);
		db.execSQL(DbSchema.CREATE_TBL_CATEGORIES);
		db.execSQL(DbSchema.CREATE_TBL_ACTIVITIES);
		db.execSQL(DbSchema.CREATE_TBL_ACTIVITYITEMS);
		db.execSQL(DbSchema.CREATE_TBL_LOCATIONS);
		db.execSQL(DbSchema.CREATE_TBL_ITEMCONTEXT);
		db.execSQL(DbSchema.CREATE_TBL_WEATHER);
		db.execSQL(DbSchema.CREATE_TBL_TIME);
		db.execSQL(DbSchema.CREATE_TRIGGER_DEL_ITEMS);	
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("PRAGMA foreign_keys=ON;");
		db.execSQL(DbSchema.DROP_TBL_ITEMS);
		db.execSQL(DbSchema.CREATE_TBL_PHOTOS);
		db.execSQL(DbSchema.DROP_TBL_CATEGORIES);
		db.execSQL(DbSchema.CREATE_TBL_ACTIVITIES);
		db.execSQL(DbSchema.CREATE_TBL_ACTIVITYITEMS);
		db.execSQL(DbSchema.CREATE_TBL_LOCATIONS);
		db.execSQL(DbSchema.CREATE_TBL_ITEMCONTEXT);
		db.execSQL(DbSchema.CREATE_TBL_WEATHER);
		db.execSQL(DbSchema.CREATE_TBL_TIME);
		db.execSQL(DbSchema.DROP_TRIGGER_DEL_ITEMS);
		onCreate(db);
	}

	
}
