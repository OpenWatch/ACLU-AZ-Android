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
