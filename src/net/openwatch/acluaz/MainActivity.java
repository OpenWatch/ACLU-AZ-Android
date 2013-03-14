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
package net.openwatch.acluaz;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.bugsense.trace.BugSenseHandler;

import net.openwatch.acluaz.constants.Constants;
import net.openwatch.acluaz.database.DatabaseManager;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

public class MainActivity extends SherlockActivity {
	
	public static Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BugSenseHandler.initAndStartSession(getApplicationContext(), SECRETS.BUGSENSE_API_KEY);
		setContentView(R.layout.activity_main);
		this.getSupportActionBar().setDisplayShowHomeEnabled(false);
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
	
	public void changeLanguage(View v){
		Log.i("changeLanguage", "clicked");
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.change_language_dialog_title))
		.setMessage(getString(R.string.change_language_dialog_body))
		.setPositiveButton(getString(R.string.dialog_ok), new OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.setClassName("com.android.settings", "com.android.settings.LanguageSettings");            
				startActivity(intent);
			}
			
		}).setNegativeButton("Cancel", new OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
				
			
		}).show();
		
	}
	
	@Override
    public boolean onOptionsItemSelected (MenuItem item){
    	switch(item.getItemId()){
    	/*
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
	    	*/
	    	case R.id.menu_reports:
	    		Intent i = new Intent(this, IncidentFeedFragmentActivity.class);
	    		startActivity(i);
	    		break;
	    	case R.id.menu_about:
	    		Intent i2 = new Intent(this, AboutActivity.class);
	    		startActivity(i2);
	    		break;
    	}
    	return true;
    }
	public void onPause(){
		context = null;
		super.onPause();
	}
	
	public void onResume(){
		super.onResume();
		context = this;
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
