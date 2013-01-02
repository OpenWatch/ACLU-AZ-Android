package net.openwatch.acluaz.fragment;

import net.openwatch.acluaz.R;

import org.json.JSONException;
import org.json.JSONObject;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Fragment that provides toJson() method, which 
 * outputs a JSON object with keys equal to 
 * EditText assignable view's tag's
 * and values equal to the text content
 * 
 * @author davidbrodsky
 *
 */
public class FormFragment extends Fragment {
	private static final String TAG = "FormFragment";
	
	
	protected String toJson(int container_id){
		ViewGroup container = ((ViewGroup) this.getView().findViewById(container_id));
		JSONObject json = new JSONObject();
		EditText view;
		for(int x=0; x < container.getChildCount(); x++){
			if(!EditText.class.isInstance(container.getChildAt(x)))
				continue;
			
			view = (EditText) container.getChildAt(x);
			if(view.getTag() != null){
				try {
					json.put(view.getTag().toString(), view.getText().toString());
				} catch (JSONException e) {
					Log.e(TAG, "Error jsonifying form input");
					e.printStackTrace();
				}
				
			}
		}
		Log.i(TAG, "toJson: " + json.toString());
		return json.toString();
		
	}
	

}
