/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.openwatch.acluaz;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import net.openwatch.acluaz.adapter.IncidentAdapter;
import net.openwatch.acluaz.constants.Constants;
import net.openwatch.acluaz.constants.DBConstants;
import net.openwatch.acluaz.contentprovider.ACLUAZContentProvider;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.Loader;
import android.support.v4.widget.SearchViewCompat.OnQueryTextListenerCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Demonstration of the implementation of a custom Loader.
 */
public class IncidentFeedFragmentActivity extends SherlockFragmentActivity {
	
	private static final String TAG = "IncidentFeedFragmentActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Show the Up button in the action bar.
     	this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FragmentManager fm = getSupportFragmentManager();

        // Create the list fragment and add it as our sole content.
        if (fm.findFragmentById(android.R.id.content) == null) {
            LocalRecordingsListFragment list = new LocalRecordingsListFragment();
            fm.beginTransaction().add(android.R.id.content, list).commit();
        }
    }
  
    @Override
    public boolean onOptionsItemSelected (MenuItem item){
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	} 
	


    public static class LocalRecordingsListFragment extends ListFragment
            implements LoaderManager.LoaderCallbacks<Cursor> {

        // This is the Adapter being used to display the list's data.
    	IncidentAdapter mAdapter;

        // If non-null, this is the current filter the user has provided.
        String mCurFilter;

        OnQueryTextListenerCompat mOnQueryTextListenerCompat;

        @Override public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            // Give some text to display if there is no data.  In a real
            // application this would come from a resource.
            //setEmptyText(getString(R.string.loading_incidents));

            // We have a menu item to show in action bar.
            setHasOptionsMenu(true);

            // Initialize adapter without cursor. Let loader provide it when ready
            mAdapter = new IncidentAdapter(getActivity(), null); 
            setListAdapter(mAdapter);

            // Start out with a progress indicator.
            //setListShown(false);

            // Prepare the loader.  Either re-connect with an existing one,
            // or start a new one.
            getLoaderManager().initLoader(0, null, this);
        }

        @Override 
        public void onListItemClick(ListView l, View v, int position, long id) {
        	Intent i = new Intent(this.getActivity(), FormFragmentActivity.class);
        	try{
        		i.putExtra(Constants.INTERNAL_DB_ID, (Integer)v.getTag(R.id.list_item_model));
        	}catch(Exception e){
        		Log.e(TAG, "failed to load list item model tag");
        		return;
        	}
        	startActivity(i);
        	
        }
        
        public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        	return inflater.inflate(R.layout.report_feed, container, false);
        }

        
		@Override
		public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
			mAdapter.swapCursor(cursor);
			// The list should now be shown.
            if (isResumed()) {
                //setListShown(true);
            } else {
                //setListShownNoAnimation(true);
            }
            
           if(cursor != null && cursor.getCount() == 0){
        		//setEmptyText(getString(R.string.no_incidents));
           }
			
		}

		@Override
		public void onLoaderReset(Loader<Cursor> arg0) {
			// TODO Auto-generated method stub
			mAdapter.swapCursor(null);
		}
		
		static final String[] PROJECTION = new String[] {
			DBConstants.ID,
			DBConstants.LOCATION,
			DBConstants.DATE,
			DBConstants.SUBMITTED

	    };

		@Override
		public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
			// TODO Auto-generated method stub
			Uri baseUri = ACLUAZContentProvider.INCIDENT_URI;
			String selection = null;
            String[] selectionArgs = null;
            String order = DBConstants.DATE + " DESC";
			
			return new CursorLoader(getActivity(), baseUri, PROJECTION, selection, selectionArgs, order);
		}
    }

}
