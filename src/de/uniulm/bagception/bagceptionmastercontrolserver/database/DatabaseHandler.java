package de.uniulm.bagception.bagceptionmastercontrolserver.database;

import de.uniulm.bagception.bagceptionmastercontrolserver.R;
import de.uniulm.bagception.bagceptionmastercontrolserver.R.layout;
import de.uniulm.bagception.bagceptionmastercontrolserver.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class DatabaseHandler extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_database_handler);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.database_handler, menu);
		return true;
	}

}
