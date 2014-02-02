package de.uniulm.bagception.bagceptionmastercontrolserver.ui;

import java.lang.reflect.InvocationTargetException;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import de.uniulm.bagception.bagceptionmastercontrolserver.R;
import de.uniulm.bagception.bagceptionmastercontrolserver.database.DatabaseTest;
import de.uniulm.bagception.bagceptionmastercontrolserver.listContent.ComponentFragmentsListContent;

/**
 * An activity representing a list of Components. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link ComponentDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ComponentListFragment} and the item details (if present) is a
 * {@link ComponentDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link ComponentListFragment.Callbacks} interface to listen for item
 * selections.
 */
public class ComponentListActivity extends FragmentActivity implements
		ComponentListFragment.Callbacks {

	public static final String FRAGMENT_CLASS = "fragment_class";
	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_component_list);

		if (findViewById(R.id.component_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((ComponentListFragment) getSupportFragmentManager()
					.findFragmentById(R.id.component_list))
					.setActivateOnItemClick(true);
		}
		
		//Intent i = new Intent(this, ItemHandler.class);
		//startActivity(i);
		Intent i = new Intent(this, DatabaseTest.class);
		startActivity(i);

		// TODO: If exposing deep links into your app, handle intents here.
	}

	/**
	 * Callback method from {@link ComponentListFragment.Callbacks} indicating
	 * that the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(
			ComponentFragmentsListContent.FragmentListItem item) {

		Fragment replaceWith = null;
		try {
			replaceWith = item.clazz.getConstructor().newInstance();
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
		}
		if (mTwoPane) {

			getFragmentManager().beginTransaction()
					.replace(R.id.component_detail_container, replaceWith)
					.commit();

		} else {

			Intent detailIntent = new Intent(this,
					ComponentDetailActivity.class);
			detailIntent
					.putExtra(FRAGMENT_CLASS, item.clazz.getCanonicalName());
			startActivity(detailIntent);
		}
	}
}
