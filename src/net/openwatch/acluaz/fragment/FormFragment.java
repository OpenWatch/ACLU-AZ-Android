package net.openwatch.acluaz.fragment;

import java.text.ParseException;
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
import android.support.v4.app.Fragment;
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
public class FormFragment extends Fragment {
	private static final String TAG = "FormFragment";

	public JSONObject toJson(ViewGroup container, JSONObject json) {
		
		if(json == null)
			json = new JSONObject();
		View view;
		for (int x = 0; x < container.getChildCount(); x++) {
			view = container.getChildAt(x);
			
			if (EditText.class.isInstance(view)){
				if (view.getTag() != null) {
					try {
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
							JSONObject location = new JSONObject();
							location.put(getString(R.string.device_lat), ((Location)view.getTag(R.id.view_tag)).getLatitude());
							location.put(getString(R.string.device_lon), ((Location)view.getTag(R.id.view_tag)).getLongitude());
							json.put(view.getTag().toString(), location);
						} catch (JSONException e) {
							Log.e(TAG, "Error jsonifying toggle input");
							e.printStackTrace();
						}
					}
					
				}
			}
			
			// combine date and time fields into a single datetime
			if(json.has(getString(R.string.date_tag)) && json.has(getString(R.string.time_tag))){
				try {
					json.put(getString(R.string.datetime), combineDateAndTime(json.getString(getString(R.string.date_tag)), json.getString(getString(R.string.time_tag))));
					json.remove(getString(R.string.date_tag));
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
			return Constants.datetime_formatter.format(Constants.user_datetime_formatter.parse(date + " " + time));
		} catch (ParseException e) {
			Log.e(TAG, "Error formatting user facing date");
			e.printStackTrace();
			return "";	
		}
	}
	
	/**
	 * maps json fields to orm object fields
	 * @param json
	 */
	public static void jsonToDatabase(Context c, JSONObject json){
		Incident incident = new Incident();
		try {
			incident.first_name.set(json.getString(c.getString(R.string.first_name_tag)));
			incident.last_name.set(json.getString(c.getString(R.string.last_name_tag)));
			incident.address1.set(json.getString(c.getString(R.string.address1_tag)));
			incident.address2.set(json.getString(c.getString(R.string.address2_tag)));
			incident.city.set(json.getString(c.getString(R.string.city_tag)));
			incident.state.set(json.getString(c.getString(R.string.state_tag)));
			incident.zipcode.set(json.getInt(c.getString(R.string.zipcode_tag)));
			incident.phone.set(json.getInt(c.getString(R.string.phone_tag)));
			incident.agency.set(json.getString(c.getString(R.string.agency_tag)));
			incident.location.set(json.getString(c.getString(R.string.location_tag)));
			incident.datetime.set(json.getString(c.getString(R.string.datetime))); 
			incident.narrative.set(json.getString(c.getString(R.string.narrative_tag)));
			if(json.has(c.getString(R.string.device_location_tag))){
				incident.device_lat.set(json.getDouble(c.getString(R.string.device_lat)));
				incident.device_lon.set(json.getDouble(c.getString(R.string.device_lon)));
			}
			
			incident.save(c);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void writeJsonToPrefs(final String prefs_name,
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

	protected class fillFormFromPrefsTask extends
			AsyncTask<String, Void, SharedPreferences> {
		private static final String TAG = "fillFormFromPrefsTask";
		private ViewGroup container;

		fillFormFromPrefsTask(ViewGroup container) {
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
