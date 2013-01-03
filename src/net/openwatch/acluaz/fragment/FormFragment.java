package net.openwatch.acluaz.fragment;

import java.util.Iterator;
import java.util.Map;

import net.openwatch.acluaz.Constants;
import net.openwatch.acluaz.R;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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

	protected JSONObject toJson(ViewGroup container) {

		JSONObject json = new JSONObject();
		EditText view;
		for (int x = 0; x < container.getChildCount(); x++) {
			if (!EditText.class.isInstance(container.getChildAt(x)))
				continue;

			view = (EditText) container.getChildAt(x);
			if (view.getTag() != null) {
				try {
					json.put(view.getTag().toString(), view.getText()
							.toString());
				} catch (JSONException e) {
					Log.e(TAG, "Error jsonifying form input");
					e.printStackTrace();
				}

			}
		}
		Log.i(TAG, "toJson: " + json.toString());
		return json;

	}

	protected void writeJsonToPrefs(final String prefs_name, final JSONObject json) {
		final Context c = this.getActivity();
		new Thread(new Runnable() {

			@Override
			public void run() {
				Iterator<?> keys = json.keys();
				SharedPreferences.Editor prefs = c.getSharedPreferences(prefs_name, c.MODE_PRIVATE).edit();
				String key;
				while (keys.hasNext()) {
					key = (String) keys.next();
					try {
						prefs.putString(key, json.get(key).toString());
					} catch (JSONException e) {
						Log.e(TAG, "Error writing json to prefs. JSON: "
										+ json.toString());
						e.printStackTrace();
					}
				}	
				prefs.commit();

			}

		}).start();
	}
	
	protected class fillFormFromPrefsTask extends AsyncTask<String, Void, Void> {
		private static final String TAG = "fillFormFromPrefsTask";
		private ViewGroup container;
		
		fillFormFromPrefsTask(ViewGroup container){
			this.container = container;
		}
	     protected Void doInBackground(String... pref_name_in) {
	    	 if(container == null)
	    		 return null;
	    	 
	         String prefs_name = pref_name_in[0];
	         SharedPreferences prefs = container.getContext().getSharedPreferences(prefs_name, Context.MODE_PRIVATE);
	         Map<String,?> keys = prefs.getAll();
	         
	         EditText form_input;
	         for(Map.Entry<String,?> entry : keys.entrySet()){
				 form_input = (EditText) container.findViewWithTag(entry.getKey());
				 if(form_input != null && entry.getValue() != null){
					 form_input.setText(entry.getValue().toString());
					 Log.d(TAG,"map values: " + entry.getKey() + ": " + entry.getValue().toString());  
				 }
				 else{
					 if(form_input == null)
						 Log.d(TAG, "Could not find view with tag: " + entry.getKey());
					 if(entry.getValue() == null)
						 Log.d(TAG, "No value for prefs with key: " + entry.getKey());
				 }
	         }
	         return null;
	     }

	     protected void onProgressUpdate(Void... progress) {
	     }

	     protected void onPostExecute(Void result) {
	     }
	 }

}
