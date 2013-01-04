package net.openwatch.acluaz;

import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import net.openwatch.acluaz.constants.Constants;
import net.openwatch.acluaz.database.DatabaseManager;
import net.openwatch.acluaz.fragment.FormFragment;
import net.openwatch.acluaz.sharedpreferences.SharedPreferencesManager;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public void onReportBtnClicked(View v){
		Intent i = new Intent(this, FormFragmentActivity.class);
		startActivity(i);
	}
	
	public void onRightsBtnClicked(View v){
		Intent i = new Intent(this, RightsActivity.class);
		startActivity(i);
	}
	
	@Override
    public boolean onOptionsItemSelected (MenuItem item){
    	switch(item.getItemId()){
	    	case R.id.menu_clear_personal:
	    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    		builder.setTitle(getString(R.string.clear_personal_info_dialog_title))
	    		.setMessage(getString(R.string.clear_personal_info_dialog_msg))
	    		.setPositiveButton(getString(R.string.clear_personal_info_ok_btn), new OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						SharedPreferencesManager.clearPrefs(MainActivity.this, Constants.PERSONAL_PREFS);
						dialog.dismiss();
					}
	    			
	    		}).setNegativeButton(getString(R.string.clear_personal_info_cancel_btn), new OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
	    			
	    		}).show();
	    		
	    		break;
	    	case R.id.menu_reports:
	    		Intent i = new Intent(this, IncidentFeedFragmentActivity.class);
	    		startActivity(i);
	    		break;
    	}
    	return true;
    }
	
	public void onResume(){
		super.onResume();
		
		SharedPreferences profile = getSharedPreferences(Constants.APP_PREFS, 0);
		boolean db_initialized = profile.getBoolean(Constants.DB_READY, false);
	
		if(!db_initialized){
			DatabaseManager.setupDB(getApplicationContext()); // do this every time to auto handle migrations
			//DatabaseManager.testDB(this);
		}else{
			DatabaseManager.registerModels(getApplicationContext()); // ensure androrm is set to our custom Database name.
		}
	}

}
