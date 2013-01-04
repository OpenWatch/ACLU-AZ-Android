package net.openwatch.acluaz.http;

import java.io.UnsupportedEncodingException;
import net.openwatch.acluaz.constants.Constants;
import net.openwatch.acluaz.model.Incident;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * OWService (Django) Requests 
 * @author davidbrodsky
 *
 */
public class OWServiceRequests {
	
	private static final String TAG = "OWServiceRequests";
	
	public interface RequestCallback{
		public void onFailure();
		public void onSuccess();
	}
	
	public static void postReport(final Context app_context, JSONObject json, final int db_id){
		JsonHttpResponseHandler response_handler = new JsonHttpResponseHandler(){
			
			@Override
    		public void onSuccess(JSONObject response){
				Log.i(TAG, "postReport response: " + response.toString());
				try {
					if(response.has(Constants.API_SUCCESS) && response.getBoolean(Constants.API_SUCCESS) == true){
						Incident incident = Incident.objects(app_context).get(db_id);
						incident.submitted.set(true);
						if(response.has(Constants.API_REPORT_ID)){
							incident.server_id.set(response.getInt(Constants.API_REPORT_ID));
						}
						incident.save(app_context);
					}
				} catch (JSONException e) {
					Log.e(TAG, "Error parsing json response");
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailure(Throwable e, JSONObject response){
				Log.e(TAG, "postReport failure " + response.toString());
				e.printStackTrace();
			}
		};
		
		postReport(app_context, json, response_handler);
	}

	/**
	 * Post report data to server
	 */
	public static void postReport(Context app_context, JSONObject json, JsonHttpResponseHandler response_handler){
    	AsyncHttpClient http_client = HttpClient.setupHttpClient(app_context);
    	http_client.post(app_context, Constants.REPORT_SUBMIT_URL, jsonToSE(json), "application/json", response_handler);
    	
    }
	
	public static StringEntity jsonToSE(JSONObject json){
		StringEntity se = null;
		try {
			se = new StringEntity(json.toString());
			Log.i(TAG, "reportToJSON: " + json.toString());
			return se;
		} catch (UnsupportedEncodingException e1) {
			Log.e(TAG, "json->stringentity failed");
			e1.printStackTrace();
		}
		
		return se;
	}
	
}