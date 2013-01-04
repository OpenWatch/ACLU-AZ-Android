package net.openwatch.acluaz.fragment;

import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.openwatch.acluaz.R;
import net.openwatch.acluaz.constants.Constants;
import net.openwatch.acluaz.constants.DBConstants;
import net.openwatch.acluaz.model.Incident;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import com.actionbarsherlock.app.SherlockFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;

/**
 * Fragment that provides toJson() method, which outputs a JSON object with keys
 * equal to EditText assignable view's tag's and values equal to the text
 * content
 * 
 * @author davidbrodsky
 * 
 */
public class FormFragment extends SherlockFragment {
	private static final String TAG = "FormFragment";

	public JSONObject toJson(ViewGroup container, JSONObject json) {
		String TAG = "FormFragment-ToJSON";
		if(container == null){
			Log.e(TAG, "null container passed to toJson");
			return new JSONObject();
		}
		
		if(json == null)
			json = new JSONObject();
		View view;
		for (int x = 0; x < container.getChildCount(); x++) {
			view = container.getChildAt(x);
			
			if (EditText.class.isInstance(view)){
				if (view.getTag() != null) {
					if( ((EditText)view).getText().toString().compareTo("") == 0)
						continue; // skip blank input
					try {
						//Log.i(TAG, "Mapping: " + view.getTag().toString() + " value: " + ((EditText)view).getText().toString());
						if(view.getTag().toString().compareTo(getString(R.string.zipcode_tag)) == 0 )
							json.put(view.getTag().toString(), Integer.parseInt(((EditText)view).getText()
										.toString()));
						else
							json.put(view.getTag().toString(), ((EditText)view).getText()
									.toString());
					} catch (JSONException e) {
						Log.e(TAG, "Error jsonifying text input");
						e.printStackTrace();
					}
	
				}
			} else if(CompoundButton.class.isAssignableFrom(view.getClass())){
				if (view.getTag() != null){
					// if location toggle, bundle location
					if( ((String)view.getTag()).compareTo(getString(R.string.device_location_tag)) == 0 && view.getTag(R.id.view_tag) != null){
						try {
							json.put(getString(R.string.device_lat), ((Location)view.getTag(R.id.view_tag)).getLatitude());
							json.put(getString(R.string.device_lon), ((Location)view.getTag(R.id.view_tag)).getLongitude());
						} catch (JSONException e) {
							Log.e(TAG, "Error jsonifying toggle input");
							e.printStackTrace();
						}
					}
					
				}
			}
			
			// combine date and time fields into a single datetime
			if(json.has(getString(R.string.date_tag)) && json.has(getString(R.string.time_tag))){
				Log.i(TAG, "found date and time tag, let's smush 'em");
				try {
					//TESTING
					//String datetime = combineDateAndTime(json.getString(getString(R.string.date_tag)), json.getString(getString(R.string.time_tag)));
					//Log.i(TAG,"datetime: " + datetime);
					json.put(getString(R.string.datetime), combineDateAndTime(json.getString(getString(R.string.date_tag)), json.getString(getString(R.string.time_tag))));
					Log.i(TAG, json.toString());
					//json.remove(getString(R.string.date_tag));
					json.remove(getString(R.string.time_tag));
				} catch (JSONException e) {
					Log.e(TAG, "Error creating json datetime field from date and time");
					e.printStackTrace();
				}
			}
			
		}
		Log.i(TAG, "toJson: " + json.toString());
		return json;

	}
	
	/**
	 * Converts user-facing datetime to machine format
	 * 
	 * @param date
	 * @param time
	 * @return
	 */
	private String combineDateAndTime(String date, String time){
		try {
			String datetime = date + " " + time;
			Date date_obj = Constants.user_datetime_formatter.parse(datetime);
			String output_date = Constants.datetime_formatter.format(date_obj);
			return Constants.datetime_formatter.format(Constants.user_datetime_formatter.parse(date + " " + time));
		} catch (ParseException e) {
			Log.e(TAG, "Error formatting user facing date");
			e.printStackTrace();
			return "";	
		}
	}
	
