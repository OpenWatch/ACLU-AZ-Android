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
