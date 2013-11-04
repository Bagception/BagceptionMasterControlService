package de.uniulm.bagception.bagceptionmastercontrolserver.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		// Add 3 sample items.
		addItem(new FragmentListItem("1", "Services"));
		addItem(new FragmentListItem("2", "Log"));
		addItem(new FragmentListItem("3", "About"));
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

		public FragmentListItem(String id, String content) {
			this.id = id;
			this.content = content;
		}

		@Override
		public String toString() {
			return content;
		}
	}
}
