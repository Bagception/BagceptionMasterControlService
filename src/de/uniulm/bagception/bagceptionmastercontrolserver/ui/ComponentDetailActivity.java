package de.uniulm.bagception.bagceptionmastercontrolserver.ui;

import java.lang.reflect.InvocationTargetException;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import de.philipphock.android.lib.logging.LOG;
import de.uniulm.bagception.bagceptionmastercontrolserver.R;

/**
 * An activity representing a single Component detail screen. This activity is
 * only used on handset devices. On tablet-size devices, item details are
 * presented side-by-side with a list of items in a
 * {@link ComponentListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing more than
 * a {@link ComponentDetailFragment}.
 */
public class ComponentDetailActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_component_detail);

		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// savedInstanceState is non-null when there is fragment state
		// saved from previous configurations of this activity
		// (e.g. when rotating the screen from portrait to landscape).
		// In this case, the fragment will automatically be re-added
		// to its container so we don't need to manually add it.
		// For more information, see the Fragments API guide at:
		//
		// http://developer.android.com/guide/components/fragments.html
		//
		if (savedInstanceState == null) {
			// Create the detail fragment and add it to the activity
			// using a fragment transaction.
				//			Bundle arguments = new Bundle();
				//			arguments.putString(
				//					ComponentDetailFragment.ARG_ITEM_ID,
				//					getIntent().getStringExtra(
				//							ComponentDetailFragment.ARG_ITEM_ID));
				//			ComponentDetailFragment fragment = new ComponentDetailFragment();
				//			fragment.setArguments(arguments);
				//			getSupportFragmentManager().beginTransaction()
				//					.add(R.id.component_detail_container, fragment).commit();
			
			
			String className= getIntent().getStringExtra(ComponentListActivity.FRAGMENT_CLASS);
			Class<?> clazz;
			Fragment toCreate=null;

			try {
				clazz = Class.forName(className);
				toCreate = (Fragment) clazz.getConstructor().newInstance();
				getFragmentManager().beginTransaction()
				.add(R.id.component_detail_container, toCreate).commit();
				return;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
			LOG.out(this, "ERRRRORRRR");

		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpTo(this, new Intent(this,
					ComponentListActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