	public static int insertIncidentInDatabase(Context c, JSONObject json){
		return jsonToDatabase(c, json, -1);
	}
	
	public static int updateIncidentInDatabase(Context c, JSONObject json, int existing_db_id){
		return jsonToDatabase(c, json, existing_db_id);
	}
	
	/**
	 * maps json fields to orm object fields
	 * @param json
	 */
	private static int jsonToDatabase(Context c, JSONObject json, int existing_db_id){
		String TAG = "FormFragment-jsonToDatabase";
		Incident incident = null;
		
		if(existing_db_id == -1){
			incident = new Incident();
			incident.uuid.set(Constants.generateUUID());
		}else
			incident = Incident.objects(c).get(existing_db_id);
		
		try {
			JSONObject user_json;
			if(json.has(c.getString(R.string.user_tag))){
				user_json = json.getJSONObject(c.getString(R.string.user_tag));
			
				if(user_json.has(c.getString(R.string.first_name_tag)))
					incident.first_name.set(user_json.getString(c.getString(R.string.first_name_tag)));
				if(user_json.has(c.getString(R.string.last_name_tag)))
					incident.last_name.set(user_json.getString(c.getString(R.string.last_name_tag)));
				if(user_json.has(c.getString(R.string.address1_tag)))
					incident.address_1.set(user_json.getString(c.getString(R.string.address1_tag)));
				if(user_json.has(c.getString(R.string.address2_tag)))
					incident.address_2.set(user_json.getString(c.getString(R.string.address2_tag)));
				if(user_json.has(c.getString(R.string.city_tag)))
					incident.city.set(user_json.getString(c.getString(R.string.city_tag)));
				if(user_json.has(c.getString(R.string.state_tag)))
					incident.state.set(user_json.getString(c.getString(R.string.state_tag)));
				if(user_json.has(c.getString(R.string.zipcode_tag)))
					incident.zip.set(user_json.getInt(c.getString(R.string.zipcode_tag)));
				if(user_json.has(c.getString(R.string.phone_tag)))
					incident.phone.set(user_json.getString(c.getString(R.string.phone_tag)));
				if(user_json.has(c.getString(R.string.email_tag)))
					incident.email.set(user_json.getString(c.getString(R.string.email_tag)));
			} else{
				Log.e(TAG, "no user object present");
			}
			JSONObject report_json;
			if(json.has(c.getString(R.string.report_tag))){
				report_json = json.getJSONObject(c.getString(R.string.report_tag));
			
				if(report_json.has(c.getString(R.string.agency_tag)))
					incident.agency.set(report_json.getString(c.getString(R.string.agency_tag)));
				if(report_json.has(c.getString(R.string.location_tag)))
					incident.location.set(report_json.getString(c.getString(R.string.location_tag)));
				if(report_json.has(c.getString(R.string.datetime)))
					incident.datetime.set(report_json.getString(c.getString(R.string.datetime))); 
				if(report_json.has(c.getString(R.string.narrative_tag)))
					incident.description.set(report_json.getString(c.getString(R.string.narrative_tag)));
				if(report_json.has(c.getString(R.string.device_location_tag))){
					incident.device_lat.set(report_json.getDouble(c.getString(R.string.device_lat)));
					incident.device_lon.set(report_json.getDouble(c.getString(R.string.device_lon)));
				}
			}else{
				Log.e(TAG, "no report object present");
			}
			
			incident.save(c);
			return incident.getId();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	
	public static JSONObject addUuidToJson(Context c, JSONObject json, int db_id){
		
		try {
			json.put(c.getString(R.string.uuid_tag), Incident.objects(c).get(db_id).uuid.get());
		} catch (Exception e) {
			Log.e("addUuidToJson", "Error adding uuid");
			e.printStackTrace();
		}
		return json;
	}

	public void writeJsonToPrefs(final String prefs_name,
			final JSONObject json) {
		final Context c = this.getActivity();
		new Thread(new Runnable() {

			@Override
			public void run() {
				Iterator<?> keys = json.keys();
				SharedPreferences.Editor prefs = c.getSharedPreferences(
						prefs_name, c.MODE_PRIVATE).edit();
				String key;
				while (keys.hasNext()) {
					key = (String) keys.next();
					try {
						prefs.putString(key, json.get(key).toString());
					} catch (JSONException e) {
						Log.e(TAG,
								"Error writing json to prefs. JSON: "
										+ json.toString());
						e.printStackTrace();
					}
				}
				prefs.commit();

			}

		}).start();
	}
	
	public void clearForm(ViewGroup container){
		View view;
		for (int x = 0; x < container.getChildCount(); x++) {
			view = container.getChildAt(x);
			
			if (EditText.class.isInstance(view)){
				if (view.getTag() != null) {
					((EditText)view).setText("");
				}
			}
		}
	}

	public class fillFormFromPrefsTask extends
			AsyncTask<String, Void, SharedPreferences> {
		private static final String TAG = "FormFragment-fillFormFromPrefsTask";
		private ViewGroup container;

		public fillFormFromPrefsTask(ViewGroup container) {
			this.container = container;
		}

		protected SharedPreferences doInBackground(String... pref_name_in) {
			if (container == null)
				return null;

			String prefs_name = pref_name_in[0];
			SharedPreferences prefs = container.getContext()
					.getSharedPreferences(prefs_name, Context.MODE_PRIVATE);
			return prefs;

		}

		protected void onProgressUpdate(Void... progress) {
		}

		protected void onPostExecute(SharedPreferences prefs) {
			if(prefs == null)
				return;
			
			Map<String, ?> keys = prefs.getAll();

			View form_input;
			for (Map.Entry<String, ?> entry : keys.entrySet()) {
				form_input = container.findViewWithTag(entry.getKey());
				setFormFieldValue(form_input, entry);
			}
			return;
		}
	}
	
	private void setFormFieldValue(View form_field, Entry map_entry){
		if (form_field != null && map_entry.getValue() != null) {
			if(EditText.class.isInstance(form_field))
				((EditText)form_field).setText(map_entry.getValue().toString());
			else if(CompoundButton.class.isInstance(form_field))
				((CompoundButton)form_field).setChecked(Boolean.parseBoolean(map_entry.getValue().toString()));
			Log.d(TAG, "map values: " + map_entry.getKey() + ": "
					+ map_entry.getValue().toString());
		} else {
			if (form_field == null)
				Log.d(TAG,
						"Could not find view with tag: "
								+ map_entry.getKey());
			if (map_entry.getValue() == null)
				Log.d(TAG,
						"No value for prefs with key: "
								+ map_entry.getKey());
		}
	}
	
	/**
	 * Populate form given the db_id of an Incident in the database
	 * This currently assumes that the database columns are equal to the view_tags (json keys)
	 * @param container
	 * @param db_id
	 */
	protected void fillFormFromDatabase(ViewGroup container, int db_id){
		String TAG = "FormFragment-fillFormFromDatabase";
		Incident incident = Incident.objects(this.getActivity().getApplicationContext()).get(db_id);
		if(incident == null)
			return;
		ContentValues values = new ContentValues();
		try {
			incident.collectData(getActivity().getApplicationContext(), values, Incident.class);
		} catch (IllegalArgumentException e) {
			Log.e(TAG, "Unable to collect ContentValues from Incident");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			Log.e(TAG, "Unable to collect ContentValues from Incident");
			e.printStackTrace();
		}
		
		View form_input;
		for(Entry<String, ?> entry : values.valueSet()){
			
			if(entry.getKey().compareTo(getString(R.string.device_lat)) == 0 || entry.getKey().compareTo(getString(R.string.device_lon)) == 0){
				// Combine lat and lon into a Location and tag the gps toggle
				form_input = container.findViewById(R.id.gps_toggle);
				Location loc = new Location("db");
				loc.setLatitude(values.getAsDouble(DBConstants.DEVICE_LAT));
				loc.setLongitude(values.getAsDouble(DBConstants.DEVICE_LON));
				form_input.setTag(R.id.view_tag, loc);
			} else{
				// If the column value is simply bound to the view
				// with tag equal to column name...
				form_input = container.findViewWithTag(entry.getKey());
				setFormFieldValue(form_input, entry);
			}
		}
		
	}

}
