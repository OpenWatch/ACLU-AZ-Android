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
package net.openwatch.acluaz.database;

import java.util.ArrayList;
import java.util.List;

import net.openwatch.acluaz.constants.Constants;
import net.openwatch.acluaz.constants.DBConstants;
import net.openwatch.acluaz.model.Incident;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.orm.androrm.DatabaseAdapter;
import com.orm.androrm.Model;

public class DatabaseManager {
	
	private static final String TAG = "DatabaseManager";
	
	private static boolean models_registered = false; // ensure this is only called once per app launch
	
	public static void setupDB(final Context app_context){
		
		registerModels(app_context);
		new Thread(new Runnable(){
			@Override
			public void run(){
				SharedPreferences profile = app_context.getSharedPreferences(Constants.APP_PREFS, 0);
				SharedPreferences.Editor editor = profile.edit();
				editor.putBoolean(Constants.DB_READY, true);
				editor.commit();
				Log.i(TAG, "db ready");
				
			}
		}).start();
		
	}
	
	public static void registerModels(Context app_context){
		if(models_registered)
			return;
		
		List<Class<? extends Model>> models = new ArrayList<Class<? extends Model>>();
		models.add(Incident.class);
		
		pointToDB();
		DatabaseAdapter adapter = DatabaseAdapter.getInstance(app_context);
		adapter.setModels(models);
		models_registered = true;
	}
	
	public static void pointToDB(){
		DatabaseAdapter.setDatabaseName(DBConstants.DB_NAME);
		Log.i(TAG, "pointToDB finished");
	}
	

}
