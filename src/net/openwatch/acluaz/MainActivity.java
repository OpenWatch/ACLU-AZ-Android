package net.openwatch.acluaz;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;

import net.openwatch.acluaz.constants.Constants;
import net.openwatch.acluaz.database.DatabaseManager;
import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

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
		Intent i = new Intent(this, FormActivity.class);
		startActivity(i);
	}
	
	public void onRightsBtnClicked(View v){
		
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
