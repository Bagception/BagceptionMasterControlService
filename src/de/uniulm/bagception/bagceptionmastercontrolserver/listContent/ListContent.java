package de.uniulm.bagception.bagceptionmastercontrolserver.listContent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Fragment;
import de.uniulm.bagception.bagceptionmastercontrolserver.ui.fragments.ServiceStatusFragment;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class ListContent {

	/**
	 * An array of sample (dummy) items.
	 */
	public static List<FragmentListItem> ITEMS = new ArrayList<FragmentListItem>();

	/**
	 * A map of sample (dummy) items, by ID.
	 */
	public static Map<String, FragmentListItem> ITEM_MAP = new HashMap<String, FragmentListItem>();

	static {
		// ###### Add Items Here ######### \\
		addItem(new FragmentListItem("1", "Services",ServiceStatusFragment.class));
		
		
	}

	public static synchronized void addItem(String name, Class<? extends Fragment> clazz){
		int index=ITEM_MAP.size()+1;
		addItem(new FragmentListItem(index+"", name, clazz));
	}
	private static void addItem(FragmentListItem item) {
		ITEMS.add(item);
		ITEM_MAP.put(item.id, item);
	}

	/**
	 * A dummy item representing a piece of content.
	 */
	public static class FragmentListItem {
		public String id;
		public String content;
		public Class<? extends Fragment> clazz;
		public FragmentListItem(String id, String content,Class<? extends Fragment> clazz) {
			this.id = id;
			this.content = content;
			this.clazz=clazz;
		}

		@Override
		public String toString() {
			return content;
		}
	}
}
