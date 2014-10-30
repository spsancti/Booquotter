package com.spsancti.booquotter.Servicing;

import java.util.Collections;
import java.util.List;

import org.geometerplus.android.fbreader.api.PluginApi;

import com.spsancti.booquotter.R;

import android.content.Context;
import android.net.Uri;

public class PluginInfo extends PluginApi.PluginInfo {
	@Override
	protected List<PluginApi.ActionInfo> implementedActions(Context context) {
		return Collections.<PluginApi.ActionInfo>singletonList(new PluginApi.MenuActionInfo(
			Uri.parse("http://vk.com/spsancti"),
			context.getText(R.string.app_name).toString(),
			Integer.MAX_VALUE
		));
	}


}
