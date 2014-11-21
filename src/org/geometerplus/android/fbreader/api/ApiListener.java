/*
 * This code is in the public domain.
 */

package org.geometerplus.android.fbreader.api;

public interface ApiListener {
	String EVENT_READ_MODE_OPENED = "startReading";
	String EVENT_READ_MODE_CLOSED = "stopReading";
	//String EVENT_NETWORK_BOOK_LIST_OPEN = "getNetworkBooks";
	String EVENT_BOOK_CHANGED = "changeBook";
	String EVENT_PAGE_TURNED = "turnPage";

	void onEvent(String event);
}
