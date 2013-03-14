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
package net.openwatch.acluaz.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import net.openwatch.acluaz.R;
import net.openwatch.acluaz.constants.Constants;
import net.openwatch.acluaz.model.Incident;
import net.openwatch.acluaz.sharedpreferences.SharedPreferencesManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

/**
 * OWService (Django) Requests
 * 
 * @author davidbrodsky
 * 
 */
public class OWServiceRequests {

	private static final String TAG = "OWServiceRequests";

	private static void showReportSubmittedDialog(Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.form_success_title).setMessage(R.string.form_success).setPositiveButton(R.string.dialog_ok, new OnClickListener(){

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				arg0.dismiss();
			}
		}).show();
	}

	public static StringEntity jsonToSE(JSONObject json) {
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
	
	public static void postReport(final Context context, final JSONObject report, final int db_id) {
        new OWServiceRequests.PostReportTask(context, db_id).execute(report);

	}
	
	static class PostReportTask extends AsyncTask<JSONObject, Void, Void>{
		Context context;
		boolean success = false;
		int db_id = -1;
		
		public PostReportTask(Context c, int db_id){
			context = c;
			this.db_id = db_id;
		}

		@Override
		protected Void doInBackground(JSONObject... params) {
			JSONObject report = params[0];
			AZHttpClient client = new AZHttpClient(context);
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
            HttpResponse response;
            try{
                HttpPost post = new HttpPost(Constants.REPORT_SUBMIT_URL);
                Log.i(TAG, "POSTing JSON: " + report.toString());
                StringEntity se = new StringEntity( report.toString(), HTTP.UTF_8);  
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                post.setEntity(se);
                response = client.execute(post);
                
                /*Checking response */
                if(response!=null && response.getStatusLine().getStatusCode() == 200){
                	InputStream in;
                	in = response.getEntity().getContent(); //Get the data in the entity
                	BufferedReader r = new BufferedReader(new InputStreamReader(in));
                	StringBuilder total = new StringBuilder();
                	String responseStr;
                	while ((responseStr = r.readLine()) != null) {
                	    total.append(responseStr);
                	}
                	Log.i(TAG, "POST report response: " + total.toString());
                	JSONObject responseJson = new JSONObject(total.toString());
                	if (responseJson.has(Constants.API_SUCCESS) && responseJson.getBoolean(Constants.API_SUCCESS) == true && db_id != -1) {
						Incident incident = Incident.objects(context).get(
								db_id);
						incident.submitted.set(true);
						if (responseJson.has(Constants.API_REPORT_ID)) {
							incident.server_id.set(responseJson
									.getInt(Constants.API_REPORT_ID));
						}
						incident.save(context);
						SharedPreferencesManager.clearPrefs(context,
								Constants.INCIDENT_PREFS);
						success = true;
					}
                }
            }
            catch(Exception e){
            	Log.e(TAG, "Error processing POST report");
                e.printStackTrace();
                //createDialog("Error", "Cannot Estabilish Connection");
            }
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			if(context != null && success){
				showReportSubmittedDialog(context);
			}
	     }
		
	}
}