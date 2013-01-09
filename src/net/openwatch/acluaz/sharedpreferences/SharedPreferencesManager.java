//  Created by David Brodsky
//  Copyright (c) 2013 OpenWatch FPC. All rights reserved.
//
//  This file is part of ACLU-AZ-Android.
//
//  ACLU-AZ-Android is free software: you can redistribute it and/or modify
//  it under the terms of the GNU General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  ACLU-AZ-Android is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
//
//  You should have received a copy of the GNU General Public License
//  along with ACLU-AZ-Android.  If not, see <http://www.gnu.org/licenses/>.
package net.openwatch.acluaz.sharedpreferences;

import net.openwatch.acluaz.R;
import net.openwatch.acluaz.constants.Constants;
import net.openwatch.acluaz.fragment.FormFragment;
import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

public class SharedPreferencesManager {
	private static final String TAG = "SharedPreferencesManager";
	
	public static void clearPrefsAndForm(Context c, FormFragment f, final String prefs_name){
    	if(prefs_name.compareTo(Constants.PERSONAL_PREFS) == 0){
    		c.getSharedPreferences(prefs_name, c.MODE_PRIVATE).edit().clear().commit();
    		//((FormFragment)mTabsAdapter.getItem(0)).fillFormFromPrefs
    		(f).clearForm((ViewGroup) f.getView().findViewById(R.id.personal_form_container));
    	}
	}
	
	public static void clearPrefs(Context c, final String prefs_name){
		c.getSharedPreferences(prefs_name, c.MODE_PRIVATE).edit().clear().commit();
		Log.i(TAG, "cleared prefs: " + prefs_name);
	}

}
